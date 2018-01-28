package org.usfirst.frc.team3555.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Wrapper class of the 2018 WPI_TalonSRX that more resembles the 2017 CANTalon, but is also more convenient for the PID Editor <br> 
 * This will: <br>
 *  Store the enabled state of the controller <br>
 *  Store the setpoint and pidf values <br>
 *  Store and use the Control Mode in set method -> won't have to pass it in every time, just set it earlier on <br>
 *  Store the feedback sensor that is being used <br>
 *  Pass in the idx and timeout when setting pidf values for you (timeout = 10ms, slotidx = 0, pididx = 0) <br>
 *  Convert a rpm setpoint into a native velocity setpoint <br> 
 *  Convert native velocity into rpm <br>
 *  Convert native position into rotations <br>
 *  Convert rotations into a distance measure
 *  
 * @author Sam Secondo
 */
public class CANTalon extends WPI_TalonSRX {
	public static final int kSlotIdx = 0;
	public static final int kPidIdx = 0;
	public static final int kTimeoutMs = 10;
	
	public static ControlMode getControlMode(int controlModeValue) {
		for(ControlMode mode : ControlMode.values())
			if(mode.value == controlModeValue)
				return mode;
		return null;
	}
	
	public static FeedbackDevice getFeedbackDevice(int feedbackDeviceValue) {
		for(FeedbackDevice device : FeedbackDevice.values()) 
			if(device.value == feedbackDeviceValue)
				return device;
		return null;
	}
	
	public static double nativeToRPM(double nativeVelocity, int sensorUnitsPerRotation) { 
		return nativeVelocity * (600.0 / sensorUnitsPerRotation); 
	}
	
	public static double rpmToNative(double rpm, int sensorUnitsPerRotation) {
		return rpm * (sensorUnitsPerRotation / 600.0);
	}
	
	public static double nativeToQuadRPM(double nativeVelocity, int sensorUnitsPerRotation) { 
		return (nativeVelocity / 4.0) * (600.0 / sensorUnitsPerRotation); 
	}
	
	public static double quadRPMToNative(double rpm, int sensorUnitsPerRotation) {
		return (rpm * sensorUnitsPerRotation * 4.0) / 600.0;
	}
	
	public static double nativeToRotations(double nativePosition, int sensorUnitsPerRotation) {
		return nativePosition / sensorUnitsPerRotation;
	}
	
	public static double rotationsToNative(double rotations, int sensorUnitsPerRotation) {
		return rotations * sensorUnitsPerRotation;
	}
	
	public static double nativeToQuadRotations(double nativePosition, int sensorUnitsPerRotation) {
		return (nativePosition / 4.0) / sensorUnitsPerRotation;
	}
	
	public static double quadRotationsToNative(double rotations, int sensorUnitsPerRotation) {
		return rotations * sensorUnitsPerRotation * 4.0;
	}
	
	public static double rotationsToDistance(double rotations, double distancePerRotation) {
		return rotations * distancePerRotation;
	}
	
	public static double distanceToRotations(double distance, double distancePerRotation) {
		return distance / distancePerRotation;
	}
	
	/**
	 * Distance / DistancePerRotation  = Rotations * 60 seconds = Rotations 
	 * Seconds                           Seconds      1 minute     minute
	 */
	public static double linearVelocityToRPM(double linearVelocity, double distancePerRotation) {
		return (linearVelocity / distancePerRotation) * 60;
	}
	
	/**
	 * Rotations * distancePerRotation = distance * 1 minute  = distance 
	 * Minute                            Minute     60 seconds  seconds
	 */
	public static double rpmToLinearVelocity(double rpm, double distancePerRotation) {
		return (rpm * distancePerRotation) / 60.0;
	}
	
	private boolean enabled;
	private double p, i, d, f;
	private double setPoint;
	
	private FeedbackDevice feedbackDevice;
	private ControlMode controlMode;

	private int sensorUnitsPerRotation;
	private double distancePerRotation;
	
	/**
	 * Initializes a CANTalon with a complete clean slate. 
	 * Make sure to set any sensors, sensor units, control modes, pids, etc...
	 * This is set to coast by default -> call setBrake() for brake mode.
	 * 
	 * @param deviceNumber -> ID of the CANTalon
	 */
	public CANTalon(int deviceNumber) {
		super(deviceNumber);
		
		setControlMode(ControlMode.PercentOutput);
		setFeedbackDevice(FeedbackDevice.None);
		set(0);
		setPIDF(0);
		
		setCoast();
		setSensorUnitsPerRotation(0);
	}
	
	public CANTalon(String name, int deviceNumber) {
		this(deviceNumber);
		
		setName("Talon SRX #", deviceNumber + ", " + name);
	}

	/**
	 * @param speed - speed to set -> speed = RPM, Percent = +- 1  
	 */
	public void set(double setPoint) {
		this.setPoint = setPoint;
		
		if(enabled) 
			super.set(controlMode, setPoint);
	}
	
