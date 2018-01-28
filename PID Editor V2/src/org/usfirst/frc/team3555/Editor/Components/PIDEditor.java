package org.usfirst.frc.team3555.Editor.Components;

import java.io.IOException;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Editor.Handler;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfo;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PIDEditor extends Pane {
	private Handler handler;
	private SquareWaveMonitor squareWaveMonitor;
	
	private DeviceInfo device;
	private Controller controller;
	private int id;
	
	private Label nameLabel;
	
	private TextField setPointField, pField, iField, dField, fField;

	private ChoiceBox<ControlMode> modeChooser;
	private Button enableButton;
	
	private Button applyButton;
	private CheckBox autoApplyCheckBox;
	
	private Button squareWaveButton;
	private TextField frequencyField, setPoint1Field, setPoint2Field;
	
	private TextField distancePerRevField;
	
//	private RestrictionMonitor restrictionMonitor;
//	private TextField upperRestrictionField, lowerRestrictionField;
//	private Button motorRestrictionButton;
	
	private ChoiceBox<FeedbackDevice> feedbackDeviceChooser;
	private TextField codesPerRevField;
	
	private Button resetPosition;
	
	private boolean autoApply;
	private double startX, startY;
	
	@SuppressWarnings("unchecked")
	public PIDEditor(Handler handler, DeviceInfo device) {
		super();
		this.handler = handler;
		this.device = device;
		this.controller = Controller.CANTalon;

		squareWaveMonitor = new SquareWaveMonitor(handler, device);
//		restrictionMonitor = new RestrictionMonitor(handler, device);
		
		handler.getUpdater().add(squareWaveMonitor);
//		handler.getUpdater().add(restrictionMonitor);
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/PID Editor.fxml"));
			getChildren().addAll(root.getChildrenUnmodifiable());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		nameLabel = (Label) (lookup("#NameLabel"));
		resetPosition = (Button) (lookup("#ResetPosition"));
		
		setPointField = (TextField) (lookup("#SetPointField"));
		pField = (TextField) (lookup("#PField"));
		iField = (TextField) (lookup("#IField"));
		dField = (TextField) (lookup("#DField"));
		fField = (TextField) (lookup("#FField"));
		
		modeChooser = (ChoiceBox<ControlMode>) (lookup("#ModeChooser"));
		enableButton = (Button) (lookup("#EnableButton"));
		
		applyButton = (Button) (lookup("#ApplyButton"));
		autoApplyCheckBox = (CheckBox) (lookup("#ApplyCheckBox"));
		
		squareWaveButton = (Button) (lookup("#StartSquareWaveButton"));
		frequencyField = (TextField) (lookup("#FrequencyField"));
		setPoint1Field = (TextField) (lookup("#SetPoint1Field"));
		setPoint2Field = (TextField) (lookup("#SetPoint2Field"));
		
		feedbackDeviceChooser = (ChoiceBox<FeedbackDevice>) (lookup("#FeedBackDeviceChooser"));
		codesPerRevField = (TextField) (lookup("#CodesPerRevField"));
		distancePerRevField = (TextField) (lookup("#DistancePerRevField"));
		
		feedbackDeviceChooser.getItems().addAll(FeedbackDevice.Analog, FeedbackDevice.QuadEncoder, FeedbackDevice.Tachometer);
		feedbackDeviceChooser.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			disableAll();
			sendData(Properties.FeedBackSensor, feedbackDeviceChooser.getItems().get(newValue.intValue()).value);
		});
		
		codesPerRevField.textProperty().addListener((observable, oldValue, newValue) -> {
			sendData(Properties.SensorUnitsPerRotation, (int) Util.getValue(codesPerRevField));
		});
		
		distancePerRevField.textProperty().addListener((observable, oldValue, newValue) -> {
			sendData(Properties.DistancePerRotation, Util.getValue(distancePerRevField));
		});
		
