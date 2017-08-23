package org.usfirst.frc.team3555.robot.Components;

import java.util.ArrayList;

public class Updater {
	private ArrayList<Updatable> toUpdate;
	
	public Updater() {
		toUpdate = new ArrayList<>();
	}
	
	public void update() {
		for(int i = toUpdate.size() - 1; i >= 0; i--) {
			if(!toUpdate.get(i).isActive()) {
				toUpdate.remove(i);
				continue;
			}
			
			toUpdate.get(i).update();
		}
	}
	
	public void add(Updatable toAdd) {
		toUpdate.add(toAdd);
	}
}