	public void setVelocityRPM(double rpm) {
		setControlMode(ControlMode.Velocity);
		
		if(feedbackDevice == FeedbackDevice.QuadEncoder) {
			System.out.println("\n\nSet RPM: " + rpm);
			set(quadRPMToNative(rpm, sensorUnitsPerRotation));
		}
		else 
			set(rpmToNative(rpm, sensorUnitsPerRotation));
	}
	
	public void setPositionRotations(double rotations) {
		setControlMode(ControlMode.Position);
		
		if(feedbackDevice == FeedbackDevice.QuadEncoder) 
			set(quadRotationsToNative(rotations, sensorUnitsPerRotation));
		else
			set(rotationsToNative(rotations, sensorUnitsPerRotation));
	}
	
	public void setPositionDistance(double distance) {
		setPositionRotations(distanceToRotations(distance, distancePerRotation));
	}
	
	/**
	 * Re-set the setpoint on the talon
	 */
	public void update() { 
		set(setPoint); 
	}
	
	public double getSetPoint() { return setPoint; }

	public void setPID(double p, double i, double d) {
		setP(p);
		setI(i);
		setD(d);
	}
	
	public void setPIDF(double p, double i, double d, double f) {
		setPID(p, i, d);
		setF(f);
	}
	
	public void setPIDF(double all) { setPIDF(all, all, all, all); }
	
	public double getP() { return p; }
	public void setP(double p) {
		this.p = p;
		config_kP(kSlotIdx, p, kTimeoutMs);
	}

	public double getI() { return i; }
	public void setI(double i) {
		this.i = i;
		config_kI(kSlotIdx, i, kTimeoutMs);
	}
	
	public double getD() { return d; }
	public void setD(double d) {
		this.d = d;
		config_kD(kSlotIdx, d, kTimeoutMs);
	}

	public double getF() { return f; }
	public void setF(double f) {
		this.f = f;
		config_kF(kSlotIdx, f, kTimeoutMs);
	}
	
	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		
		if(!enabled)
			stopMotor();
	}
	
	public void enable() { setEnabled(true); }
	public void disable() { setEnabled(false); }

	public void setCoast() { setNeutralMode(NeutralMode.Coast); }
	public void setBrake() { setNeutralMode(NeutralMode.Brake); }
	
	public int getSensorUnitsPerRotation() { return sensorUnitsPerRotation; }
	public void setSensorUnitsPerRotation(int sensorUnitsPerRotation) { this.sensorUnitsPerRotation = sensorUnitsPerRotation; }

	public double getDistancePerRotation() { return distancePerRotation; }
	public void setDistancePerRotation(double distancePerRotation) { this.distancePerRotation = distancePerRotation; }
	
	public FeedbackDevice getFeedbackDevice() { return feedbackDevice; }
	public void setFeedbackDevice(int value) { setFeedbackDevice(getFeedbackDevice(value)); }
	public void setFeedbackDevice(FeedbackDevice feedbackDevice) {
		this.feedbackDevice = feedbackDevice;
		configSelectedFeedbackSensor(feedbackDevice, kPidIdx, kTimeoutMs);
	}
	
	public ControlMode getControlMode() { return controlMode; }
	public void setControlMode(int value) { setControlMode(getControlMode(value)); }
	public void setControlMode(ControlMode controlMode) { this.controlMode = controlMode; }
	
	public double getVelocityRPM() { 
		if(sensorUnitsPerRotation != 0) {
			if(feedbackDevice == FeedbackDevice.QuadEncoder)
				return nativeToQuadRPM(getNativeVelocity(), sensorUnitsPerRotation);
			else 
				return nativeToRPM(getNativeVelocity(), sensorUnitsPerRotation);
		}
		return 0;
	}
	
	public double getLinearVelocity() {
		if(distancePerRotation != 0) 
			return rpmToLinearVelocity(getVelocityRPM(), distancePerRotation);
		return 0;
	}
	
	public double getPositionRotations() { 
		if(sensorUnitsPerRotation != 0) {
			if(feedbackDevice == FeedbackDevice.QuadEncoder) 
				return nativeToQuadRotations(getNativePosition(), sensorUnitsPerRotation);
			return nativeToRotations(getNativePosition(), sensorUnitsPerRotation);
		}
		return 0;
	}
	
	public double getPositionLinearDistance() {
		if(distancePerRotation != 0) 
			return rotationsToDistance(getPositionRotations(), distancePerRotation);
		return 0;
	}
	
	public double getNativeVelocity() { return getSelectedSensorVelocity(kPidIdx); }
	public double getNativePosition() { return getSelectedSensorPosition(kPidIdx); }
	
	public int getAnalogInNativeVelocity() { return getSensorCollection().getAnalogInVel(); }
	public double getAnalogInRPMVelocity() { return nativeToRPM(getAnalogInNativeVelocity(), 1024); }
	
	public int getAnalogInNativePosition() { return getSensorCollection().getAnalogIn(); }
	public double getAnalogInRotationPosition() { return nativeToRotations(getAnalogInNativePosition(), 4096); }
	
	public void setSensorPosition(int sensorPos) { setSelectedSensorPosition(sensorPos, kPidIdx, kTimeoutMs); }
}