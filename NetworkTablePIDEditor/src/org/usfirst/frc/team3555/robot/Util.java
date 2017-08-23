package org.usfirst.frc.team3555.robot;

import javafx.scene.control.TextField;

public class Util {
	public static double getValue(TextField from) {
		double temp;
		
		try {
			temp = Double.valueOf(from.getText());
		} catch(NumberFormatException ex) {
			temp = 0;
		}
		
		return temp;
	}
}
