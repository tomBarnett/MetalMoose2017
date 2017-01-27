package org.usfirst.frc.team1391.robot.subsystems;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import org.usfirst.frc.team1391.robot.RobotMap;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import com.kauailabs.navx.frc.AHRS;

/**
 *
 */
public class DriveBase extends PIDSubsystem {

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
	
	//Encoder encoderLeftF = new Encoder(RobotMap.encoderLeftF[0], RobotMap.encoderLeftF[0], false, Encoder.EncodingType.k4X);
	
	AHRS ahrs;
	
	public static double gyroP = 0.03;
    public static double gyroI = 0.00;
    public static double gyroD = 0.00;
    //double kF = 0.00;
    
    double kToleranceDegrees = 2.0;
    
    // Initialize your subsystem here
    public DriveBase() {
    	super(gyroP, gyroI, gyroD);
        
    	getPIDController().setInputRange(-180.0f,  180.0f);
        getPIDController().setOutputRange(-1.0, 1.0);
        getPIDController().setAbsoluteTolerance(kToleranceDegrees);
        getPIDController().setContinuous(true);
    	
        ahrs = new AHRS(SPI.Port.kMXP);
        
        //encoderLeftF.reset();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void mecanumDrive(double xIn, double yIn, double rotation){
    	
    	setLeftFSpeed(xIn-yIn+rotation);
    	setLeftBSpeed(xIn-yIn-rotation);
    	setRightFSpeed(xIn+yIn+rotation);
    	setRightBSpeed(xIn+yIn-rotation);    	
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
    
    // Set the PID controller to get input from the gyro
    public void setGyroPIDControl(double setpoint){
    	
    	// set PID input from gyro
    	// set PID setpoint
    	// set PID values for gyro
    	// set PID output
    }
    
    // Set the PID controller to get input from the encoders
    public void setEncoderPIDControl(double setpoint) {
    	
    	// set PID input from encoder
    	// set PID setpoint
    	// set PID values for encoder
    	// set PID output
    }
    
    protected double returnPIDInput() {
        return ahrs.getAngle();
    }

    protected void usePIDOutput(double output) {
        
    }
}
