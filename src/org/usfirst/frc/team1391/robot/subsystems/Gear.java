package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Gear extends Subsystem {

	Victor gearGate = new Victor(RobotMap.gearGate);

	public boolean active = false;
	boolean centered = false;
	boolean drivingForward = false;
	boolean pullingGateUp = false;
	boolean drivingBackward = false;

	int count = 0;

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
    	
    		if(!centered){
    			
    		}
    		
    	}
    	
    }

	public void sequenceEject() {

		active = false;
		centered = false;
		drivingForward = false;
		pullingGateUp = false;
		drivingBackward = false;

	}

}
