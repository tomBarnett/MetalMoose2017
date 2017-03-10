package org.usfirst.frc.team1391.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team1391.robot.commands.*;
import org.usfirst.frc.team1391.robot.triggers.JoystickTrigger;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	public static Joystick driver = new Joystick(0);
	public static Joystick operator = new Joystick(1);
	
	public static Button driverX = new JoystickButton (driver, 1);
	public static Button driverA = new JoystickButton (driver, 2);
	public static Button driverB = new JoystickButton (driver, 3);
	public static Button driverY = new JoystickButton (driver, 4);
	
	public static Button driverLB = new JoystickButton (driver, 5);
	public static Button driverRB = new JoystickButton (driver, 6);
	public static Button driverLT = new JoystickButton (driver, 7);
	public static Button driverRT = new JoystickButton (driver, 8);
	
	public static Button driverJoyL = new JoystickButton (driver, 11);
	public static Button driverJoyR = new JoystickButton (driver, 12);
	
	public static Button operatorX = new JoystickButton (operator, 1);
	public static Button operatorA = new JoystickButton (operator, 2);
	public static Button operatorB = new JoystickButton (operator, 3);
	public static Button operatorY = new JoystickButton (operator, 4);
	
	public static Button operatorLB = new JoystickButton (operator, 5);
	public static Button operatorRB = new JoystickButton (operator, 6);
	public static Button operatorLT = new JoystickButton (operator, 7);
	public static Button operatorRT = new JoystickButton (operator, 8);
	
	public static Button operatorJoyL = new JoystickButton (operator, 11);
	public static Button operatorJoyR = new JoystickButton (operator, 12);
	
	//DRIVE CODE
	
	
	
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	
	OI (){
		
	}
	
}
