package org.usfirst.frc.team3555.robot;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Handler {
	private TextField setPointField, pField, iField, dField;
	private Button enableButton, syncButton;
	private Label scaleLabel;
	private LineChart lineChart;
	
	private Stage stage;
	private Scene scene;

	@SuppressWarnings("unchecked")
	public Handler(Stage stage, Scene scene) {
		this.stage = stage;
		this.scene = scene;
		
		enableButton = (Button) (scene.lookup("#enable"));
		syncButton = (Button) (scene.lookup("#Sync"));
		setPointField = (TextField) (scene.lookup("#field"));
		pField = (TextField) scene.lookup("#p");
		iField = (TextField) scene.lookup("#i");
		dField = (TextField) scene.lookup("#d");
		scaleLabel = (Label) (scene.lookup("#pointLabel"));
		lineChart = (LineChart<Number, Number>) scene.lookup("#lineChart");
	}

	public TextField getSetPointField() {
		return setPointField;
	}

	public TextField getpField() {
		return pField;
	}

	public TextField getiField() {
		return iField;
	}

	public TextField getdField() {
		return dField;
	}

	public Button getEnableButton() {
		return enableButton;
	}

	public Button getSyncButton() {
		return syncButton;
	}

	public Label getScaleLabel() {
		return scaleLabel;
	}

	public LineChart getLineChart() {
		return lineChart;
	}

	public Stage getStage() {
		return stage;
	}

	public Scene getScene() {
		return scene;
	}
}
