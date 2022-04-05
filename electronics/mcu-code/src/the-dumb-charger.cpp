// the-dumb-charger
//
// Copyright (C) 2022 Aleksandar Radivojevic
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

#include <Arduino.h>

// pin where the LDR voltage divider is located at
#if !defined(LDR_PIN) || !defined(PWR_SW_PIN)
    #error LDR_PIN and PWR_SW_PIN must be defined
#endif

// change in value (raw adc not voltage, so max is 1024) that will trigger it
#define THRESHOLD 350

// delay between checking the light level for start of sequence
#define POLLING_RATE 15

// delay between each bit of the sequence, this has to be larger as most APIs
// seem to be very slow on camera flash toggling
#define SEQUENCE_DELAY 100

// uncomment to make sw on by default
// #define PWR_SW_ON_BY_DEFAULT

int adc = 0;
int last_adc = 0;

void setup() {
    Serial.begin(9600);
    pinMode(LDR_PIN, INPUT);
    pinMode(PWR_SW_PIN, OUTPUT);

    #ifdef PWR_SW_ON_BY_DEFAULT
    digitalWrite(PWR_SW_PIN, HIGH);
    #endif

    delay(50);
}

// checks if light level has changed by more than THRESHOLD
bool changed() {
    adc = analogRead(LDR_PIN);
    return abs(last_adc - adc) > THRESHOLD;
}

// wait for a sharp spikes (both positive and negative) in light then if the
// sequence of spikes matches then toggle the power using PWR_SW_PIN
void loop() {
    last_adc = analogRead(LDR_PIN);
    delay(15); // this may be too fast?

    // wait for first trigger
    if (changed()) {
        last_adc = adc;
        delay(POLLING_RATE);

        // keep same level for 100 ms
        if (changed())
            return;

        last_adc = adc;
        delay(POLLING_RATE);

        // then trigger again
        if (!changed())
            return;

        // toggle pwr
        digitalWrite(PWR_SW_PIN, !digitalRead(PWR_SW_PIN));

        // sleep for 500ms
        delay(500);
    }
}
