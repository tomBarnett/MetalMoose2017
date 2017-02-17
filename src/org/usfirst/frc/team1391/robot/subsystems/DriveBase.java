package org.usfirst.frc.team1391.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import org.usfirst.frc.team1391.robot.RobotMap;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.kauailabs.navx.frc.AHRS;

/**
 *
 */

public class DriveBase extends PIDSubsystem {

	// Flag for PID controller input type

	public enum PIDInput {
		NoPIDInput, EncoderPIDInput, GyroPIDInput, VisionPIDInput
	}

	PIDInput currentInputType;

	// Motors driving front-left mecanum wheel
	Victor leftFA = new Victor(RobotMap.leftFA);
	Victor leftFB = new Victor(RobotMap.leftFB);

	// Motors driving back-left mecanum wheel
	Victor leftBA = new Victor(RobotMap.leftBA);
	Victor leftBB = new Victor(RobotMap.leftBB);

	// Motors driving front-right mecanum wheel
	Victor rightFA = new Victor(RobotMap.rightFA);
	Victor rightFB = new Victor(RobotMap.rightFB);

	// Motors driving back-right mecanum wheel
	Victor rightBA = new Victor(RobotMap.rightBA);
	Victor rightBB = new Victor(RobotMap.rightBB);

	//SOLENOIDS
	DoubleSolenoid solRightF = new DoubleSolenoid(RobotMap.solRightF[0], RobotMap.solRightF[1]);
	DoubleSolenoid solRightB = new DoubleSolenoid(RobotMap.solRightB[0], RobotMap.solRightB[1]);
	DoubleSolenoid solLeftF = new DoubleSolenoid(RobotMap.solLeftF[0], RobotMap.solLeftF[1]);
	DoubleSolenoid solLeftB = new DoubleSolenoid(RobotMap.solLeftB[0], RobotMap.solLeftB[1]);
	
	// Encoder encoderLeftF = new Encoder(RobotMap.encoderLeftF[0],
	// RobotMap.encoderLeftF[0], false, Encoder.EncodingType.k4X);

	AHRS ahrs;

	// PID control variables
	public static double gyroP = 0.045;
	public static double gyroI = 0.003;
	public static double gyroD = 0.00;
	// double kF = 0.00;
	public int first = 1;

	double kToleranceDegrees = 2.0;

