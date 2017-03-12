package org.usfirst.frc.team1391.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
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
	Victor leftF = new Victor(RobotMap.leftF);

	// Motors driving back-left mecanum wheel
	Victor leftB = new Victor(RobotMap.leftB);

	// Motors driving front-right mecanum wheel
	Victor rightF = new Victor(RobotMap.rightF);

	// Motors driving back-right mecanum wheel
	Victor rightB = new Victor(RobotMap.rightB);

	//SOLENOIDS
	
	Compressor c = new Compressor(0);
	
	DoubleSolenoid solDrive = new DoubleSolenoid(RobotMap.solDrive[0], RobotMap.solDrive[1]);
	
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
	double gyroGain = 1.2;

	// Initialize your subsystem here
	public DriveBase() {
		super(0, 0, 0);

		getPIDController().setOutputRange(-1.0, 1.0);

		ahrs = new AHRS(SPI.Port.kMXP);
		c.setClosedLoopControl(true);

		
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
	
	public void mecanumTeloDrive(double xIn, double yIn, double rotation){
		
		setLeftFSpeed(xIn + yIn + rotation);
		setLeftBSpeed(xIn + yIn - rotation);
		setRightFSpeed(xIn - yIn + rotation);
		setRightBSpeed(xIn - yIn - rotation);
		
	}
	
	public void gyroMecanumDrive(double xIn, double yIn, double rotation, double targetAngle){
		
		gyroGain = SmartDashboard.getNumber("gyroGain", gyroGain);
		
		xIn = xIn + gyroGain*((targetAngle - ahrs.getYaw())/180);
		
		
		setLeftFSpeed(xIn - yIn + rotation);
		setLeftBSpeed(xIn - yIn - rotation);
		setRightFSpeed(xIn + yIn + rotation );
		setRightBSpeed(xIn + yIn - rotation);
		
		SmartDashboard.putNumber("YAW", ahrs.getYaw());
		
	}
	
	public void lowGear(){
		
		solDrive.set(DoubleSolenoid.Value.kReverse);
		
	}
	
	public void highGear(){
		
		solDrive.set(DoubleSolenoid.Value.kForward);
		
	}
	
	public void motorTest() {
		leftF.set(.5);
	}
	
	public void resetGyro() {
		ahrs.reset();
	}

	private void setLeftFSpeed(double speed) {
		leftF.setSpeed(speed);
	}

	private void setLeftBSpeed(double speed) {
		leftB.setSpeed(speed);
	}

	private void setRightFSpeed(double speed) {
		rightF.setSpeed(speed);
	}

	private void setRightBSpeed(double speed) {
		rightB.setSpeed(speed);
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
