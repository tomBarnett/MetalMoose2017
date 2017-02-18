package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BallCollector extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	Victor collectorMotor = new Victor(RobotMap.collectorMotor);
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void startCollector() {
		collectorMotor.setSpeed(1);
	}
    
    public void stopCollector() {
    	collectorMotor.setSpeed(0);
    }
}

