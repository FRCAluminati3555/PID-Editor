package org.usfirst.frc.team3555.Editor;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Editor.Components.PIDEditor;
import org.usfirst.frc.team3555.Editor.Components.Grapher.Grapher;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Display extends Application {
	private Handler handler;
	
	private Stage stage;
	private Pane pane;
	
	private Button addGraphButton;
	private Button addEditorButton;
	private Button allButton;
	
	private TextField idField;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		stage.getIcons().add(new Image("/Icon.png"));
		stage.setTitle("Line Chart");
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/UI.fxml"));
    	pane = new Pane(root);
    	Scene scene = new Scene(pane, 800, 600);

    	handler = new Handler();
    	
    	idField = (TextField) (scene.lookup("#IDField"));
    	
    	addEditorButton = (Button) (scene.lookup("#AddEditor"));
    	addEditorButton.setOnAction(e -> {
    		int id = (int) Util.getValue(idField);
    		if(!handler.getDeviceInfoManager().containsId(id))
    			return;
    		
    		pane.getChildren().add(new PIDEditor(handler, handler.getDeviceInfoManager().getDeviceInfo(Controller.CANTalon, id)));
    	});
    	
    	addGraphButton = (Button) (scene.lookup("#AddGraph"));
    	addGraphButton.setOnAction(e -> {
    		int id = (int) Util.getValue(idField);
    		if(!handler.getDeviceInfoManager().containsId(id))
    			return;
    		
    		System.out.println("\n\n\n\n" + handler.getDeviceInfoManager().getDeviceInfo(Controller.CANTalon, id) + "\n\n\n\n\n");
    		Grapher g = new Grapher(handler, handler.getDeviceInfoManager().getDeviceInfo(Controller.CANTalon, id));
    		
    		handler.getUpdater().add(g);
    		pane.getChildren().add(g.getLineChart());
    	});
    	
    	allButton = (Button) (scene.lookup("#AllButton"));
    	allButton.setOnAction(e -> {
    		if(allButton.getText().equals("Enable All") && (boolean) handler.getDeviceInfoManager().getInfo(Controller.DriverStation, Properties.Enabled, 0)) {
    			allButton.setText("Disable All");
    			enableAll();
    		} else {
    			allButton.setText("Enable All");
    			disableAll();
    		}
    	});

    	stage.setOnCloseRequest(e -> {
    		handler.getDeviceInfoManager().ForEach(device -> {
    			if(!(boolean) device.getInfo(Properties.Enabled))
    				handler.getDeviceInfoManager().sendData(device, Properties.Enabled, false);
    		});
    	});
    	
    	Timeline loop = new Timeline();
        loop.setCycleCount(Timeline.INDEFINITE);
        
        loop.getKeyFrames().add(new KeyFrame(Duration.seconds(.1), event -> {
        	handler.update();
        }));
        
        loop.play();
        
        stage.setScene(scene);
        stage.show();
	}
	
	public void enableAll() {
		for(Node n : pane.getChildren()) 
			if(n instanceof PIDEditor) 
				((PIDEditor) n).enable();
	}
	
	public void disableAll() {
		for(Node n : pane.getChildren()) 
			if(n instanceof PIDEditor) 
				((PIDEditor) n).disableAll();
	}

	public Stage getStage(){return stage;}
}
