package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Hanger extends Subsystem {

    Victor hookMotorL = new Victor(RobotMap.hookMotorL);
    Victor hookMotorR = new Victor(RobotMap.hookMotorR);
    

    public void initDefaultCommand() {
       
    }
    
    public void lift(){
    	hookMotorL.set(-1);
    	hookMotorR.set(-1);
    	
    	
    }
    
    public void halt(){
    	hookMotorL.set(0);
    	hookMotorR.set(0);
    }
}

