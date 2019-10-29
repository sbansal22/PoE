const int mosfet = 6;
const int tran = 5;

void setup() {
pinMode(mosfet, OUTPUT);
pinMode(tran, OUTPUT);
}

void loop() {
  if(btSignal == True) {
    analogWrite(6, 153);
    analogWrite(5, 153);
    }

  else {
    analogWrite(6,0);
    analogWrite(5,0);
    }
    
  

}
