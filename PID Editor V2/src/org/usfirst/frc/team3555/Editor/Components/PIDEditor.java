package org.usfirst.frc.team3555.Editor.Components;

import java.io.IOException;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Editor.Handler;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfo;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;

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

	private CheckBox additiveCheckBox;
	private Button squareWaveButton;
	private TextField frequencyField, setPoint1Field, setPoint2Field;
	
	private TextField distancePerRevField;
	
	private Button limitSwitchButton;
	
	private ChoiceBox<LimitSwitchSource> forwardLimitSwitchSource, reverseLimitSwitchSource; 
	private ChoiceBox<LimitSwitchNormal> forwardLimitSwitchNormal, reverseLimitSwitchNormal;
	
	private Button softLimitButton;
	private TextField softLimitForward, softLimitReverse;
	
	private Button exportButton;
	
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
		this.id = device.getId();
		
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
		
		additiveCheckBox = (CheckBox) (lookup("#AdditiveCheckBox"));
		squareWaveButton = (Button) (lookup("#StartSquareWaveButton"));
		frequencyField = (TextField) (lookup("#FrequencyField"));
		setPoint1Field = (TextField) (lookup("#SetPoint1Field"));
		setPoint2Field = (TextField) (lookup("#SetPoint2Field"));
		
		feedbackDeviceChooser = (ChoiceBox<FeedbackDevice>) (lookup("#FeedBackDeviceChooser"));
		codesPerRevField = (TextField) (lookup("#CodesPerRevField"));
		distancePerRevField = (TextField) (lookup("#DistancePerRevField"));
		
		limitSwitchButton = (Button) (lookup("#limitSwitchEnable"));
		
		forwardLimitSwitchSource = (ChoiceBox<LimitSwitchSource>) (lookup("#ForwardLimitSwitchSourceChooser"));
		reverseLimitSwitchSource = (ChoiceBox<LimitSwitchSource>) (lookup("#ReverseLimitSwitchSourceChooser"));
		
		forwardLimitSwitchNormal = (ChoiceBox<LimitSwitchNormal>) (lookup("#ForwardLimitSwitchNormalChooser"));
		reverseLimitSwitchNormal = (ChoiceBox<LimitSwitchNormal>) (lookup("#ReverseLimitSwitchNormalChooser"));
		
		softLimitButton = (Button) (lookup("#SoftLimitEnable"));
		softLimitForward = (TextField) (lookup("#ForwardLimitField"));
		softLimitReverse = (TextField) (lookup("#ReverseLimitField"));
		
		exportButton = (Button) (lookup ("#Export"));
		exportButton.setOnAction(e -> {
			FileIO.exportInformation(this);
		});
		
		feedbackDeviceChooser.getItems().addAll(FeedbackDevice.None, FeedbackDevice.Analog, FeedbackDevice.QuadEncoder, FeedbackDevice.Tachometer);
		feedbackDeviceChooser.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			disableAll();
			sendData(Properties.FeedBackSensor, feedbackDeviceChooser.getItems().get(newValue.intValue()).value);
		});
		feedbackDeviceChooser.getSelectionModel().select(0);
		
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
		
		setPointField.setText(String.valueOf(device.getInfo(Properties.SetPoint)));
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
		
		additiveCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
			squareWaveMonitor.setAdditive(newValue);
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
		
		//Limit Switch
		limitSwitchButton.setOnAction(e -> {
			if(limitSwitchButton.getText().equals("Enable Limit Switch")) {
				sendData(Properties.EnableLimitSwitch, true);
				limitSwitchButton.setText("Disable Limit Switch");
			} else {
				sendData(Properties.EnableLimitSwitch, false);
				limitSwitchButton.setText("Enable Limit Switch");
			}
		});
		
		forwardLimitSwitchNormal.getItems().addAll(LimitSwitchNormal.NormallyOpen, LimitSwitchNormal.NormallyClosed);
		forwardLimitSwitchNormal.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			sendData(Properties.ForwardLimitSwitchNormal, forwardLimitSwitchNormal.getItems().get(newValue.intValue()).value);
		});
		
		reverseLimitSwitchNormal.getItems().addAll(LimitSwitchNormal.NormallyOpen, LimitSwitchNormal.NormallyClosed);
		reverseLimitSwitchNormal.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			sendData(Properties.ReverseLimitSwitchNormal, reverseLimitSwitchNormal.getItems().get(newValue.intValue()).value);
		});
		
		
		forwardLimitSwitchSource.getItems().addAll(LimitSwitchSource.FeedbackConnector, LimitSwitchSource.RemoteCANifier, LimitSwitchSource.RemoteTalonSRX);
		forwardLimitSwitchSource.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			sendData(Properties.ForwardLimitSwtichSource, forwardLimitSwitchSource.getItems().get(newValue.intValue()).value);
		});
		
		reverseLimitSwitchSource.getItems().addAll(LimitSwitchSource.FeedbackConnector, LimitSwitchSource.RemoteCANifier, LimitSwitchSource.RemoteTalonSRX);
		reverseLimitSwitchSource.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			sendData(Properties.ReverseLimitSwtichSource, reverseLimitSwitchSource.getItems().get(newValue.intValue()).value);
		});
		
		//Soft Limit
		softLimitButton.setOnAction(e -> {
			if(softLimitButton.getText().equals("Enable Soft Limit")) {
				sendData(Properties.EnableSoftLimit, true);
				softLimitButton.setText("Disable Soft Limit");
			} else {
				sendData(Properties.EnableSoftLimit, false);
				softLimitButton.setText("Enable Soft Limit");
			}
		});
		
		softLimitForward.textProperty().addListener((observable, oldValue, newValue) -> {
			sendData(Properties.SoftLimitForward, Util.getValue(softLimitForward));
		});
		
		softLimitReverse.textProperty().addListener((observable, oldValue, newValue) -> {
			sendData(Properties.SoftLimitReverse, Util.getValue(softLimitReverse));
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

	//Getters
	public int getControllerId() {
		return device.getId();
	}
	
	public SquareWaveMonitor getSquareWaveMonitor() { return squareWaveMonitor; }

	public Label getNameLabel() { return nameLabel; }
	
	public TextField getSetPointField() { return setPointField; }
	public TextField getpField() { return pField; }
	public TextField getiField() { return iField; }   
	public TextField getdField() { return dField; }
	public TextField getfField() { return fField; }
	
	public ChoiceBox<ControlMode> getModeChooser() { return modeChooser; }
	public Button getEnableButton() { return enableButton; }
	public Button getApplyButton() { return applyButton; }
	
	public CheckBox getAutoApplyCheckBox() { return autoApplyCheckBox; }
	public CheckBox getAdditiveCheckBox() { return additiveCheckBox; }
	
	public Button getSquareWaveButton() { return squareWaveButton; } 
	public TextField getFrequencyField() { return frequencyField; }
	public TextField getSetPoint1Field() { return setPoint1Field; }
	public TextField getSetPoint2Field() { return setPoint2Field; }
	
	public TextField getDistancePerRevField() { return distancePerRevField; }
	public Button getLimitSwitchButton() { return limitSwitchButton; }
	public ChoiceBox<LimitSwitchSource> getForwardLimitSwitchSource() { return forwardLimitSwitchSource; }
	public ChoiceBox<LimitSwitchSource> getReverseLimitSwitchSource() { return reverseLimitSwitchSource; }
	public ChoiceBox<LimitSwitchNormal> getForwardLimitSwitchNormal() { return forwardLimitSwitchNormal; }
	public ChoiceBox<LimitSwitchNormal> getReverseLimitSwitchNormal() { return reverseLimitSwitchNormal; }
	public Button getSoftLimitButton() { return softLimitButton; }
	public TextField getSoftLimitForward() { return softLimitForward; }
	public TextField getSoftLimitReverse() { return softLimitReverse; }
	
	public ChoiceBox<FeedbackDevice> getFeedbackDeviceChooser() { return feedbackDeviceChooser; }
	public TextField getCodesPerRevField() { return codesPerRevField; }
	public Button getResetPosition() { return resetPosition; }
}
