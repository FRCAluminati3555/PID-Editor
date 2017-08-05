package org.usfirst.frc.team3555.robot;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.KeyCode;
import sun.nio.ch.Net;

public class CANTalonGrapher {
	private Series<Number, Number> dataSeries;
	private ArrayList<Point2D> previousData;
	
	private Series<Number, Number> setPointSeries;
	private ArrayList<Point2D> previousSetPointData;
	
	private int x;
	private boolean going;
	private int scale = 25;
	private int translate;

	private double startX;
	private int resetPoint = 500;

	private NetworkReader reader;
	private Handler handler;
	
	@SuppressWarnings("unchecked")
	public CANTalonGrapher(Handler handler, NetworkReader reader) {
		this.handler = handler;
		this.reader = reader;
		
		LineChart<Number, Number> lineChart = handler.getLineChart();
    	lineChart.setAnimated(false);
    	
    	previousData = new ArrayList<>();
    	previousSetPointData = new ArrayList<>();
    	
        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        
        yAxis.setForceZeroInRange(true);
        xAxis.setForceZeroInRange(false);

        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
        
        dataSeries = new Series<Number, Number>();
        setPointSeries = new Series<Number, Number>();

        dataSeries.setName("CANTalon Speed");
        setPointSeries.setName("SetPoint");
        
        lineChart.getData().add(dataSeries);
        lineChart.getData().add(setPointSeries);
        
        lineChart.setCreateSymbols(false);
        
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
        	
        	handler.getScaleLabel().setText(Integer.toString(scale));
        });
        
        lineChart.setOnKeyPressed(e -> {
        	if(e.getCode() == KeyCode.SPACE)
        		alternateState();
        });
        
        lineChart.setOnMousePressed(e -> {
        	startX = e.getX();
        });
        
        lineChart.setOnMouseDragged(e -> {
        	if(!going && Math.abs(e.getX() - startX) > 5) {
	        	if(e.getX() > startX) {
	        		if(!(previousData.size() - 2 - scale - translate < 0)) {
	        			translate++;
	        			startX = e.getX();
	        		}
	        	} else if (translate - 1 > 0){
	        		translate--;
	        		startX = e.getX();
	        	}
        	}
        });
	}
	
	public void update(double value, double setPoint){
//		value = Math.random() * 10;
		
		if(going) {
			translate = 0;

			previousSetPointData.add(new Point2D.Double(x, setPoint));
			previousData.add(new Point2D.Double(x, value));
			
			if(scale < 20)
				scale = 20;
			x++;
		}
		
		setSeriesData();
		
		if(x == resetPoint) {
			dataSeries.getData().clear();
			previousData.clear();

			setPointSeries.getData().clear();
			previousSetPointData.clear();
			
			x = 0;
		}
	}
	
	public void setSeriesData() {
		dataSeries.getData().clear();
		setPointSeries.getData().clear();
		
		if(previousData.size() > scale && !(previousData.size() - 1 - scale - translate < 0))
			for(int i = previousData.size() - 1 - scale - translate; i < previousData.size() - 1 - translate; i++) {
				dataSeries.getData().add(new Data<Number, Number>(previousData.get(i).getX(), previousData.get(i).getY()));
				setPointSeries.getData().add(new Data<Number, Number>(previousSetPointData.get(i).getX(), previousSetPointData.get(i).getY()));
			}
		else 
			for(int i = 0; i < previousData.size() - 1; i++) {
				dataSeries.getData().add(new Data<Number, Number>(previousData.get(i).getX(), previousData.get(i).getY()));
				setPointSeries.getData().add(new Data<Number, Number>(previousSetPointData.get(i).getX(), previousSetPointData.get(i).getY()));
			}
	}
	
	public int getScale() {return scale;}
	public void setScale(int scale){this.scale = scale;}
	public void alternateState(){going = !going;}
	public boolean isGoing(){return going;}
}
