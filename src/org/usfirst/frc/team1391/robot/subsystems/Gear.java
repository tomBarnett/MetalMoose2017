package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.Robot;
import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Gear extends Subsystem {

	Victor gearGate = new Victor(RobotMap.gearGate);

	public boolean active = false;
	boolean rightAngle = false;
	double setAngle = 0;
	boolean centered = false;
	boolean drivingForward = false;
	boolean pullingGateUp = false;
	boolean drivingBackward = false;

	int count = 0;
	int dropDistance = 50;
	int safeDistance = 100;

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void open() {
		gearGate.set(.3);
	}

	public void close() {
		gearGate.set(-.3);
	}

	public void stop() {
		gearGate.set(0);
	}

	public void sequence(){
    	
    	if(active){
    		
    		if(!rightAngle){
    			
    			Robot.driveBase.setGyroPIDControl(0);
    			
    			if(Robot.driveBase.ahrs.getYaw() <= (setAngle + 3) && Robot.driveBase.ahrs.getYaw() >= (setAngle - 3)){
    				Robot.driveBase.setNoPid();
    				rightAngle = true;
    			}
    			
    		}else if(!centered){
    			
    			if(SmartDashboard.getNumber("angle", 10) < 0){ //LEFT, needs to pull from vision
    				Robot.driveBase.mecanumDrive(.5, 0, 0);
    			}else{ //RIGHT
    				Robot.driveBase.mecanumDrive(-.5, 0, 0);
    			}
    			
    			drivingForward = true;
    			
    			if(SmartDashboard.getNumber("angle", 10) < -3 && SmartDashboard.getNumber("angle", 10) > -3){
    				centered = true;
    			}
    			
    		}else if(drivingForward){
    			Robot.driveBase.mecanumDrive(0, .5, 0);
    			
    			if(SmartDashboard.getNumber("dist", dropDistance) < dropDistance){
    				drivingForward = false;
    				pullingGateUp = true;
    			}
    			
    		}else if(pullingGateUp){
    			open();
    			count++;
    			
    			if(count == 10){
    				pullingGateUp = false;
    				drivingBackward = true;
    			}
    			
    		}else if(drivingBackward){
    			Robot.driveBase.mecanumDrive(0, -.5, 0);
    		}else{
    			sequenceEject();
    		}
    		
    	}
    	
    }

	public void sequenceEject() {

		count = 0;
		rightAngle = false;
		active = false;
		centered = false;
		drivingForward = false;
		pullingGateUp = false;
		drivingBackward = false;

	}

}
