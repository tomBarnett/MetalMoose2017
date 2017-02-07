package org.usfirst.frc.team1391.robot.commands;


import org.usfirst.frc.team1391.robot.OI;
import org.usfirst.frc.team1391.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *	This Command allows the driver to turn the robot 90 degrees to the field orientation, assuming the NavX is reset at the beginning of the race.
 *	
 *	<p><b>Note:</b> the driver will not have the control of the robot until the robot is correctly oriented to 90 degrees to the field orientation.
 */
public class GyroRight extends Command {

    public GyroRight() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveBase);
        this.setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {   
    	
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
    	
    	Robot.driveBase.setGyroPIDControl(90);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	// Set finished when PID reaches its target
    	if (Robot.driveBase.getPIDController().onTarget()) {
    		Robot.driveBase.setNoPid();
    		return true;
    	}
    	
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	System.out.println("GyroInterruted");
    	Robot.driveBase.setNoPid();
    	end();
    }
}
