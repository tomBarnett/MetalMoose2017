package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {

	Victor shooter = new Victor(RobotMap.shooterMotor);
	Victor feeder = new Victor(RobotMap.feederMotor);
	
	double defaultShooterSpeed = 1.0;
	double defaultFeederSpeed = 1.0;

    public void initDefaultCommand() {
      
    }
    
    protected void setShooterSpeed(double shootSpeed){
    	shooter.set(shootSpeed);   	
    }
    
    protected void setFeederSpeed(double feederSpeed) {
    	feeder.set(feederSpeed);
    }
    
    public void startShooting() {
    	// Use default shooter speed and default feeder speed
    	setShooterSpeed(defaultShooterSpeed);
    	setFeederSpeed(defaultFeederSpeed);
    }
    
    public void startShootingWithShooterSpeed(double shooterSpeed) {
    	setShooterSpeed(shooterSpeed);
    	setFeederSpeed(defaultFeederSpeed);
    }
}

