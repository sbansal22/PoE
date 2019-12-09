#include <SoftwareSerial.h>

char junk;
String inputString="";

SoftwareSerial mySerial(6, 7); // RX, TX

void setup() {
  Serial.begin(9600);
  mySerial.begin(38400);
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
}

void loop()
{
  if(mySerial.available()){
  while(mySerial.available())
    {
      char inChar = (char)Serial.read(); //read the input
      inputString += inChar;        //make a string of the characters coming on serial
    }
    Serial.println(inputString);
    while (mySerial.available() > 0)  
    { junk = mySerial.read() ; }      // clear the serial buffer
    if(inputString == "a"){         //in case of 'a' turn the LED on
      digitalWrite(4, HIGH);  
      digitalWrite(5, HIGH); 
    }else if(inputString == "b"){   //incase of 'b' turn the LED off
      digitalWrite(4, LOW);
      digitalWrite(5, LOW); 
    }
    inputString = "";
  }
}
