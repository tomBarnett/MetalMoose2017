package org.usfirst.frc.team1391.robot.commands;

import org.usfirst.frc.team1391.robot.OI;
import org.usfirst.frc.team1391.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GryoRight extends Command {

    public GryoRight() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	Robot.driveBase.setGyroPIDControl(90);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	// Stop sometime
    	
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveBase.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.driveBase.stop();
    }
}
