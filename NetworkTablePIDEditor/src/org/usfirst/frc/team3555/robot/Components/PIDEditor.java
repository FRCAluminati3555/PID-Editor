package org.usfirst.frc.team3555.robot.Components;

import java.io.IOException;

import org.usfirst.frc.team3555.robot.Handler;
import org.usfirst.frc.team3555.robot.Util;
import org.usfirst.frc.team3555.robot.Data.DeviceInfo;

import com.ctre.CANTalon;
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
	
	private int id;
	
	private Label nameLabel;
	
	private TextField setPointField, pField, iField, dField, fField;

	private ChoiceBox<ControlMode> modeChooser;
	private Button enableButton;
	
	private Button applyButton;
	private CheckBox autoApplyCheckBox;
	
	private Button squareWaveButton;
	private TextField frequencyField, setPoint1Field, setPoint2Field;
	
	private RestrictionMonitor restrictionMonitor;
	private TextField upperRestrictionField, lowerRestrictionField;
	private Button motorRestrictionButton;
	
	private ChoiceBox<String> feedbackSetPointChooser;
	private CheckBox feedbackSetPointCheckBox;
	private ChoiceBox<FeedbackDevice> feedbackDeviceChooser;
	private TextField codesPerRevField;
	
	private Button resetPosition;
	
	private SetPointFeedbackMonitor setPointFeedbackMonitor;
	
	private boolean autoApply;
	private double startX, startY;
	
	@SuppressWarnings("unchecked")
	public PIDEditor(Handler handler, int id) {
		super();
		this.handler = handler;
		this.id = id;

		squareWaveMonitor = new SquareWaveMonitor(handler, this, id);
		restrictionMonitor = new RestrictionMonitor(handler, this, id);
		setPointFeedbackMonitor = new SetPointFeedbackMonitor(handler, this, id);
		
		handler.getUpdater().add(squareWaveMonitor);
		handler.getUpdater().add(restrictionMonitor);
		handler.getUpdater().add(setPointFeedbackMonitor);
		
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
		
		//TODO New Stuff
		feedbackSetPointChooser = (ChoiceBox<String>) (lookup("#SetPointFeedbackChooser"));
		feedbackSetPointCheckBox = (CheckBox) (lookup("#SetPointFeedbackCheckBox"));
		feedbackDeviceChooser = (ChoiceBox<FeedbackDevice>) (lookup("#FeedBackDeviceChooser"));
		codesPerRevField = (TextField) (lookup("#CodesPerRevField"));

		feedbackSetPointChooser.getItems().addAll("AnalogInPosition");
		feedbackSetPointChooser.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
//			disable();
			setPointFeedbackMonitor.setFeedbackProperty(feedbackSetPointChooser.getItems().get(newValue.intValue()));
		});
		
		feedbackSetPointCheckBox.setOnAction(e -> {
			if(squareWaveMonitor.isMonitoring())
				endSquareWaveMonitor();
			
			setPointFeedbackMonitor.setFeedbackToSetPoint(feedbackSetPointCheckBox.isSelected());
			if(!setPointFeedbackMonitor.isFeedbackToSetPoint())
				handler.getDeviceInfoManager().setDouble("SetPoint", Util.getValue(setPointField), id);
		});
		
		feedbackDeviceChooser.getItems().addAll(FeedbackDevice.None, FeedbackDevice.Analog, FeedbackDevice.QuadEncoder, FeedbackDevice.Tachometer);
		feedbackDeviceChooser.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			disableAll();
			handler.getDeviceInfoManager().setFeedbackDevice(feedbackDeviceChooser.getItems().get(newValue.intValue()), id);
		});
		
		codesPerRevField.textProperty().addListener((observable, oldValue, newValue) -> {
			handler.getDeviceInfoManager().setInteger("CodesPerRev", (int) Util.getValue(codesPerRevField), id);
		});
		
		upperRestrictionField = (TextField) (lookup("#UpperRestrictField"));
		lowerRestrictionField = (TextField) (lookup("#LowerRestrictField"));
		motorRestrictionButton = (Button) (lookup("#MotorRestrictorButton"));
		
		motorRestrictionButton.setOnAction(e -> {
			if(motorRestrictionButton.getText().equals("Enable Restrictor") &&
					(Util.getValue(upperRestrictionField) != 0 || Util.getValue(lowerRestrictionField) != 0)) {
				
				motorRestrictionButton.setText("Disable Restrictor");
				restrictionMonitor.setMonitoring(true);
			} else if(motorRestrictionButton.getText().equals("Disable Restrictor")) {
				motorRestrictionButton.setText("Enable Restrictor");
				restrictionMonitor.setMonitoring(false);
			}
		});

		lowerRestrictionField.textProperty().addListener((observable, oldValue, newValue) -> {
			restrictionMonitor.getBounds()[0] = Util.getValue(lowerRestrictionField);
		});
		
		upperRestrictionField.textProperty().addListener((observable, oldValue, newValue) -> {
			restrictionMonitor.getBounds()[1] = Util.getValue(upperRestrictionField); 
		});
		
		nameLabel.setText(handler.getDeviceInfoManager().getDevices().get(id).getName());
		
		autoApplyCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
			autoApply = newValue;
			applyButton.setDisable(newValue);
        });
		autoApplyCheckBox.setSelected(true);
		
		setPointField.setText(String.valueOf(handler.getDeviceInfoManager().getDouble("SetPoint", id)));
		pField.setText(String.valueOf(handler.getDeviceInfoManager().getDouble("P", id)));
		iField.setText(String.valueOf(handler.getDeviceInfoManager().getDouble("I", id)));
		dField.setText(String.valueOf(handler.getDeviceInfoManager().getDouble("D", id)));
		fField.setText(String.valueOf(handler.getDeviceInfoManager().getDouble("F", id)));
		
		modeChooser.getItems().addAll(ControlMode.PercentOutput, ControlMode.Velocity, ControlMode.Position, ControlMode.Follower, ControlMode.Current);
		modeChooser.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			disableAll();
			handler.getDeviceInfoManager().setControlMode(modeChooser.getItems().get(newValue.intValue()), id);
		});
		
		modeChooser.getSelectionModel().selectFirst();
		
		resetPosition.setOnAction(e -> {
			handler.getDeviceInfoManager().setBoolean("ResetPosition", true, id);
		});
		
		enableButton.setOnAction(e -> {
			if(enableButton.getText().equals("Enable")) {
				if(DeviceInfo.RobotEnabled) {
					enableButton.setText("Disable");
					handler.getDeviceInfoManager().setBoolean("Enabled", true, id);
//					handler.getDeviceInfoManager().setDouble("SetPoint", Util.getValue(setPointField), id);
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
				if(setPointFeedbackMonitor.isFeedbackToSetPoint()) {
					setPointFeedbackMonitor.setFeedbackToSetPoint(false);
					feedbackSetPointCheckBox.setSelected(false);
				}
				
				handler.getDeviceInfoManager().setBoolean("Enabled", true, id);
				squareWaveButton.setText("End Square Wave");
				enableButton.setText("Disable");
			} else {
				handler.getDeviceInfoManager().setBoolean("Enabled", false, id);
				squareWaveButton.setText("Start Square Wave");
				enableButton.setText("Enable");
				squareWaveMonitor.end();
			}
		});
		
		applyButton.setOnAction(e -> {
			setPointFeedbackMonitor.setFeedbackToSetPoint(false);
			feedbackSetPointCheckBox.setSelected(false);
			
			if(!squareWaveMonitor.isMonitoring()) {
				handler.getDeviceInfoManager().setDouble("SetPoint", Util.getValue(setPointField), id);
			} else {
				squareWaveMonitor.setFrequency(Util.getValue(frequencyField));
				squareWaveMonitor.getSetPoints()[0] = Util.getValue(setPoint1Field);
				squareWaveMonitor.getSetPoints()[1] = Util.getValue(setPoint2Field);
			}
			
			handler.getDeviceInfoManager().setDouble("P", Util.getValue(pField), id);
			handler.getDeviceInfoManager().setDouble("I", Util.getValue(iField), id);
			handler.getDeviceInfoManager().setDouble("D", Util.getValue(dField), id);
			handler.getDeviceInfoManager().setDouble("F", Util.getValue(fField), id);
		});
		
		setPointField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				setPointFeedbackMonitor.setFeedbackToSetPoint(false);
				feedbackSetPointCheckBox.setSelected(false);
				
				handler.getDeviceInfoManager().setDouble("SetPoint", Util.getValue(setPointField), id);
				
				if(squareWaveMonitor.isMonitoring()) {
					squareWaveButton.setText("Start Sqaure Wave");
					squareWaveMonitor.end();
				}
			}
		});
		
		pField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				handler.getDeviceInfoManager().setDouble("P", Util.getValue(pField), id);
			}
		});
		
		iField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				handler.getDeviceInfoManager().setDouble("I", Util.getValue(iField), id);
			}
		});
		
		dField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				handler.getDeviceInfoManager().setDouble("D", Util.getValue(dField), id);
			}
		});
		
		fField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(autoApply) {
				handler.getDeviceInfoManager().setDouble("F", Util.getValue(fField), id);
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
		disableController();
		endRestriction();
	}
	
	public void disableController() {
		handler.getDeviceInfoManager().setBoolean("Enabled", false, id);
		enableButton.setText("Enable");
	}
	
	public void endSquareWaveMonitor() {
		squareWaveButton.setText("Start Square Wave");
		squareWaveMonitor.end();
	}
	
	public void endRestriction() {
		motorRestrictionButton.setText("Enable Restrictor");
		restrictionMonitor.setMonitoring(false);
	}
	
	public void enable() {
		if(!handler.getDeviceInfoManager().getBoolean("Enabled", id)) {
			enableButton.fire();
		}
	}
}
