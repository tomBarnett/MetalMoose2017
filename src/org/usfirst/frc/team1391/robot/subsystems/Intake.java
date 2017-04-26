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
	//DigitalInput gearLimit = new DigitalInput(RobotMap.gearLimit);

	// DoubleSolenoid solIntake = new DoubleSolenoid(RobotMap.solIntake[0],
	// RobotMap.solIntake[1]);

	int liftTimer = 0;
	int outtakeTimer = 0;

	public void initDefaultCommand() {
	}

	public void intake() {
		
		intakeMotor.set(-.65);

	}

	public void outtake() {
		intakeMotor.set(.5);
	}

	public void stop() {
		intakeMotor.set(0);
	}

	public void lift(double liftSpeed) {
		liftMotor.set(liftSpeed);
	}

	public void liftMethod() {

		if (liftTimer < 25) {
			intakeMotor.set(0);
			lift(-.5);
		} else {
			lift(-.2);
		}
	}
	
	public void gearCheck(){
		
		/*
		if (!gearLimit.get()){
			liftMethod();
		}
		*/
		
	}

	public void lower(double liftSpeed) {

		liftMotor.set(liftSpeed);
		liftTimer = 0;

	}

	public void liftStop() {

		liftTimer = 0;
		liftMotor.set(0);

	}

	public void outtakeMethod() {

		outtake();

		lower(.5);

	}
	
	public void intakeHold(){
		
		intakeMotor.set(-.2);
		
	}

	public boolean limitCheck() {
		//return gearLimit.get();
		return true;
	}

	public void hingeOpen() {
		// solIntake.set(DoubleSolenoid.Value.kForward);
	}

	public void hingeClose() {
		// solIntake.set(DoubleSolenoid.Value.kReverse);
	}
}