	// Initialize your subsystem here
	public DriveBase() {
		super(0, 0, 0);

		getPIDController().setOutputRange(-1.0, 1.0);

		ahrs = new AHRS(SPI.Port.kMXP);

		
		// encoderLeftF.reset();
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void mecanumDrive(double xIn, double yIn, double rotation) {

		setLeftFSpeed(xIn - yIn + rotation);
		setLeftBSpeed(xIn - yIn - rotation);
		setRightFSpeed(xIn + yIn + rotation);
		setRightBSpeed(xIn + yIn - rotation);
	}

	public void setToLowGear(){
		
		solRightF.set(DoubleSolenoid.Value.kForward);
		solRightB.set(DoubleSolenoid.Value.kForward);
		solLeftF.set(DoubleSolenoid.Value.kForward);
		solLeftB.set(DoubleSolenoid.Value.kForward);
		
	}
	
	public void setToHighGear(){
		
		solRightF.set(DoubleSolenoid.Value.kReverse);
		solRightB.set(DoubleSolenoid.Value.kReverse);
		solLeftF.set(DoubleSolenoid.Value.kReverse);
		solLeftB.set(DoubleSolenoid.Value.kReverse);
		
	}
	
	public void motorTest() {
		leftFA.set(.5);
		leftFB.set(.5);
	}

	private void setLeftFSpeed(double speed) {
		leftFA.setSpeed(speed);
		leftFB.setSpeed(speed);
	}

	private void setLeftBSpeed(double speed) {
		leftBA.setSpeed(speed);
		leftBA.setSpeed(speed);
	}

	private void setRightFSpeed(double speed) {
		rightFA.setSpeed(speed);
		rightFB.setSpeed(speed);
	}

	private void setRightBSpeed(double speed) {
		rightBA.setSpeed(speed);
		rightBB.setSpeed(speed);
	}

	public void stop() {
		setLeftFSpeed(0);
		setLeftBSpeed(0);
		setRightFSpeed(0);
		setRightBSpeed(0);
	}
	
	public double getAngle(){
		if(first == 1){
			first = 0;
			SmartDashboard.putNumber("gyroP", gyroP);
			SmartDashboard.putNumber("gyroI", gyroI);
			SmartDashboard.putNumber("gyroD", gyroD);
		}
		gyroP = SmartDashboard.getNumber("gyroP", gyroP);
		gyroI = SmartDashboard.getNumber("gyroI", gyroI);
		gyroD = SmartDashboard.getNumber("gyroD", gyroD);
		getPIDController().setPID(gyroP, gyroI, gyroD);
		SmartDashboard.putNumber("YAW", ahrs.getYaw());
		return ahrs.getYaw();
		
	}

	// Set the PID controller to get input from the gyro
	public void setGyroPIDControl(double setpoint) {

		if (currentInputType != PIDInput.GyroPIDInput) {
			getPIDController().setInputRange(-180.0, 180.0);
			getPIDController().setAbsoluteTolerance(kToleranceDegrees);
			// set PID input from gyro
			currentInputType = PIDInput.GyroPIDInput;
			// set PID setpoint

			// set PID values for gyro
			getPIDController().setPID(gyroP, gyroI, gyroD);
			// set PID continues
			getPIDController().setContinuous(true);
			getPIDController().enable();
		}
		

			getPIDController().setSetpoint(setpoint);
		

		SmartDashboard.putNumber("YAW", ahrs.getYaw());

		if (getPIDController().onTarget()) {
			setNoPid();
		}

	}

	public void setVisionPIDControl(double setpoint){
		
		if (currentInputType != PIDInput.VisionPIDInput) {
			getPIDController().setInputRange(-180.0, 180.0);
			getPIDController().setAbsoluteTolerance(kToleranceDegrees);
			// set PID input from gyro
			currentInputType = PIDInput.VisionPIDInput;
			// set PID setpoint

			// set PID values for gyro
			getPIDController().setPID(gyroP, gyroI, gyroD);
			// set PID continues
			getPIDController().setContinuous(true);
			getPIDController().enable();
		}
		
		if(setpoint != getPIDController().getSetpoint()){
			getPIDController().setSetpoint(setpoint);
		}

		SmartDashboard.putNumber("YAW", ahrs.getYaw());

		if (getPIDController().onTarget()) {
			setNoPid();
		}
		
	}
	
	// Set the PID controller to get input from the encoders
	public void setEncoderPIDControl(double setpoint) {

		// set PID input from encoder
		// set PID setpoint
		// set PID values for encoder
		// set PID output
	}

	public void setNoPid() {
		currentInputType = PIDInput.NoPIDInput;
		getPIDController().disable();
	}
	
	protected double returnPIDInput() {

		switch (currentInputType) {
		case NoPIDInput:
			return 0;
		case EncoderPIDInput:

			// Put Encoder input

			return 0;
		case GyroPIDInput:
			// Use the Yaw values provided by NavX interface
			return ahrs.getYaw();
		
		case VisionPIDInput:
			return SmartDashboard.getNumber("angle", 0);
		
		default:
			return 0;
		}
	}

	protected void usePIDOutput(double output) {

		switch (currentInputType) {
		case NoPIDInput:
			break;
		case EncoderPIDInput:
			break;
		case GyroPIDInput:
			mecanumDrive(output, 0, 0);
			System.out.println("002");
			break;
		case VisionPIDInput:
			mecanumDrive(output, 0, 0);
		default:
			break;
		}
	}
}
