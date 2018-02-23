package org.usfirst.frc.team3555.Editor.Components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Editor.Display;
import org.usfirst.frc.team3555.robot.CANTalon;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class FileIO {
	public static void readDisplay(Display display) throws FileNotFoundException {//E 5 126 84
		File file = getDisplayFile();
		
		if(!file.exists())
			return;
		
		Scanner sc = new Scanner(file);
		
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] parts = line.split(" ");
			
			if(parts.length != 4)
				continue;
			
			int id = Integer.parseInt(parts[1]);
			double x = Double.parseDouble(parts[2]);
			double y = Double.parseDouble(parts[3]);
			String type = parts[0];
			
			if(type.equals("G")) 
				display.addGrapher(id, x, y);
			else if(type.equals("E"))
				display.addEditor(id, x, y);
		}
		
		sc.close();
	}
	
	public static void exportDisplay(Display display) throws IOException {
		File file = getDisplayFile();
		
		if(!file.exists())
			file.createNewFile();
		
		PrintWriter writer = new PrintWriter(file);

		for(Node n : display.getChilderen()) {
			if(n instanceof PIDEditor) 
				writer.println("E" + " " + ((PIDEditor) n).getControllerId() + " " + n.getLayoutX() + " " + n.getLayoutY());
			else if(n instanceof LineChart) 
				writer.println("G" + " " + ((PIDEditor) n).getControllerId() + " " + n.getLayoutX() + " " + n.getLayoutY());
		}
		
		writer.close();
	}
	
	public static void readInformation(PIDEditor editor) {
		TextField setPointField = editor.getSetPointField();
		TextField pField = editor.getpField();
		TextField iField = editor.getiField();
		TextField dField = editor.getdField();
		TextField fField = editor.getfField();
		
		ChoiceBox<FeedbackDevice> feedbackChooser = editor.getFeedbackDeviceChooser();
		ChoiceBox<ControlMode> controlMode = editor.getModeChooser();
		
		TextField codesPerRevField = editor.getCodesPerRevField();
		TextField distancePerRev = editor.getDistancePerRevField();
		
		File file = getFile(editor);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			return;
		}
		
		setPointField.setText(getValue(sc.nextLine()));
		pField.setText(getValue(sc.nextLine()));
		iField.setText(getValue(sc.nextLine()));
		dField.setText(getValue(sc.nextLine()));
		fField.setText(getValue(sc.nextLine()));
		
//		controlMode.getItems().get(controlMode.getSelectionModel().getSelectedIndex()).value);
		feedbackChooser.getSelectionModel().select(CANTalon.getFeedbackDevice(Integer.parseInt(getValue(sc.nextLine()))));
		controlMode.getSelectionModel().select(CANTalon.getControlMode(Integer.parseInt(getValue(sc.nextLine()))));
		
		codesPerRevField.setText(getValue(sc.nextLine()));
		distancePerRev.setText(getValue(sc.nextLine()));
		
		sc.close();
	}
	
	public static void exportInformation(PIDEditor editor) {
		TextField setPointField = editor.getSetPointField();
		TextField pField = editor.getpField();
		TextField iField = editor.getiField();
		TextField dField = editor.getdField();
		TextField fField = editor.getfField();
		
		ChoiceBox<FeedbackDevice> feedbackChooser = editor.getFeedbackDeviceChooser();
		ChoiceBox<ControlMode> controlMode = editor.getModeChooser();
		
		TextField codesPerRevField = editor.getCodesPerRevField();
		TextField distancePerRev = editor.getDistancePerRevField();
		
		File file = getFile(editor);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			System.err.println("Cannot Find File: " + editor.getId());
		}
		
		writer.println(Properties.SetPoint + " " + setPointField.getText());
		writer.println(Properties.P + " " + pField.getText());
		writer.println(Properties.I + " " + iField.getText());
		writer.println(Properties.D + " " + dField.getText());
		writer.println(Properties.F + " " + fField.getText());
		
		writer.println(Properties.FeedBackSensor + " " + feedbackChooser.getItems().get(feedbackChooser.getSelectionModel().getSelectedIndex()).value);
		writer.println(Properties.ControlMode + " " + controlMode.getItems().get(controlMode.getSelectionModel().getSelectedIndex()).value);
		
		writer.println(Properties.SensorUnitsPerRotation + " " + Util.getValue(codesPerRevField));
		writer.println(Properties.DistancePerRotation + " " + Util.getValue(distancePerRev));
		
		writer.close();
	}
	
	private static File getFile(PIDEditor editor) {
		return new File("res/Information/TalonSRX/" + editor.getControllerId()); 
	}
	
	private static File getDisplayFile() {
		return new File("res/Information/Display.info");
	}
	
	private static String getValue(String string) {
		String[] array = string.split(" ");
		return array[array.length - 1];
	}
}