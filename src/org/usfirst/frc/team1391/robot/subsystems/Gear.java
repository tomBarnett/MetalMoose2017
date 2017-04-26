package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.Robot;
import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Gear extends Subsystem {

	public boolean active = false;
	boolean rightAngle = false;
	double setAngle = 30;
	boolean centered = false;
	boolean drivingForward = false;
	boolean pullingGateUp = false;
	boolean drivingBackward = false;

	int count = 0;
	int dropDistance = 50;
	int safeDistance = 100;
	
	double rightGearAngle = 30;
	
	int distanceAuto = 100;
	
	DoubleSolenoid solGear = new DoubleSolenoid(RobotMap.solGear[0], RobotMap.solGear[1]);
	DoubleSolenoid solHopperDropper = new DoubleSolenoid(RobotMap.solHopperDropper[0], RobotMap.solHopperDropper[1]);

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
	
	public void open(){
		solGear.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void close(){
		solGear.set(DoubleSolenoid.Value.kForward);
	}
	
	public void hopperOpen(){
		solHopperDropper.set(DoubleSolenoid.Value.kForward);
	}
	
	public void hopperClose(){
		solHopperDropper.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void sequenceStart(){
		//active = true; 
		
		sequenceTwo();
		
		//SmartDashboard.putBoolean("visionTarget", false);
	}

	public void sequence(){
    	
		
    	if(active){
    		System.out.println("active");
    		if(!rightAngle){
    			System.out.println("angle");
    			Robot.driveBase.setGyroPIDControl(rightGearAngle);
    			
    			if(Robot.driveBase.ahrs.getYaw() <= (rightGearAngle + 3) && Robot.driveBase.ahrs.getYaw() >= (rightGearAngle - 3)){
    				Robot.driveBase.setNoPid();
    				rightAngle = true;
    			}
    			
    		}else if(!centered){
    			System.out.println("center");
    			if(SmartDashboard.getNumber("angle", 10) < 0){ //LEFT, needs to pull from vision
    				Robot.driveBase.mecanumDrive(0, .4, 0);
    			}else{ //RIGHT
    				Robot.driveBase.mecanumDrive(0, -.4, 0);
    			}
    			
    			drivingForward = true;
    			if(SmartDashboard.getBoolean("targetInFrame", true)){
    				if(SmartDashboard.getNumber("angle", 10) < 5 && SmartDashboard.getNumber("angle", 10) > 5){
    					centered = true;
    				}
    			}
    			
    		}else if(drivingForward){
    			Robot.driveBase.mecanumDrive(0, 0, -.5);
    			System.out.println("forward");
    			count++;
    			
    			if(count > 100){
    				drivingForward = false;
    				pullingGateUp = true;
    			}
    			/*
    			if(SmartDashboard.getNumber("dist", dropDistance) < dropDistance){
    				drivingForward = false;
    				pullingGateUp = true;
    			}
    			*/
    			
    		}else if(pullingGateUp){
    			System.out.println("gateup");
    			//open();
    			count++;
    			
    			if(count > 150){
    				pullingGateUp = false;
    				drivingBackward = true;
    			}
    			
    		}else if(drivingBackward){
    			System.out.println("drivingBack");
    			Robot.driveBase.mecanumDrive(0, 0, .5);
    		}else{
    			sequenceEject();
    		}
    		
    	}
    	
    }

	public void sequenceTwo(){
		count++;
		
		distanceAuto = (int) SmartDashboard.getNumber("distanceAuto", distanceAuto);
		
		if(count < 75){
			Robot.driveBase.gyroMecanumDrive(0, 0 , -.7, 0);
		}
	}
	
	
	public void sequenceEject() {

		SmartDashboard.putNumber("distanceAuto", distanceAuto);
		
		count = 0;
		rightAngle = false;
		active = false;
		centered = false;
		drivingForward = false;
		pullingGateUp = false;
		drivingBackward = false;

	}

}
