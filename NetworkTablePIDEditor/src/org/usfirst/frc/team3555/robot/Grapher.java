package org.usfirst.frc.team3555.robot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Grapher extends Application {
	private Series<Number, Number> series;
	private Series<Number, Number> setPointSeries;
	
	private int x = 0;
	private int value;
	private boolean going = true;
	private int scale = 20;

	private NetworkReader reader;
	private Handler handler;
	
	@SuppressWarnings("unchecked")
	@Override
    public void start(Stage stage) throws Exception {
		reader = new NetworkReader();
		handler = new Handler(this, reader);
		
    	stage.setTitle("Line Chart");
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/UI.fxml"));
    	Scene scene = new Scene(root);
    	
    	LineChart<Number, Number> lineChart = (LineChart<Number, Number>) scene.lookup("#lineChart");
    	
    	Button b = (Button) (scene.lookup("#go"));
    	b.setOnAction(event -> {
    		going = !going;
    	});
    	
    	TextField t = (TextField) (scene.lookup("#field"));
    	t.setText(Double.toString(reader.getSetPoint()));
    	t.textProperty().addListener((observable, oldValue, newValue) -> {
    		int a;
    		
    		try{
    			a = Integer.valueOf(newValue);
    		}
    		catch(NumberFormatException e){
    			return;
    		}
    		
    		reader.setSetPoint(a);
    	});
    	
    	TextField p = (TextField) scene.lookup("#p");
    	p.setText(Double.toString(reader.getP()));
    	
    	TextField i = (TextField) scene.lookup("#i");
    	i.setText(Double.toString(reader.getI()));
    	
    	TextField d = (TextField) scene.lookup("#d");
    	d.setText(Double.toString(reader.getD()));
    	
    	p.textProperty().addListener((observable, oldValue, newValue) -> {
    		reader.setP(Double.valueOf(newValue));
    	});
    	
    	i.textProperty().addListener((observable, oldValue, newValue) -> {
    		reader.setI(Double.valueOf(newValue));
    	});
    	
    	d.textProperty().addListener((observable, oldValue, newValue) -> {
    		reader.setD(Double.valueOf(newValue));
    	});
    	
    	Label l = (Label) (scene.lookup("#pointLabel"));
    	l.setText(Integer.toString(scale));
    	
        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        
        yAxis.setForceZeroInRange(false);
        xAxis.setForceZeroInRange(false);

        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
        
        series = new Series<Number, Number>();
        setPointSeries = new Series<Number, Number>();

        series.setName("CANTalon Speed");
        setPointSeries.setName("SetPoint");
        
        lineChart.getData().add(series);
        lineChart.getData().add(setPointSeries);
        
        lineChart.setCreateSymbols(false);
        
        lineChart.setOnScroll(event -> {
        	if(event.getDeltaY() < 0){
        		scale++;
        	}
        	if(event.getDeltaY() > 0){
        		scale--;
        		if(scale < 20){
        			scale = 20;
        		}
        	}
        	
        	l.setText(Integer.toString(scale));
//        	if(scale > series.getData().size()){
//        		scale = series.getData().size();
//        	}
        });
        
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        
        gameLoop.getKeyFrames().add(new KeyFrame(Duration.seconds(.1), event -> {
        	handler.sync();
        }));
        
        gameLoop.play();
        
        stage.setScene(scene);
        stage.show();
    }
	
	public void update(double value, double setPoint){
		if(going){
			series.getData().add(new Data<Number, Number>(x, value));
			setPointSeries.getData().add(new Data<Number, Number>(x, setPoint));
			
			if(series.getData().size() >= scale + 1){
				while(series.getData().size() > scale){
					series.getData().remove(0);
					setPointSeries.getData().remove(0);
				}
			}
			
			if(scale < 20)
				scale = 20;
			x++;
		}
	}
	
	public void start(KeyFrame kf){
		Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        
        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
	}

	public int getValue(){return value;}
	public void setValue(int value){this.value = value;}
	public boolean isGoing(){return going;}
	public void setGoing(boolean going){this.going = going;}
}
