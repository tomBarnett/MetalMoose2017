package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {

	Victor shooter = new Victor(RobotMap.shooterMotor);
	Victor feeder = new Victor(RobotMap.feederMotor);
	
	Encoder shooterEncoder = new Encoder(RobotMap.shooterEncoder[0], RobotMap.shooterEncoder[1]);
	
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
    
    public void shoot() {
    	// Use default shooter speed and default feeder speed
    	setShooterSpeed(defaultShooterSpeed);
    	setFeederSpeed(defaultFeederSpeed);
    }
    
    public void shootWithShooterSpeed(double shooterSpeed) {
    	setShooterSpeed(shooterSpeed);
    	setFeederSpeed(defaultFeederSpeed);
    }
    
    public void shootWithShooterSpeedAndFeederSpeed(double shooterSpeed, double feederSpeed) {
    	setShooterSpeed(shooterSpeed);
    	setFeederSpeed(feederSpeed);
    }
    
    public void shootWithEncoderSetRate(double setRate){
    	double baseSpeed = 0.62;
    	double protionMultiplier = 1.0;
    	double result = 0;
    	
    	double currentRate = shooterEncoder.getRate();
    	// A single PID loop with encoder as feedback to adjust motor speed to meet the desired set rate.
    	
    	shootWithShooterSpeed(result);
    }
}

