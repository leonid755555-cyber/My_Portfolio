import matplotlib.pyplot as plt
import matplotlib.animation as animation
import linecache
import tkinter as tk
from tkinter import filedialog
import os
import time

time_scales = [
    'Секунды',
    'Минуты',
    'Часы'
]
# --- Глобальные данные для графика ---
file_name = 'data.txt'
file_pos = 0
file_cursor = 0
RESISTANCE = 1.5

max_voltage = 5.06
max_data_size = 400
limit_s = 100
VOLTAGE_THRESHOLD = 0.20
limit_m = 6000

times = []
times_per_dot = []
voltages = []
voltages_per_dot = []

resistans = 1.5
time_run = 0     # текущее время (сек)
final_time = 0
capacity = 0.0     # ёмкость в мА·ч
value_per_dot = 1
cursor = 0

minutes = False
hours = False
ex_run = True


# --- Настройка графика ---
fig, ax = plt.subplots(figsize=(8, 5))
line, = ax.plot([], [], 'k-', linewidth=2, label='График')
# Добавляем текстовую метку для вывода capacity
# x, y – координаты в системе графика (обычно в левом верхнем углу)
text_capacity = ax.text(0.01, 0.98, '', transform=ax.transAxes,
                        fontsize=12, verticalalignment='top',
                        bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.5))
text_time = ax.text(0.01, 0.8, '', transform=ax.transAxes,
                        fontsize=12, verticalalignment='top',
                        bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.5))

# Статические элементы графика (настраиваются один раз)
ax.set_ylim(-1, 2)
ax.axhline(0, color='gray', linewidth=0.8)
ax.axvline(0, color='gray', linewidth=0.8)
ax.legend(fontsize=12, loc='upper right')
ax.set_title('Ёмкость батарейки', fontsize=14)
ax.set_xlabel(time_scales[0], fontsize=12)
ax.set_ylabel('Напряжение (В)', fontsize=12)
ax.grid(True, linestyle='--', alpha=0.7)
ax.text(1.5, 5.5, f'Capacity: {capacity}', fontsize=12, color='blue')  # Выводим capacity в точке (1.5, 5.5)

def select_file():
    """Открывает диалог выбора файла"""
    root = tk.Tk()
    root.withdraw()  # Скрыть главное окно
    root.attributes('-topmost', True)  # Поверх других окон

    file_path = filedialog.askopenfilename(
        title="Выберите файл эксперимента",
        filetypes=[("Text files", "*.txt"), ("All files", "*.*")],
        initialdir="."
    )

    root.destroy()
    return file_path

