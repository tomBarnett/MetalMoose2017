package org.usfirst.frc.team1391.robot.commands;

import org.usfirst.frc.team1391.robot.OI;
import org.usfirst.frc.team1391.robot.Robot;

import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;
//import org.usfirst.frc.team1391.robot.subsystems.DriveBase;

/**
 *
 */
public class MecanumDrive extends Command {

    public MecanumDrive() {
        requires(Robot.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double xIn = OI.driver.getAxis(AxisType.kX);
    	double yIn = OI.driver.getAxis(AxisType.kThrottle);
    	double zIn = OI.driver.getAxis(AxisType.kZ);
    	
    	Robot.driveBase.mecanumDrive(xIn, yIn, zIn);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	// Stops the robor by default
    	Robot.driveBase.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
