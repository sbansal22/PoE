EESchema Schematic File Version 4
LIBS:Project_Schematic-cache
EELAYER 30 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L MCU_Module:Arduino_UNO_R3 A1
U 1 1 5DAFBCC0
P 8750 2400
F 0 "A1" H 8750 3581 50  0000 C CNN
F 1 "Arduino_UNO_R3" H 8750 3490 50  0000 C CNN
F 2 "Module:Arduino_UNO_R3" H 8900 1350 50  0001 L CNN
F 3 "https://www.arduino.cc/en/Main/arduinoBoardUno" H 8550 3450 50  0001 C CNN
	1    8750 2400
	1    0    0    -1  
$EndComp
$Comp
L RF_Bluetooth:MOD-nRF8001 U1
U 1 1 5DB0065E
P 7250 2100
F 0 "U1" H 7250 1319 50  0000 C CNN
F 1 "MOD-nRF8001" H 7250 1410 50  0000 C CNN
F 2 "RF_Module:MOD-nRF8001" H 7300 2150 50  0001 C CNN
F 3 "https://www.olimex.com/Products/Modules/RF/MOD-nRF8001/" H 7300 2100 50  0001 C CNN
	1    7250 2100
	-1   0    0    1   
$EndComp
Wire Wire Line
	7750 1900 8250 1900
Wire Wire Line
	7750 2000 8000 2000
Wire Wire Line
	8000 2000 8000 1800
Wire Wire Line
	8000 1800 8250 1800
Wire Wire Line
	7250 2700 6500 2700
Wire Wire Line
	6500 2700 6500 1100
Wire Wire Line
	6500 1100 8850 1100
Wire Wire Line
	8850 1100 8850 1400
$EndSCHEMATC
