import serial
import serial.tools.list_ports
import time
import sys
import gc
from datetime import datetime

BAUD_RATE = 9600
MAX_VOLTAGE_REF = 4.41
RECONNECT_DELAY = 2


def generate_filename():
    timestamp = datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
    return f"experiment_{timestamp}.txt"


def close_arduino(arduino):
    """Закрытие порта (вызывает сброс!)"""
    if arduino:
        try:
            arduino.dtr = False
            arduino.rts = False
            time.sleep(0.1)
            if arduino.is_open:
                arduino.flush()
                arduino.close()
        except:
            pass
        try:
            del arduino
        except:
            pass
        gc.collect()
    return None


def find_arduino_port():
    """Ищет доступный порт"""
    ports = serial.tools.list_ports.comports()
    ports.sort(key=lambda x: x.device)
    for port in ports:
        try:
            test_serial = serial.Serial(port.device, BAUD_RATE, timeout=0.1, dsrdtr=False)
            test_serial.close()
            print(f"✅ Найден свободный порт: {port.device} ({port.description})")
            return port.device
        except (serial.SerialException, PermissionError):
            continue
    return None


def initialize_port(port_name):
    """Первичная инициализация (допускает сброс)"""
    print(f"🔌 Инициализация порта {port_name}...")
    try:
        arduino = serial.Serial(port_name, BAUD_RATE, timeout=1, dsrdtr=False, rtscts=False)
        time.sleep(0.5)
        arduino.reset_input_buffer()
        arduino.reset_output_buffer()
        print(f"✅ Порт инициализирован")
        return arduino
    except Exception as e:
        print(f"❌ Ошибка инициализации: {e}")
        return None


def try_recover_without_closing(arduino):
    """
    Пытается восстановить связь БЕЗ закрытия порта.
    Это ключевая функция для предотвращения сброса.
    """
    if not arduino:
        return None

    try:
        # Проверяем, жив ли порт вообще
        if arduino.is_open:
            # Пытаемся прочитать что-то, чтобы проверить связь
            arduino.reset_input_buffer()
            arduino.reset_output_buffer()
            time.sleep(0.3)
            print("✅ Порт перепроверен без закрытия")
            return arduino
    except:
        pass

    return None


def main():
    # === ИНИЦИАЛИЗАЦИЯ ПЕРЕМЕННЫХ ===
    experiment_active = True
    time_counter = 0
    last_written_time = -1
    file = None
    arduino = None
    connection_lost = False
    port_was_closed = False  # Флаг: закрывали ли мы порт явно
    # ================================================

    # === ПОИСК ПОРТА ===
    print("🔍 Поиск Arduino...")
    PORT_NAME = find_arduino_port()
    if not PORT_NAME:
        print("❌ Не найдено свободных COM-портов.")
        sys.exit(1)

    file_name = generate_filename()
    print(f"📁 Файл для записи: {file_name}")

    try:
        file = open(file_name, "w", buffering=1)
        file.write("time, voltage\n")
        print("🟢 Начало записи данных... (Ctrl+C для остановки)")

        # 1. ПЕРВИЧНОЕ ПОДКЛЮЧЕНИЕ
        arduino = initialize_port(PORT_NAME)
        if not arduino:
            sys.exit(1)
        port_was_closed = False

        while experiment_active:
            # === ПРОВЕРКА СОЕДИНЕНИЯ ===
            if not arduino:
                connection_lost = True
            elif not arduino.is_open:
                connection_lost = True
            else:
                # Проверяем, не отключилось ли устройство на уровне ОС
                try:
                    # Попытка прочитать статус порта
                    _ = arduino.in_waiting
                except (serial.SerialException, OSError):
                    connection_lost = True
                    port_was_closed = True  # ОС разорвала соединение

            # === ВОССТАНОВЛЕНИЕ СОЕДИНЕНИЯ ===
            if connection_lost:
                print(f"🔄 Потеря связи...")

                # Если порт был закрыт ОС, придется создавать новый (будет сброс)
                if port_was_closed:
                    arduino = close_arduino(arduino)
                    time.sleep(RECONNECT_DELAY)
                    print(f"🔄 Пересоздание порта {PORT_NAME} (возможен сброс платы)...")
                    arduino = initialize_port(PORT_NAME)
                    if arduino:
                        connection_lost = False
                        port_was_closed = False
                        print("✅ Соединение восстановлено (через пересоздание)")
                    else:
                        print("⏳ Не удалось подключиться. Повтор...")
                        time.sleep(RECONNECT_DELAY)
                else:
                    # Порт открыт, но есть ошибки - пробуем оживить без закрытия
                    print("🔄 Попытка восстановления без закрытия порта...")
                    time.sleep(0.5)
                    recovered = try_recover_without_closing(arduino)
                    if recovered:
                        connection_lost = False
                        print("✅ Связь восстановлена без сброса!")
                    else:
                        # Не помогло, придется закрывать
                        arduino = close_arduino(arduino)
                        port_was_closed = True
                        time.sleep(RECONNECT_DELAY)

            # === ЧТЕНИЕ ДАННЫХ ===
            try:
                if arduino and arduino.is_open:
                    if arduino.in_waiting >= 2:
                        data = arduino.read(2)

                        if len(data) == 2 and data[0] == data[1]:
                            raw_val = data[0]
                            voltage = raw_val * MAX_VOLTAGE_REF / 512
                            time_counter += 1

                            if time_counter == last_written_time:
                                arduino.write(b'\x31')
                                continue

                            log_line = f"{time_counter}, {voltage:.2f}\n"
                            file.write(log_line)
                            last_written_time = time_counter

                            print(f"t={time_counter}s, U={voltage:.2f}V", end='\r')
                            arduino.write(b'\x31')
                        else:
                            arduino.write(b'\x30')

            except (serial.SerialException, OSError) as e:
                print(f"\n⚠️ Ошибка чтения/записи: {e}")
                connection_lost = True
                # Не закрываем порт сразу, пробуем восстановить
                continue
            except (ValueError, PermissionError) as e:
                print(f"\n⚠️ Критическая ошибка порта: {e}")
                connection_lost = True
                port_was_closed = True
                continue

    except KeyboardInterrupt:
        print("\n⚠️ Остановка пользователем")
    except IOError as e:
        print(f"\n❌ Ошибка записи в файл: {e}")
    finally:
        if file:
            file.close()
            print(f"💾 Файл сохранён: {file_name}")
        arduino = close_arduino(arduino)
        print("\n=== Эксперимент завершён ===")


if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        print(f"\n💥 Критическая ошибка: {e}")
        sys.exit(1)