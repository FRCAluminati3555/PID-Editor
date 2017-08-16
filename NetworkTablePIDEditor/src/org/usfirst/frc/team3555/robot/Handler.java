package org.usfirst.frc.team3555.robot;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Handler {
	private TextField setPointField, pField, iField, dField;
	private Button enableButton;
	private Label scaleLabel;
	private LineChart<Number, Number> lineChart;
	
	private Button squareWaveButton;
	private TextField frequencyField, squareSetPoint1Field, squareSetPoint2Field;

	private Display display;
	private Stage stage;
	private Scene scene;

	private NetworkReader reader;
	private SquareWaveMonitor squareWaveMonitor;
	
	@SuppressWarnings("unchecked")
	public Handler(Display display, Stage stage, Scene scene, NetworkReader reader) {
		this.display = display;
		this.stage = stage;
		this.scene = scene;
		this.reader = reader;
		
		squareWaveMonitor = new SquareWaveMonitor(this);
		
		enableButton = (Button) (scene.lookup("#enable"));
		setPointField = (TextField) (scene.lookup("#field"));
		pField = (TextField) scene.lookup("#p");
		iField = (TextField) scene.lookup("#i");
		dField = (TextField) scene.lookup("#d");
		scaleLabel = (Label) (scene.lookup("#pointLabel"));
		lineChart = (LineChart<Number, Number>) scene.lookup("#lineChart");
		
		squareWaveButton = (Button) (scene.lookup("#SquareWaveButton"));
		frequencyField = (TextField) (scene.lookup("#FrequencyField"));
		squareSetPoint1Field = (TextField) (scene.lookup("#SquareSetPoint1"));
		squareSetPoint2Field = (TextField) (scene.lookup("#SquareSetPoint2"));
	}
	
	public double getValue(TextField from) {
		double temp;
		
		try {
			temp = Double.valueOf(from.getText());
		} catch(NumberFormatException ex) {
			temp = 0;
		}
		
		return temp;
	}

	public Display getDisplay(){return display;}
	public Button getSquareWaveButton(){return squareWaveButton;}
	public TextField getFrequencyField(){return frequencyField;}
	public TextField getSquareSetPoint1Field(){return squareSetPoint1Field;}
	public TextField getSquareSetPoint2Field(){return squareSetPoint2Field;}
	public SquareWaveMonitor getSquareWaveMonitor(){return squareWaveMonitor;}
	public NetworkReader getReader(){return reader;}
	public TextField getSetPointField(){return setPointField;}
	public TextField getpField(){return pField;}
	public TextField getiField(){return iField;}
	public TextField getdField(){return dField;}
	public Button getEnableButton(){return enableButton;}
	public Label getScaleLabel(){return scaleLabel;}
	public LineChart<Number, Number> getLineChart(){return lineChart;}
	public Stage getStage(){return stage;}
	public Scene getScene(){return scene;}
}
