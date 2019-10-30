const int mosfet = 6;
const int tran = 5;
char junk;
String inputString="";


void setup() {
  Serial.begin(9600); 
  pinMode(mosfet, OUTPUT);
  pinMode(tran, OUTPUT);
  pinMode(13, OUTPUT);
}



/*
Arduino Turn LED On/Off using Serial Commands
Created April 22, 2015
Hammad Tariq, Incubator (Pakistan)

It's a simple sketch which waits for a character on serial
and in case of a desirable character, it turns an LED on/off.

Possible string values:
a (to turn the LED on)
b (tor turn the LED off)
*/


void loop()
{
  if(Serial.available()){
  while(Serial.available())
    {
      char inChar = (char)Serial.read(); //read the input
      inputString += inChar;        //make a string of the characters coming on serial
    }
    Serial.println(inputString);
    while (Serial.available() > 0)  
    { junk = Serial.read() ; }      // clear the serial buffer
    if(inputString == "a"){         //in case of 'a' turn the LED on
      analogWrite(6, 153);
      analogWrite(5, 153);  
    }
    else if(inputString == "b"){   //incase of 'b' turn the LED off
      analogWrite(6,0);
      analogWrite(5,0);
    }
    inputString = "";
  }
}
