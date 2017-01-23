package org.usfirst.frc.team1391.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import org.usfirst.frc.team1391.robot.RobotMap;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 *
 */
public class DriveBase extends PIDSubsystem {

	Victor leftF = new Victor(RobotMap.leftF);
	Victor leftB = new Victor(RobotMap.leftB);
	Victor rightF = new Victor(RobotMap.rightF);
	Victor rightB = new Victor(RobotMap.rightB);
	
	//Encoder encoderLeftF = new Encoder(RobotMap.encoderLeftF[0], RobotMap.encoderLeftF[0], false, Encoder.EncodingType.k4X);
	
    // Initialize your subsystem here
    public DriveBase() {
    	super(0, 0, 0);
        leftF.setInverted(true);
        leftB.setInverted(true);
        disable(); 
        //encoderLeftF.reset();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void mecanumDrive(double xIn, double yIn, double rotation){
    	
    	leftF.setSpeed((xIn+yIn+rotation));
    	leftB.setSpeed((-xIn+yIn-rotation));
    	rightF.setSpeed((-xIn+yIn+rotation));
    	rightB.setSpeed((xIn+yIn-rotation));
    	
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
