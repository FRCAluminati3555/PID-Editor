package org.usfirst.frc.team3555.robot.Components;

import java.io.IOException;

import org.usfirst.frc.team3555.robot.Handler;
import org.usfirst.frc.team3555.robot.Util;
import org.usfirst.frc.team3555.robot.Data.DeviceInfo;

import com.ctre.CANTalon;

import javafx.beans.value.ChangeListener;
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

	private ChoiceBox<CANTalon.TalonControlMode> modeChooser;
	private Button enableButton;
	
	private Button applyButton;
	private CheckBox autoApplyCheckBox;
	
	private Button squareWaveButton;
	private TextField frequencyField, setPoint1Field, setPoint2Field;
	
	private boolean autoApply;
	private double startX, startY;
	
	@SuppressWarnings("unchecked")
	public PIDEditor(Handler handler, int id) {
		super();
		this.handler = handler;
		this.id = id;

		squareWaveMonitor = new SquareWaveMonitor(handler, id);
		handler.getUpdater().add(squareWaveMonitor);
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/PID Editor.fxml"));
			getChildren().addAll(root.getChildrenUnmodifiable());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		nameLabel = (Label) (lookup("#NameLabel"));
		
		setPointField = (TextField) (lookup("#SetPointField"));
		pField = (TextField) (lookup("#PField"));
		iField = (TextField) (lookup("#IField"));
		dField = (TextField) (lookup("#DField"));
		fField = (TextField) (lookup("#FField"));
		
		modeChooser = (ChoiceBox<CANTalon.TalonControlMode>) (lookup("#ModeChooser"));
		enableButton = (Button) (lookup("#EnableButton"));
		
		applyButton = (Button) (lookup("#ApplyButton"));
		autoApplyCheckBox = (CheckBox) (lookup("#ApplyCheckBox"));
		
		squareWaveButton = (Button) (lookup("#StartSquareWaveButton"));
		frequencyField = (TextField) (lookup("#FrequencyField"));
		setPoint1Field = (TextField) (lookup("#SetPoint1Field"));
		setPoint2Field = (TextField) (lookup("#SetPoint2Field"));
		
		nameLabel.setText(String.valueOf(id));
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
		
		modeChooser.getItems().addAll(CANTalon.TalonControlMode.PercentVbus, CANTalon.TalonControlMode.Speed, CANTalon.TalonControlMode.Position, CANTalon.TalonControlMode.Voltage, CANTalon.TalonControlMode.Current);
		modeChooser.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
			disable();
			handler.getDeviceInfoManager().setControlMode(modeChooser.getItems().get(newValue.intValue()), id);
		});
		
		modeChooser.getSelectionModel().selectFirst();
		
		enableButton.setOnAction(e -> {
			if(enableButton.getText().equals("Enable")) {
				if(DeviceInfo.RobotEnabled) {
					enableButton.setText("Disable");
					handler.getDeviceInfoManager().setBoolean("Enabled", true, id);
					handler.getDeviceInfoManager().setDouble("SetPoint", Util.getValue(setPointField), id);
				}
			} else {
				enableButton.setText("Enable");
				handler.getDeviceInfoManager().setBoolean("Enabled", false, id);
				squareWaveButton.setText("Start Square Wave");
				squareWaveMonitor.end();
			}
		});
		
		squareWaveButton.setOnAction(e -> {
			if(squareWaveButton.getText().equals("Start Square Wave") && squareWaveMonitor.start()) {
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
		
		disable();
	}
	
	public void disable() {
		handler.getDeviceInfoManager().setBoolean("Enabled", false, id);
		enableButton.setText("Enable");
		squareWaveButton.setText("Start Square Wave");
		squareWaveMonitor.end();
	}
	
	public void applyAllValuesToTable() {
		
	}
}
