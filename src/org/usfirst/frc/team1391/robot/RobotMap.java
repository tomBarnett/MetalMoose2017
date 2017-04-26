package org.usfirst.frc.team1391.robot;


/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	public static int leftF = 3;
	public static int leftB = 2;
	public static int rightF = 0;
	public static int rightB = 1;
	
	public static int shooterMotor = 8;
	public static int feedMotor = 4;
	
	public static int intakeMotor = 5;
	public static int liftMotor = 9;
	
	public static int hookMotorL = 6;
	public static int hookMotorR = 7; 

	public static int shooterEncoder[] = {0, 1};
	
	public static int solDrive[] = {0, 1};
	public static int solHopperDropper[] = {2, 3};
	public static int solGear[] = {6, 7};
	
	public static int gearLimit = 9;
	
}
