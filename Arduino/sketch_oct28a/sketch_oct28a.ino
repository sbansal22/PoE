const int mosfet = 6;

void setup() {
pinMode(mosfet, OUTPUT);
}

void loop() {
analogWrite(6, 153);
}
