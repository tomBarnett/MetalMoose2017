package org.usfirst.frc.team1391.robot.commands;

import org.usfirst.frc.team1391.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class GyroVision extends Command {

    public GyroVision() {
       requires(Robot.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
    	double gyroAngle = Robot.driveBase.getAngle(); //angle of gyro
    	double visionAngle = SmartDashboard.getNumber("angle", gyroAngle); //angle of displacement from camera
    	
    	Robot.driveBase.setGyroPIDControl(gyroAngle - visionAngle); //feeds it into a pid loop to posotion
    																//uses gyro to get around lag of vision
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
