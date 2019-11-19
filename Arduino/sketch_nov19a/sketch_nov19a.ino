void setup() {
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
}

void loop()
{ 
  delay(1000);
  analogWrite(4, 253);  
  analogWrite(5, 253); 
  delay(1000);
  analogWrite(4, 0);
  analogWrite(5, 0); 
}
