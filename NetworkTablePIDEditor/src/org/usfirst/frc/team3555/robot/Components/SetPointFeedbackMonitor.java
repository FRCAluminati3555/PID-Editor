package org.usfirst.frc.team3555.robot.Components;

import org.usfirst.frc.team3555.robot.Handler;

public class SetPointFeedbackMonitor extends Updatable {
	private PIDEditor editor;
	private String feedbackProperty;
	
	private boolean feedbackToSetPoint;
	
	public SetPointFeedbackMonitor(Handler handler, PIDEditor editor, int id) {
		super(handler, id);
		this.editor = editor;
	}
	
	@Override
	public void update() {
		if(feedbackToSetPoint && feedbackProperty != null) 
			handler.getDeviceInfoManager().setDouble("SetPoint", handler.getDeviceInfoManager().getDouble(feedbackProperty, id), id);
	}

	public boolean isFeedbackToSetPoint() { return feedbackToSetPoint; }
	public void setFeedbackToSetPoint(boolean feedbackToSetPoint) { this.feedbackToSetPoint = feedbackToSetPoint; }
	public void setFeedbackProperty(String property) { this.feedbackProperty = property; }
	public String getFeedBackProperty() { return feedbackProperty; }
}
