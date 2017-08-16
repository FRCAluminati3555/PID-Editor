package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Display extends Application {
	private Handler handler;
	
	private CANTalonGrapher grapher;
	private NetworkReader reader;
	private Stage stage;
	private Scene scene;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		stage.setTitle("Line Chart");
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/UI.fxml"));
    	scene = new Scene(root);
		
		reader = new NetworkReader();
		reader.setEnabled(false);
		
		handler = new Handler(this, stage, scene, reader);
		grapher = new CANTalonGrapher(handler, reader);
    	
    	handler.getEnableButton().setOnAction(event -> {
    		if(handler.getReader().isRobotEnabled()) {
	    		if(handler.getSquareWaveMonitor().isMonitoring())
	    			handler.getSquareWaveMonitor().end();
	    		
				grapher.alternateState();
				
				reader.setEnabled(grapher.isGoing());
				
				if(handler.getEnableButton().getText().equals("Enable"))
					handler.getEnableButton().setText("Disable");
				else
					handler.getEnableButton().setText("Enable");
    		} else {
    			handler.getEnableButton().setText("Enable");
    			handler.getReader().setEnabled(false);
    		}
    	});
    	
    	handler.getSetPointField().setText(Double.toString(reader.getSetPoint()));
    	handler.getSetPointField().textProperty().addListener((observable, oldValue, newValue) -> {
    		if(handler.getSquareWaveMonitor().isMonitoring())
    			handler.getSquareWaveMonitor().end();
    		
    		Double a;
    		
    		try{
    			a = Double.valueOf(newValue);
    		}
    		catch(NumberFormatException e){
    			return;
    		}
    		
    		reader.setSetPoint(a);
    	});
    	
    	handler.getpField().setText(Double.toString(reader.getP()));
    	handler.getiField().setText(Double.toString(reader.getI()));
    	handler.getdField().setText(Double.toString(reader.getD()));
    	
    	handler.getpField().textProperty().addListener((observable, oldValue, newValue) -> {
    		if(handler.getSquareWaveMonitor().isMonitoring())
    			handler.getSquareWaveMonitor().end();
    		
    		Double a;
    		
    		try{
    			a = Double.valueOf(newValue);
    		}
    		catch(NumberFormatException e){
    			return;
    		}
    		
    		reader.setP(a);
    	});
    	
    	handler.getiField().textProperty().addListener((observable, oldValue, newValue) -> {
    		if(handler.getSquareWaveMonitor().isMonitoring())
    			handler.getSquareWaveMonitor().end();
    		
    		Double a;
    		
    		try{
    			a = Double.valueOf(newValue);
    		}
    		catch(NumberFormatException e){
    			return;
    		}
    		
    		reader.setI(a);
    	});
    	
    	handler.getdField().textProperty().addListener((observable, oldValue, newValue) -> {
    		if(handler.getSquareWaveMonitor().isMonitoring())
    			handler.getSquareWaveMonitor().end();
    		
    		Double a;
    		
    		try{
    			a = Double.valueOf(newValue);
    		}
    		catch(NumberFormatException e){
    			return;
    		}
    		
    		reader.setD(a);
    	});
    	
    	handler.getSquareSetPoint1Field().textProperty().addListener((observable, oldValue, newValue) -> {
    		if(handler.getSquareWaveMonitor().isMonitoring())
    			handler.getSquareWaveMonitor().start();
    	});
    	
    	handler.getSquareSetPoint2Field().textProperty().addListener((observable, oldValue, newValue) -> {
    		if(handler.getSquareWaveMonitor().isMonitoring())
    			handler.getSquareWaveMonitor().start();
    	});
    	
    	handler.getFrequencyField().textProperty().addListener((observable, oldValue, newValue) -> {
    		if(handler.getSquareWaveMonitor().isMonitoring())
    			handler.getSquareWaveMonitor().start();
    	});
    	
    	handler.getScaleLabel().setText(Integer.toString(grapher.getScale()));
    	
    	handler.getSquareWaveButton().setOnAction(e -> {
    		//End Square Wave
    		if(handler.getSquareWaveMonitor().isMonitoring()) {
    			handler.getSquareWaveMonitor().end();
    			handler.getEnableButton().fire();

    			double set;
    			
    			try {
    				set = Double.valueOf(handler.getSetPointField().getText());
    			} catch(NumberFormatException ex) {
    				set = 0;
    			}
    			
    			reader.setSetPoint(set);
    		} else if(handler.getReader().isRobotEnabled()){ //Start Square Wave
				handler.getSquareWaveMonitor().start();
//				handler.getSquareWaveButton().setText("End Square Wave");
    		}
    	});
    	
    	syncPIDData();
    	
    	Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        
        gameLoop.getKeyFrames().add(new KeyFrame(Duration.seconds(.1), event -> {
        	if(grapher.isGoing() && !handler.getReader().isRobotEnabled()) {
        		grapher.alternateState();
        		handler.getEnableButton().fire();
        	}
        	if(handler.getSquareWaveMonitor().isMonitoring() && !handler.getReader().isRobotEnabled())
        		handler.getSquareWaveMonitor().end();
        	
        	handler.getSquareWaveMonitor().update();
        	grapher.update(reader.getValue(), reader.getSetPoint());
        }));
        
        gameLoop.play();
        
        stage.setOnCloseRequest(value -> {
        	NetworkTable.shutdown();
        });
        
        stage.setScene(scene);
        stage.show();
	}
	
	public void syncPIDData(){
		handler.getpField().setText(String.valueOf(reader.getP()));
		handler.getiField().setText(String.valueOf(reader.getI()));
		handler.getdField().setText(String.valueOf(reader.getD()));
		handler.getSetPointField().setText(String.valueOf(reader.getSetPoint()));
	}

	public CANTalonGrapher getGrapher(){return grapher;}
	public NetworkReader getReader(){return reader;}
	public Stage getStage(){return stage;}
	public Scene getScene(){return scene;}
}