# --- Функция сжатия данных ---
def compress():
    """Сжимает данные достигшие лимита"""
    global value_per_dot, cursor, voltages, times, max_data_size
    for i in range(0, max_data_size // 2):
        voltages[i] = (voltages[2 * i] + voltages[2 * i + 1]) / 2
        times[i] = (times[2 * i] + times[2 * i + 1]) / 2
    voltages = voltages[:-(max_data_size // 2)]
    times = times[:-(max_data_size // 2)]
    value_per_dot *= 2
    cursor = max_data_size / 2
# --- Функция инициализации анимации ---
def init():
    """Устанавливает начальные пределы осей и возвращает объект линии."""
    ax.set_xlim(0, 10)  # временно, будет автоматически масштабироваться
    ax.set_ylim(0, 2)  # временно, будет автоматически масштабироваться
    return line,

# --- Функция обновления кадра ---
def update(frame):
    global time_run, capacity, cursor, times_per_dot, voltages_per_dot, file_cursor, minutes, hours, ex_run, file_pos, final_time, capacity_wh
    if not file_name or not os.path.exists(file_name):
        return
# Проверяем, есть ли данные в порту (неблокирующее чтение)
    try:
        with open(file_name, "r") as f:
            f.seek(file_pos)
            new_lines = f.readlines()
            file_pos = f.tell()
            for row in new_lines:
                row = row.strip()
                if not row or row.startswith('time'):
                    continue

                parts = row.split(',')
                if len(parts) >= 2:
                    try:
                        time_run = int(float(parts[0])) - 1
                        voltage = float(parts[1])
                        if voltage:
                                status = "Активен" if ex_run else "Завершён"
                                color = 'green' if ex_run else 'red'
                                if voltage <= VOLTAGE_THRESHOLD or not ex_run:
                                    ex_run = False
                                    text_capacity.set_text(f"Напряжение: {voltage:.2f} В\n"                                                    
                                                       f"Ёмкость: {capacity=:.4f} Вт·ч\n"
                                                       f"Статус: {status}\n"
                                                       f'Время: {final_time//3600%60} Часов {final_time//60%60} Минут {final_time%60} Секунд')

                                amperage = voltage / resistans
                                print(f"{voltage=:.3f} В, {amperage=:.4f} А, {time_run=:.0f} сек, {capacity=:.4f} Вт/ч")
                                if ex_run:
                                    capacity += amperage * voltage / 3600 # / 3.6  # Вт/ч
                                    final_time = time_run
                                    text_capacity.set_text(f"Напряжение: {voltage:.2f} В\n"
                                                       f"Ёмкость: {capacity=:.4f} Вт·ч\n"
                                                       f"Статус: {status}\n"
                                                       f'Время: {time_run//3600%60} Часов {time_run//60%60} Минут {time_run%60} Секунд')
                                    text_capacity.set_color(color)
                                # Добавляем данные
                                times_per_dot.append(time_run / (1 + 59 * minutes + 3540 * hours))
                                voltages_per_dot.append(voltage)
                                if time_run >= limit_s and not minutes:
                                    minutes = True
                                    ax.set_xlabel(time_scales[1], fontsize=12)
                                    for i in range(len(times)):
                                        times[i] /= 60
                                    for i in range(len(times_per_dot)):
                                        times_per_dot[i] /= 60

                                if time_run >= limit_m and minutes and not hours:
                                    hours = True
                                    ax.set_xlabel(time_scales[2], fontsize=12)
                                    # 3. Принудительно перерисовываем холст
                                    fig.canvas.draw_idle()
                                    for i in range(len(times)):
                                        times[i] /= 60
                                    for i in range(len(times_per_dot)):
                                        times_per_dot[i] /= 60


                                if (len(times_per_dot) >= value_per_dot):
                                    mean_times = sum(times_per_dot) / value_per_dot
                                    times.append(mean_times)
                                    times_per_dot = []
                                    mean_voltage = sum(voltages_per_dot) / value_per_dot
                                    voltages.append(mean_voltage)
                                    voltages_per_dot = []

                                    # Обновляем линию
                                    line.set_data(times, voltages)

                                    # Автомасштабируем оси по новым данным
                                    ax.relim()
                                    ax.autoscale_view()
                                    ax.set_xlim(0, time_run / (1 + 59 * minutes + 3540 * hours) + 5/ (1 + 59 * minutes + 3540 * hours))  # временно, будет автоматически масштабироваться
                                    cursor += 1
                                    if (cursor >= max_data_size):
                                        compress()
                    except ValueError:
                        continue
    except (ValueError, UnicodeDecodeError) as e:
        print(f"Ошибка чтения: {e}")
    ax.relim()
    ax.autoscale_view()
    # Увеличиваем счетчик времени (каждые 100 мс, примерно 10 Гц)
    #time.sleep(0.5)
    return line,

# --- Запуск анимации ---
def main():
    global file_name
    file_name = select_file()

    if not file_name:
        print("Файл не выбран. Запуск отменён.")
        return

    print(f"Открыт файл: {file_name}")
    ani = animation.FuncAnimation(fig, update, init_func=init,
                                  interval=1000, blit=False, cache_frame_data=False)

    plt.show()

if __name__ == "__main__":
    main()
