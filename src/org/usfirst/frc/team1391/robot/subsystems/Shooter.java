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
	Victor feeder = new Victor(RobotMap.feedMotor);
	Encoder encoder = new Encoder(RobotMap.shooterEncoder[0], RobotMap.shooterEncoder[1]);

    public void initDefaultCommand() {
      
    }
    
    public void shoot(double shootSpeed){
    	
    	shooter.set(shootSpeed);
    	
    }
    
    public void stop(){
    	
    	feeder.set(0);
    	
    }
    
    public void feed(){
    	
    	feeder.set(-1);
    	
    }
    
}

