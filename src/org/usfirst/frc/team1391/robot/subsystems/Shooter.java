package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {

	Victor shooter = new Victor(RobotMap.shooterMotor);

    public void initDefaultCommand() {
      
    }
    
    public void shoot(float shooterSpeed){
    	
    	shooter.set(shooterSpeed);
    	
    }
    
}

