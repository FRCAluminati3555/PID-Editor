package org.usfirst.frc.team3555.robot;

import java.util.Set;

import org.usfirst.frc.team3555.robot.Components.PIDEditor;
import org.usfirst.frc.team3555.robot.Components.Grapher.Grapher;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
	
	private Button addGraphButton;
	private Button addEditorButton;
	private TextField idField;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		stage.getIcons().add(new Image("/Icon.png"));
		stage.setTitle("Line Chart");
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/fxml/UI.fxml"));
    	Pane pane = new Pane(root);
    	Scene scene = new Scene(pane, 800, 600);

    	handler = new Handler();
    	
    	idField = (TextField) (scene.lookup("#IDField"));
    	
    	addEditorButton = (Button) (scene.lookup("#AddEditor"));
    	addEditorButton.setOnAction(e -> {
    		int id = (int) Util.getValue(idField);
    		if(!handler.getDeviceInfoManager().getDevices().containsKey(id))
    			return;
    		
    		pane.getChildren().add(new PIDEditor(handler, id));
    	});
    	
    	addGraphButton = (Button) (scene.lookup("#AddGraph"));
    	addGraphButton.setOnAction(e -> {
    		int id = (int) Util.getValue(idField);
    		if(!handler.getDeviceInfoManager().getDevices().containsKey(id))
    			return;
    		
    		Grapher g = new Grapher(handler, id);
    		
    		handler.getUpdater().add(g);
    		pane.getChildren().add(g.getLineChart());
    	});

    	stage.setOnCloseRequest(e -> {
    		Set<Integer> keys = handler.getDeviceInfoManager().getDevices().keySet();
    		
    		for(Integer id : keys) 
    			handler.getDeviceInfoManager().setBoolean("Enabled", false, id);
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
	
	public Stage getStage(){return stage;}
}
