package org.usfirst.frc.team3555.Editor.Components.Grapher;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Editor.Handler;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfo;

import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class DataSeries {
	private Series<Number, Number> dataSeries;
	private ArrayList<Point2D> previousData;
	
	private Handler handler;
	private DeviceInfo device;
	private Properties property;
	
	private String name;
	private int id;
	
	public DataSeries(Handler handler, DeviceInfo device, Properties property) {
		this.handler = handler;
		this.device = device;
		this.property = property;
		
		this.name = property.toString();
		this.id = device.getId();
		
		previousData = new ArrayList<>();
		dataSeries = new Series<Number, Number>();
		dataSeries.setName(name);
	}
	
	public void updateData(double time) {
		previousData.add(new Point2D.Double(time, (double) device.getInfo(property)));
	}
	
	public void setSeriesData(int scale, int translate) {
		dataSeries.getData().clear();
//		setPointSeries.getData().clear();
		
		if(previousData.size() > scale && previousData.size() - 1 - scale - translate > 0)
			for(int i = previousData.size() - 1 - scale - translate; i < previousData.size() - 1 - translate; i++) {
				dataSeries.getData().add(new Data<Number, Number>(previousData.get(i).getX(), previousData.get(i).getY()));
//				setPointSeries.getData().add(new Data<Number, Number>(previousSetPointData.get(i).getX(), previousSetPointData.get(i).getY()));
			}
		else 
			for(int i = 0; i < previousData.size() - 1; i++) {
				dataSeries.getData().add(new Data<Number, Number>(previousData.get(i).getX(), previousData.get(i).getY()));
//				setPointSeries.getData().add(new Data<Number, Number>(previousSetPointData.get(i).getX(), previousSetPointData.get(i).getY()));
			}
	}

	public void clear() {
		dataSeries.getData().clear();
		previousData.clear();
	}
	
	public Series<Number, Number> getDataSeries() { return dataSeries; }
	public ArrayList<Point2D> getPreviousData() { return previousData; }
	public String getName() { return name; }
}
