package org.usfirst.frc.team3555.robot.Components.Grapher;

import java.io.File;
import java.util.HashMap;

import org.usfirst.frc.team3555.robot.Handler;
import org.usfirst.frc.team3555.robot.Components.Updatable;

import javafx.beans.value.ObservableValue;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class Grapher extends Updatable {
//	private Series<Number, Number> dataSeries;
//	private ArrayList<Point2D> previousData;
//	
//	private Series<Number, Number> setPointSeries;
//	private ArrayList<Point2D> previousSetPointData;

	private HashMap<String, DataSeries> data;
	
	private int x;
	private boolean going;
	private int scale = 25;
	private int translate;

	private double startX;
	private double startY;
	
	private double rightClickSensitivity;
	private boolean dragging;
	
	private int resetPoint = 1000;

	private Handler handler;
	private LineChart<Number, Number> lineChart;
	private ContextMenu rightClickMenu;
	
	private int id;
	
	public Grapher(Handler handler, int id) {
		this.handler = handler;
		this.id = id;
		
		rightClickSensitivity = 10;
		
		active = true;
		
		data = new HashMap<>();
		
		NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
		
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);
    	lineChart.setAnimated(false);

    	String name = handler.getDeviceInfoManager().getDevices().get(id).getName();
    	if(name != null) 
    		lineChart.setTitle(name);
    	else
    		lineChart.setTitle(String.valueOf(id));
    		
        yAxis.setForceZeroInRange(true);
        xAxis.setForceZeroInRange(false);

        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
        
        lineChart.setCreateSymbols(false);
        lineChart.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
        lineChart.setStyle("-fx-background-color: white");
        
        rightClickMenu = new ContextMenu();
        
        generateRightClickMenu("SetPoint", "Velocity", "Position", "Current", "Voltage", "Temperature");
        MenuItem exportItem = new MenuItem("Export");
        exportItem.setOnAction(e -> {
        	//TODO Export
        	ExportData.export(lineChart.getTitle(), data);
        });
        rightClickMenu.getItems().add(exportItem);
        
        
        data.put("UpperBound", new DataSeries(handler, "UpperBound", id));
        data.put("LowerBound", new DataSeries(handler, "LowerBound", id));
        
        lineChart.getData().add(data.get("UpperBound").getDataSeries());
        lineChart.getData().add(data.get("LowerBound").getDataSeries());
        
        lineChart.setOnScroll(event -> {
        	if(event.getDeltaY() < 0) {
        		scale++;
        	}
        	if(event.getDeltaY() > 0) {
        		scale--;
        		if(scale < 20){
        			scale = 20;
        		}
        	}
        });
        
        lineChart.setOnKeyPressed(e -> {
        	if(e.getCode() == KeyCode.SPACE)
        		alternateState();
        });
        
        lineChart.setOnMousePressed(e -> {
        	if(e.getButton() == MouseButton.PRIMARY) {
        		startX = e.getX();
        		rightClickMenu.hide();
        	}
        	else if(e.getButton() == MouseButton.SECONDARY) {
        		startX = lineChart.getLayoutX() - e.getSceneX();
    			startY = lineChart.getLayoutY() - e.getSceneY();
    			
    			lineChart.toFront();
    			
    			rightClickMenu.hide();
        	} 
        });
        
        lineChart.setOnMouseReleased(e -> {
        	if(e.getButton() == MouseButton.SECONDARY) {
        		if(!dragging)
        			rightClickMenu.show(lineChart, e.getScreenX(), e.getScreenY());
        		else
        			dragging = false;
        	}
        });
        
        lineChart.setOnMouseDragged(e -> {
        	if(!going && Math.abs(e.getX() - startX) > 5 && e.getButton() == MouseButton.PRIMARY) {
	        	if(e.getX() > startX) {
	        		if(x - 2 - scale - translate > 0) {
	        			translate++;
	        			startX = e.getX();
	        		}
	        	} else if (translate - 1 > 0){
	        		translate--;
	        		startX = e.getX();
	        	}
        	} else if(e.getButton() == MouseButton.SECONDARY && Math.abs(e.getX() - startX) > rightClickSensitivity) {
        		dragging = true;
        		lineChart.setLayoutX(startX + e.getSceneX());
        		lineChart.setLayoutY(startY + e.getSceneY());
        	}
        });
	}
	
	@Override
	public void update() {
//		System.out.println("Going: " + going + " Id Enabled: " + handler.getDeviceInfoManager().getBoolean("Enabled", id));
		
		if(going == handler.getDeviceInfoManager().getBoolean("Enabled", id)) {
			if(!data.isEmpty()) {
				if(going) {
					translate = 0;
		
					data.forEach((s, d) -> {
						d.updateData(x);
					});
					
					if(scale < 20)
						scale = 20;
					x++;
				}
				
				data.forEach((s, d) -> {
					d.setSeriesData(scale, translate);
				});
				
				if(x == resetPoint) {
					data.forEach((s, d) -> {
						d.clear();
					});
					x = 0;
				}
			}
		} else {
//			System.out.println("Switch");
			alternateState();
		}
	}
	
	public void generateRightClickMenu(String... names) {
		for(String name : names) {
			data.put(name, new DataSeries(handler, name, id));
			
			CheckMenuItem item = new CheckMenuItem(name);
	        rightClickMenu.getItems().add(item);
	        item.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
	        	if(newValue) {
	        		lineChart.getData().add(data.get(item.getText()).getDataSeries());
	        	} else {
	        		lineChart.getData().remove(data.get(item.getText()).getDataSeries());
	        	}
	        });
		}
	}
	
//	public void setSeriesData() {
//		dataSeries.getData().clear();
//		setPointSeries.getData().clear();S
//		
//		if(previousData.size() > scale && previousData.size() - 1 - scale - translate > 0)
//			for(int i = previousData.size() - 1 - scale - translate; i < previousData.size() - 1 - translate; i++) {
//				dataSeries.getData().add(new Data<Number, Number>(previousData.get(i).getX(), previousData.get(i).getY()));
//				setPointSeries.getData().add(new Data<Number, Number>(previousSetPointData.get(i).getX(), previousSetPointData.get(i).getY()));
//			}
//		else 
//			for(int i = 0; i < previousData.size() - 1; i++) {
//				dataSeries.getData().add(new Data<Number, Number>(previousData.get(i).getX(), previousData.get(i).getY()));
//				setPointSeries.getData().add(new Data<Number, Number>(previousSetPointData.get(i).getX(), previousSetPointData.get(i).getY()));
//			}
//	}
	
	public LineChart<Number, Number> getLineChart(){return lineChart;}
	public int getScale() {return scale;}
	public void setScale(int scale){this.scale = scale;}
	public void alternateState(){going = !going;}
	public boolean isGoing(){return going;}
}