//		upperRestrictionField = (TextField) (lookup("#UpperRestrictField"));
//		lowerRestrictionField = (TextField) (lookup("#LowerRestrictField"));
//		motorRestrictionButton = (Button) (lookup("#MotorRestrictorButton"));
//		
//		motorRestrictionButton.setOnAction(e -> {
//			if(motorRestrictionButton.getText().equals("Enable Restrictor") &&
//					(Util.getValue(upperRestrictionField) != 0 || Util.getValue(lowerRestrictionField) != 0)) {
//				
//				motorRestrictionButton.setText("Disable Restrictor");
//				restrictionMonitor.setMonitoring(true);
//			} else if(motorRestrictionButton.getText().equals("Disable Restrictor")) {
//				motorRestrictionButton.setText("Enable Restrictor");
//				restrictionMonitor.setMonitoring(false);
//			}
//		});
//
//		lowerRestrictionField.textProperty().addListener((observable, oldValue, newValue) -> {
//			restrictionMonitor.setLowerBound(Util.getValue(lowerRestrictionField));
//		});
//		
//		upperRestrictionField.textProperty().addListener((observable, oldValue, newValue) -> {
//			restrictionMonitor.setLowerBound(Util.getValue(upperRestrictionField));
//		});
		
		nameLabel.setText((String) device.getInfo(Properties.Name));
		
		autoApplyCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
			autoApply = newValue;
			applyButton.setDisable(newValue);
        });
		autoApplyCheckBox.setSelected(true);
		
		setPointField.setText(String.valueOf(handler.getDeviceInfoManager().getInfo(controller, Properties.SetPoint, id)));
		pField.setText(String.valueOf(device.getInfo(Properties.P)));
		iField.setText(String.valueOf(device.getInfo(Properties.I)));
		dField.setText(String.valueOf(device.getInfo(Properties.D)));
		fField.setText(String.valueOf(device.getInfo(Properties.F)));
		
		modeChooser.getItems().addAll(ControlMode.PercentOutput, ControlMode.Velocity, ControlMode.Position, ControlMode.Follower, ControlMode.Current);
		modeChooser.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			disableAll();
			sendData(Properties.ControlMode, modeChooser.getItems().get(newValue.intValue()).value);
			sendData(Properties.SetPoint, Util.getValue(setPointField));
		});
		
		modeChooser.getSelectionModel().selectFirst();
		
		resetPosition.setOnAction(e -> {
			sendData(Properties.ResetSensorPosition, true);
		});
		
		enableButton.setOnAction(e -> {
			if(enableButton.getText().equals("Enable")) {
				if((boolean) handler.getDeviceInfoManager().getInfo(Controller.DriverStation, Properties.Enabled, 0)) {
					enableButton.setText("Disable");
					sendData(Properties.Enabled, true);
				}
			} else {
//				enableButton.setText("Enable");
//				handler.getDeviceInfoManager().setBoolean("Enabled", false, id);
//				squareWaveButton.setText("Start Square Wave");
//				squareWaveMonitor.end();
				disableAll();
			}
		});
		
		squareWaveButton.setOnAction(e -> {
			if(squareWaveButton.getText().equals("Start Square Wave") && squareWaveMonitor.start()) {
				sendData(Properties.Enabled, true);
				squareWaveButton.setText("End Square Wave");
				enableButton.setText("Disable");
			} else {
				sendData(Properties.Enabled, false);
				squareWaveButton.setText("Start Square Wave");
				enableButton.setText("Enable");
				squareWaveMonitor.end();
			}
		});
		
		applyButton.setOnAction(e -> {
			if(!squareWaveMonitor.isMonitoring()) {
				sendData(Properties.SetPoint, Util.getValue(setPointField));
			} else {
				squareWaveMonitor.setFrequency(Util.getValue(frequencyField));
				squareWaveMonitor.getSetPoints()[0] = Util.getValue(setPoint1Field);
				squareWaveMonitor.getSetPoints()[1] = Util.getValue(setPoint2Field);
			}
			
			sendData(Properties.P, Util.getValue(pField));
			sendData(Properties.I, Util.getValue(iField));
			sendData(Properties.D, Util.getValue(dField));
			sendData(Properties.F, Util.getValue(fField));
		});
		
		setPointField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				sendData(Properties.SetPoint, Util.getValue(setPointField));
				
				if(squareWaveMonitor.isMonitoring()) {
					squareWaveButton.setText("Start Sqaure Wave");
					squareWaveMonitor.end();
				}
			}
		});
		
		pField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				sendData(Properties.P, Util.getValue(pField));
			}
		});
		
		iField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				sendData(Properties.I, Util.getValue(iField));
			}
		});
		
		dField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				sendData(Properties.D, Util.getValue(dField));
			}
		});
		
		fField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				sendData(Properties.F, Util.getValue(fField));
			}
		});
		
		frequencyField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				squareWaveMonitor.setFrequency(Util.getValue(frequencyField));
			}
		});
		
		setPoint1Field.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				squareWaveMonitor.getSetPoints()[0] = Util.getValue(setPoint1Field);
			}
		});
		
		setPoint2Field.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				squareWaveMonitor.getSetPoints()[1] = Util.getValue(setPoint2Field);
			}
		});
		
		setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
		setStyle("-fx-background-color: white");
		
		setOnMousePressed(e -> {
			if(e.getButton() == MouseButton.SECONDARY) {
				startX = getLayoutX() - e.getSceneX();
				startY = getLayoutY() - e.getSceneY();
				
				toFront();
			}
		});
		
		setOnMouseDragged(e -> {
			if(e.getButton() == MouseButton.SECONDARY) {
        		setLayoutX(startX + e.getSceneX());
        		setLayoutY(startY + e.getSceneY());
        	}
		});
		
		disableAll();
	}
	
	
	
	public void disableAll() {
		disableController();
//		endRestriction();
	}
	
	public void disableController() {
		handler.getDeviceInfoManager().sendData(device, Properties.Enabled, false);
		
		enableButton.setText("Enable");
	}
	
	public void endSquareWaveMonitor() {
		squareWaveButton.setText("Start Square Wave");
		squareWaveMonitor.end();
	}
	
//	public void endRestriction() {
//		motorRestrictionButton.setText("Enable Restrictor");
//		restrictionMonitor.setMonitoring(false);
//	}
	
	public void enable() {
		if(!(boolean) handler.getDeviceInfoManager().getInfo(controller, Properties.Enabled, id)) {
			enableButton.fire();
		}
	}
	
	private void sendData(Properties property, Object value) {
		handler.getDeviceInfoManager().sendData(device, property, value);
	}
}
