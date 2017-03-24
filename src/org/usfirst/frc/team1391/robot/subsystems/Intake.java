package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.Robot;
import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {

    Spark intakeMotor = new Spark(RobotMap.intakeMotor);
    Victor liftMotor = new Victor(RobotMap.liftMotor);
    DigitalInput gearLimit = new DigitalInput(RobotMap.gearLimit);
    
    //DoubleSolenoid solIntake = new DoubleSolenoid(RobotMap.solIntake[0], RobotMap.solIntake[1]);
    
    int liftTimer = 0; 
    int outtakeTimer = 0;

    public void initDefaultCommand() {
    }
    
    public void intake(){
    	if(gearLimit.get()){
    		intakeMotor.set(-1);
    	}else{
    		intakeMotor.set(0);
    	}
    	
    }
    
    public void outtake(){
    	intakeMotor.set(1);
    }
    
    public void stop(){
    	intakeMotor.set(0);
    }
    
    public void lift(double liftSpeed){
    	liftMotor.set(liftSpeed);
    }
    
    public void liftMethod(){
    	
    	if(!gearLimit.get()){
    		if(liftTimer < 25){
    			intakeMotor.set(0);
    			lift(-.5);
    		}else{
    			lift(-.2);
    		}
    	}
    }
    
    public void lower(double liftSpeed){
    	
    	liftMotor.set(liftSpeed);
    	liftTimer = 0;
    	
    }
    
    public void liftStop(){
    	
    	liftTimer = 0;
    	liftMotor.set(0);
    	
    }
    
    public void outtakeMethod(){
    	
    	outtake();
    	
    	if(outtakeTimer < 25){
    		Robot.driveBase.mecanumDrive(0, 0, .1);
    		lower(.1);
    	}else{
    		Robot.driveBase.mecanumDrive(0, 0, .4);
    		lower(.35);
    	}
    	
    }
    
    public boolean limitCheck(){
    	return gearLimit.get();
    }
    
    
    public void hingeOpen(){
		//solIntake.set(DoubleSolenoid.Value.kForward);
	}
	
	public void hingeClose(){
		//solIntake.set(DoubleSolenoid.Value.kReverse);
	}
}

