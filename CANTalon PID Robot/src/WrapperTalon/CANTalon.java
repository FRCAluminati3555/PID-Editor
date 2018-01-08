package WrapperTalon;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Wrapper class of the 2018 WPI_TalonSRX that more resembles the 2017 CANTalon, but is also more convenient for the PID Editor 
 * @author Sam
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
	
	public static double RPMToNative(double rpm, int sensorUnitsPerRotation) {
		return rpm * (sensorUnitsPerRotation / 600.0);
	}
	
	private boolean enabled;
	private double p, i, d, f;
	private double setPoint;
	
	private FeedbackDevice feedbackDevice;
	private ControlMode controlMode;

	private int sensorUnitsPerRotation;
	
	public CANTalon(int deviceNumber) {
		super(deviceNumber);
		
		setControlMode(ControlMode.PercentOutput);
	}
	
	public CANTalon(String name, int deviceNumber) {
		this(deviceNumber);
		
		setName("Talon SRX #", deviceNumber + ", " + name);
	}

	/**
	 * @param speed - speed to set -> speed = RPM, Percent = +- 1  
	 */
	public void set(double setPoint) {
		if(enabled) {
			if(controlMode == ControlMode.Velocity) 
				super.set(controlMode, setPoint * (sensorUnitsPerRotation / 600));
			super.set(controlMode, setPoint);
		}
	}
	public double getSetPoint() { return setPoint; }
	
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

	public void setCoast() { setNeutralMode(NeutralMode.Coast); }
	public void setBrake() { setNeutralMode(NeutralMode.Brake); }
	
	public int getSensorUnitsPerRotation() { return sensorUnitsPerRotation; }
	public void setSensorUnitsPerRotation(int sensorUnitsPerRotation) { this.sensorUnitsPerRotation = sensorUnitsPerRotation; }

	public FeedbackDevice getFeedbackDevice() { return feedbackDevice; }
	public void setFeedbackDevice(int value) { setFeedbackDevice(getFeedbackDevice(value)); }
	public void setFeedbackDevice(FeedbackDevice feedbackDevice) {
		this.feedbackDevice = feedbackDevice;
		configSelectedFeedbackSensor(feedbackDevice, kPidIdx, kTimeoutMs);
	}
	
	public ControlMode getControlMode() { return controlMode; }
	public void setControlMode(int value) { setControlMode(getControlMode(value)); }
	public void setControlMode(ControlMode controlMode) { this.controlMode = controlMode; }
	
	public double getVelocityRPM() { return getSelectedSensorVelocity(kPidIdx) * (600.0 / sensorUnitsPerRotation); }
	public double getPositionRotations() { return getSelectedSensorPosition(kPidIdx) * (1.0 / sensorUnitsPerRotation); }
	
	public double getNativeVelocity() { return getSelectedSensorVelocity(kPidIdx); }
	public double getNetivePosition() { return getSelectedSensorPosition(kPidIdx); }
	
	public void setSensorPosition(int sensorPos) { setSelectedSensorPosition(sensorPos, kPidIdx, kTimeoutMs); }
}