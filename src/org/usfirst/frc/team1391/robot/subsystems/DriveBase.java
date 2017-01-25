package org.usfirst.frc.team1391.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import org.usfirst.frc.team1391.robot.RobotMap;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 *
 */
public class DriveBase extends PIDSubsystem {

	Victor leftFA = new Victor(RobotMap.leftFA);
	Victor leftFB = new Victor(RobotMap.leftFB);
	Victor leftBA = new Victor(RobotMap.leftBA);
	Victor leftBB = new Victor(RobotMap.leftBB);
	Victor rightFA = new Victor(RobotMap.rightFA);
	Victor rightFB = new Victor(RobotMap.rightFB);
	Victor rightBA = new Victor(RobotMap.rightBA);
	Victor rightBB = new Victor(RobotMap.rightBB);
	
	//Encoder encoderLeftF = new Encoder(RobotMap.encoderLeftF[0], RobotMap.encoderLeftF[0], false, Encoder.EncodingType.k4X);
	
    // Initialize your subsystem here
    public DriveBase() {
    	super(0, 0, 0);
        leftFA.setInverted(true);
        leftFB.setInverted(true);
        leftBA.setInverted(true);
        leftBB.setInverted(true);
        disable(); 
        //encoderLeftF.reset();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void mecanumDrive(double xIn, double yIn, double rotation){
    	
    	leftFA.setSpeed((xIn+yIn+rotation));
    	leftFB.setSpeed((xIn+yIn+rotation));
    	leftBA.setSpeed((-xIn+yIn-rotation));
    	leftBB.setSpeed((-xIn+yIn-rotation));
    	rightFA.setSpeed((-xIn+yIn+rotation));
    	rightFB.setSpeed((-xIn+yIn+rotation));
    	rightBA.setSpeed((xIn+yIn-rotation));
    	rightBB.setSpeed((xIn+yIn-rotation));
    	
    }

    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        return 0.0;
    }

    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
    }
}
