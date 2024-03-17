#include <Wire.h>

#include <Adafruit_PWMServoDriver.h>

Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver(); // Initialize the PWM driver

char BT_input; // to store input character received via BT.

void setup() {
  Serial.begin(9600); //default baud rate of module

  Wire.begin(); // Start the I2C bus

  pwm.begin(); // Initialize the PWM driver

  pwm.setPWMFreq(50); // Set the frequency of the PWM signals

  pwm.setPWM(0, 0, 310);
  pwm.setPWM(1, 0, 110);
  pwm.setPWM(2, 0, 310);
  pwm.setPWM(3, 0, 110);
  pwm.setPWM(4, 0, 310);
  pwm.setPWM(5, 0, 110);

  while (!Serial) {
    // wait for serial port to connect. Needed for native USB port only
  }
}

void loop()

{
  if (Serial.available()) {
    BT_input = Serial.read(); // read input string from bluetooth 

    switch (BT_input) {
    case 'A':
    case 'a':
      pwm.setPWM(0, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      delay(0);
      break;
    case 'B':
    case 'b':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(2, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(2, 0, 310);
      delay(0);
      break;
    case 'C':
    case 'c':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(1, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(1, 0, 110);
      delay(0);
      break;
    case 'D':
    case 'd':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(3, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(3, 0, 110);
      delay(0);
      break;
    case 'E':
    case 'e':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(3, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(3, 0, 110);
      delay(0);
      break;
    case 'F':
    case 'f':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(2, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(2, 0, 310);
      delay(0);
      break;
    case 'G':
    case 'g':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(3, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(3, 0, 110);
      delay(0);
      break;
    case 'H':
    case 'h':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(3, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(3, 0, 110);
      delay(0);
      break;
    case 'I':
    case 'i':
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(2, 0, 110);
      delay(1000);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(2, 0, 310);
      delay(0);
      break;
    case 'J':
    case 'j':
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(3, 0, 310);
      delay(1000);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(3, 0, 110);
      delay(0);
      break;
    case 'K':
    case 'k':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case 'L':
    case 'l':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case 'M':
    case 'm':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case 'N':
    case 'n':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(3, 0, 310);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(3, 0, 110);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case 'O':
    case 'o':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(3, 0, 310);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(3, 0, 110);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case 'P':
    case 'p':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case 'Q':
    case 'q':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(3, 0, 310);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(3, 0, 110);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case 'R':
    case 'r':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(3, 0, 310);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(3, 0, 110);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case 'S':
    case 's':
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case 'T':
    case 't':
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(3, 0, 310);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(3, 0, 110);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case 'U':
    case 'u':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(4, 0, 110);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(4, 0, 310);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case 'V':
    case 'v':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(4, 0, 110);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(4, 0, 310);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case 'W':
    case 'w':
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(3, 0, 310);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(3, 0, 110);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case 'X':
    case 'x':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(4, 0, 110);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(4, 0, 310);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case 'Y':
    case 'y':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(3, 0, 310);
      pwm.setPWM(4, 0, 110);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(3, 0, 110);
      pwm.setPWM(4, 0, 310);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case 'Z':
    case 'z':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(3, 0, 310);
      pwm.setPWM(4, 0, 110);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(3, 0, 110);
      pwm.setPWM(4, 0, 310);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case '0':
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(3, 0, 310);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(3, 0, 110);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case '1':
      pwm.setPWM(2, 0, 110);
      delay(1000);
      pwm.setPWM(2, 0, 310);
      delay(0);
      break;
    case '2':
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(4, 0, 110);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(4, 0, 310);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case '3':
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(3, 0, 310);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(3, 0, 110);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case '4':
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(3, 0, 310);
      delay(1000);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(3, 0, 110);
      delay(0);
      break;
    case '5':
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case '6':
      pwm.setPWM(4, 0, 110);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(4, 0, 310);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case '7':
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case '8':
      pwm.setPWM(1, 0, 310);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(1, 0, 110);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case '9':
      pwm.setPWM(0, 0, 110);
      pwm.setPWM(2, 0, 110);
      pwm.setPWM(5, 0, 310);
      delay(1000);
      pwm.setPWM(0, 0, 310);
      pwm.setPWM(2, 0, 310);
      pwm.setPWM(5, 0, 110);
      delay(0);
      break;
    case '.':
      pwm.setPWM(4, 0, 110);
      delay(1000);
      pwm.setPWM(4, 0, 310);
      delay(0);
      break;
    case ' ':
      delay(1000);
      break;
    }

  }

}
