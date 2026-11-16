#include <avr/io.h>
#include <avr/interrupt.h>

int base = 10;
const int buffer_size = 100;
uint8_t buffer_data[buffer_size] = {};
int first_cursor = 0;
int last_cursor = 0;
int overfull = 0;

// Индикация
const int segPins[8] = {37, 32, 36, 33, 34, 30, 35, 31};
const int gndPins[4] = {22, 23, 24, 25};
const int anPins[2] = {A0, A1};
const byte digits[12] = {
  0b11111010, 0b00100010, 0b10111001, 0b10101011,
  0b01100011, 0b11001011, 0b11011011, 0b10100010,
  0b11111011, 0b11101011, 0b00000100, 0b01111010
};

volatile int voltage = 0; 
volatile int order = 0;
volatile int currentDigit[4] = {0, 0, 0, 0};

void print_digit(int number, int pos) {
  byte digit = digits[number];
  for (int i = 0; i < 8; i++) {
    int bit = (digit >> (7 - i)) & 1;
    if (pos == 0 && i == 5) bit = 1;
    digitalWrite(segPins[i], bit);
  }  
  digitalWrite(gndPins[pos], HIGH);
}

void change_state(int number) {
  currentDigit[3] = 11; 
  currentDigit[2] = number / base % base;
  currentDigit[1] = number / (base * base) % base;
  currentDigit[0] = number / (base * base * base) % base;
}

void setup() {
  Serial.begin(9600);
  // Не ставим setTimeout, будем читать байты напрямую
  analogReference(DEFAULT);
  pinMode(anPins[0], INPUT);
  for (int i = 0; i < 8; i++) {
    pinMode(segPins[i], OUTPUT);
    digitalWrite(segPins[i], LOW);
  }
  for (int i = 0; i < 4; i++) {
    pinMode(gndPins[i], OUTPUT);
    digitalWrite(gndPins[i], LOW);
  }
  
  noInterrupts();
  // Timer1 (Секундомер / Замер)
  TCCR1A = 0;
  TCCR1B = 0;
  TCCR1B |= (1 << CS10) | (1 << CS12); // Prescaler 1024
  TCCR1B |= (1 << WGM12); // CTC mode
  OCR1A = 15624; // ~1 секунда при 16MHz
  TIMSK1 |= (1 << OCIE1A);
  
  // Timer2 (Индикация)
  TCCR2A = 0;
  TCCR2B = 0;
  TCCR2A |= (1 << WGM21);
  TCCR2B |= (1 << CS21);
  OCR2A = 199;
  TIMSK2 |= (1 << OCIE2A);
  interrupts();
}

ISR(TIMER1_COMPA_vect) {
  voltage = ((uint32_t)abs(analogRead(anPins[0])) * 4410 >> 10);
  change_state(voltage);
  
  // Сохраняем сырое значение (0-255) в буфер
  uint8_t output = (analogRead(anPins[0]) >> 2); // 10 бит в 8 бит
  
  if (!overfull || first_cursor != last_cursor) {
    buffer_data[last_cursor] = output;
    last_cursor += 1;
    if (last_cursor >= buffer_size) {
      last_cursor = 0;
      overfull = 1;
    } 
  } 
}

ISR(TIMER2_COMPA_vect) {
  digitalWrite(gndPins[order], LOW);
  order = (order + 1) % 4;
  print_digit(currentDigit[order], order);
}

void loop() {
  // 1. Сначала проверяем, не пришло ли подтверждение от ПК
  // Это нужно делать независимо от флагов, иначе мы не узнаем, что данные приняты
  if (Serial.available() > 0) {
    byte command = Serial.read();
    
    if (command == '1') { // Подтверждение (0x31)
      // Двигаем курсор чтения, только если есть данные
      if (overfull || first_cursor != last_cursor) {
        first_cursor += 1;
        if (first_cursor >= buffer_size) {
          first_cursor = 0;
          overfull = 0;
        }
      }
    } 
    // Если пришло '0', просто игнорируем (ПК просит повтор, но мы все равно отправим текущее first_cursor)
  }

  // 2. Отправляем данные, если есть что отправлять
  // Отправляем текущее значение, на которое указывает first_cursor
  if (overfull || first_cursor < last_cursor) {
    uint8_t val = buffer_data[first_cursor];
    // Отправляем два одинаковых байта для проверки целостности на ПК
    uint8_t packet[2] = {val, val};
    Serial.write(packet, 2);
    
    // Небольшая задержка, чтобы не забивать порт слишком быстро, 
    // даем время ПК на ответ
    delay(10); 
  } else {
    // Если буфер пуст, тоже можно отправить что-то, чтобы ПК не ждал вечно,
    // но лучше просто ждать прерывания таймера
    delay(10);
  }
}