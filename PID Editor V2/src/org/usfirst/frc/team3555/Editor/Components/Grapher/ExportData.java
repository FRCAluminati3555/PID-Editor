/**
 * This class has the ability to export the data of a certain controller
 * Not Updated to the 2018 season
 */

//package org.usfirst.frc.team3555.Editor.Components.Grapher;
//
//import java.awt.geom.Point2D;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Set;
//
//import javafx.scene.control.Label;
//import jxl.Workbook;
//import jxl.write.WritableSheet;
//import jxl.write.WritableWorkbook;
//import jxl.write.WriteException;
//
//public class ExportData {
//	public static void export(String name, HashMap<String, DataSeries> data) {
//		WritableWorkbook workbook = null;
//		
//		try {
//			workbook = Workbook.createWorkbook(new File(System.getProperty("user.home") + "/Desktop/" + name + "-Export.xls"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		WritableSheet sheet = workbook.createSheet("Sheet", 0);
//
//		Set<String> temp = data.keySet();
//		String[] keys = new String[data.size()];
//		
//		int j = 0;
//		for(String key : temp) {
//			keys[j] = key;
//			j++;
//		}
//		
//		try {
//			sheet.addCell(new Label(0, 0, "Tick"));
//		} catch (WriteException e1) {
//			e1.printStackTrace();
//		}
//		
//		for(int i = 1; i <= keys.length; i++) {
//			try {
//				sheet.addCell(new Label(i, 0, keys[i-1]));
//			} catch (WriteException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		//Fill out all the tick values
//		ArrayList<Point2D> ticks = data.get(keys[0]).getPreviousData();
//		for(int y = 1; y <= ticks.size(); y++) {
//			try {
//				sheet.addCell(new Number(0, y, ticks.get(y - 1).getX()));
//			} catch (WriteException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		for(int x = 1; x <= keys.length; x++) {
//			ArrayList<Point2D> points = data.get(keys[x - 1]).getPreviousData();
//			for(int y = 1; y <= points.size(); y++) {
//				try {
//					sheet.addCell(new Number(x, y, points.get(y - 1).getY()));
//				} catch (WriteException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		try {
//			workbook.write();
//			workbook.close();
//		} catch (WriteException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
