package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

    Victor intakeMotor = new Victor(RobotMap.intakeMotor);
    DoubleSolenoid solIntake = new DoubleSolenoid(RobotMap.solIntake[0], RobotMap.solIntake[1]);


    public void initDefaultCommand() {
        
    }
    
    public void start(){
    	intakeMotor.set(1);
    }
    
    public void stop(){
    	intakeMotor.set(0);
    }
    
    public void hingeOpen(){
		solIntake.set(DoubleSolenoid.Value.kForward);
	}
	
	public void hingeClose(){
		solIntake.set(DoubleSolenoid.Value.kReverse);
	}
}

