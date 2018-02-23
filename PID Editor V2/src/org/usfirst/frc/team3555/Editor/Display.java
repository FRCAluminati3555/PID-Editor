package org.usfirst.frc.team3555.Editor;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Editor.Components.FileIO;
import org.usfirst.frc.team3555.Editor.Components.PIDEditor;
import org.usfirst.frc.team3555.Editor.Components.Grapher.Grapher;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
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
	
	private Button exportUIButton;
	private Button loadUIButton;
	
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
    		addEditor((int) Util.getValue(idField));
    	});
    	
    	addGraphButton = (Button) (scene.lookup("#AddGraph"));
    	addGraphButton.setOnAction(e -> {
    		addGraph((int) Util.getValue(idField));
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
    	
    	exportUIButton = (Button) (scene.lookup("#ExportUI"));
    	exportUIButton.setOnAction(e -> {
    		try {
				FileIO.exportDisplay(this);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	});
    	
    	loadUIButton = (Button) (scene.lookup("#LoadUI"));
    	loadUIButton.setOnAction(e -> {
    		try {
				FileIO.readDisplay(this);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
    	});

    	stage.setOnCloseRequest(e -> {
    		handler.getDeviceInfoManager().ForEach(device -> {
    			if(!(boolean) device.getInfo(Properties.Enabled))
    				handler.getDeviceInfoManager().sendData(device, Properties.Enabled, false);
    			PIDLauncher.client.shutDown();
    		});
    	});
    	
    	Timeline loop = new Timeline();
        loop.setCycleCount(Timeline.INDEFINITE);
        
        loop.getKeyFrames().add(new KeyFrame(Duration.seconds(.0167), event -> {
        	handler.update();
        }));
        
        loop.play();
        
        stage.setScene(scene);
        stage.show();
	}
	
	public PIDEditor addEditor(int id, double x, double y) {
		PIDEditor editor = addEditor(id);
		
		if(editor == null)
			return null;
		
		editor.setLayoutX(x);
		editor.setLayoutY(y);
		
		return editor;
	}
	
	public PIDEditor addEditor(int id) {
//		int id = (int) Util.getValue(idField);
		if(!handler.getDeviceInfoManager().containsId(id))
			return null;
		
		PIDEditor editor = new PIDEditor(handler, handler.getDeviceInfoManager().getDeviceInfo(Controller.CANTalon, id));
		FileIO.readInformation(editor);
		
		pane.getChildren().add(editor);
		return editor;
	}
	
	public Grapher addGrapher(int id, double x, double y) {
		Grapher graph = addGraph(id);
		
		if(graph == null)
			return null;
		
		graph.getLineChart().setLayoutX(x);
		graph.getLineChart().setLayoutY(y);
		
		return graph;
	}
	
	public Grapher addGraph(int id) {
//		int id = (int) Util.getValue(idField);
		if(!handler.getDeviceInfoManager().containsId(id))
			return null;
		
//		System.out.println("\n\n\n\n" + handler.getDeviceInfoManager().getDeviceInfo(Controller.CANTalon, id) + "\n\n\n\n\n");
		Grapher g = new Grapher(handler, handler.getDeviceInfoManager().getDeviceInfo(Controller.CANTalon, id));
		
		handler.getUpdater().add(g);
		pane.getChildren().add(g.getLineChart());
		
		return g;
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
	
	public ObservableList<Node> getChilderen() {
		return pane.getChildren();
	}
	
	public Stage getStage(){return stage;}
}
