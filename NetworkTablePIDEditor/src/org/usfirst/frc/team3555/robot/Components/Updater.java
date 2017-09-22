package org.usfirst.frc.team3555.robot.Components;

import java.util.ArrayList;
import java.util.Comparator;

import org.usfirst.frc.team3555.robot.Components.Grapher.Grapher;

public class Updater {
	private ArrayList<Updatable> toUpdate;
	private Comparator<Updatable> sorter;
	
	public Updater() {
		toUpdate = new ArrayList<>();
		
		sorter = new Comparator<Updatable>() {
			@Override
			public int compare(Updatable o1, Updatable o2) {
				if(o1 instanceof Grapher)
					return 1;
				else 
					return -1;
			}
		};
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
		toUpdate.sort(sorter);
	}
}
