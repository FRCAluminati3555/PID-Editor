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
		
    	handler = new Handler(stage, scene);
    	
		reader = new NetworkReader();
		reader.setEnabled(false);
		
		grapher = new CANTalonGrapher(handler, reader);
    	
    	handler.getEnableButton().setOnAction(event -> {
//    		if(reader.isRobotEnabled()){
				grapher.alternateState();
				
				reader.setEnabled(grapher.isGoing());
				
				if(handler.getEnableButton().getText().equals("Enable"))
					handler.getEnableButton().setText("Disable");
				else
					handler.getEnableButton().setText("Enable");
//    		}
    	});
    	
    	handler.getSyncButton().setOnAction(e -> {
    		syncPIDData();
    	});
    	
    	
    	handler.getSetPointField().setText(Double.toString(reader.getSetPoint()));
    	handler.getSetPointField().textProperty().addListener((observable, oldValue, newValue) -> {
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
    		Double a;
    		
    		try{
    			a = Double.valueOf(newValue);
    		}
    		catch(NumberFormatException e){
    			return;
    		}
    		
    		reader.setD(a);
    	});
    	
    	handler.getScaleLabel().setText(Integer.toString(grapher.getScale()));
    	
    	Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        
        gameLoop.getKeyFrames().add(new KeyFrame(Duration.seconds(.1), event -> {
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
