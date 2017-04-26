
package org.usfirst.frc.team1391.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1391.robot.commands.GyroRight;
import org.usfirst.frc.team1391.robot.commands.GyroStop;
import org.usfirst.frc.team1391.robot.commands.GyroVision;
import org.usfirst.frc.team1391.robot.commands.MecanumDrive;
import org.usfirst.frc.team1391.robot.subsystems.DriveBase;
import org.usfirst.frc.team1391.robot.subsystems.Gear;
import org.usfirst.frc.team1391.robot.subsystems.Hanger;
import org.usfirst.frc.team1391.robot.subsystems.Intake;
import org.usfirst.frc.team1391.robot.subsystems.Shooter;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final DriveBase driveBase = new DriveBase();
	public static final Gear gear = new Gear();
	public static final Intake intake = new Intake();
	public static final Hanger hanger = new Hanger();

	public static final Shooter shooter = new Shooter();
	public static final GyroRight gyroRight = new GyroRight();
	public static final GyroVision gyroVision = new GyroVision();
	public static final GyroStop gyroStop = new GyroStop();
	public static final MecanumDrive mecanumDrive = new MecanumDrive();
	public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	public static boolean visionFlag = true;
	public static boolean visionTarget = false; // false = gear; true = target;
	public static boolean visionSeen = false;

	public static int orientationJoy = 0;
	public static boolean orient = true;

	public static int autoTimer = 0;
	public static int delayTimer = 0;

	public static double shootSpeed = .725;
	public static int shootTimer = 0;

	public static boolean outtakeFlag = false;
	public static boolean gearLiftFlag = false;
	public static int gearLiftCount = 0;
	public static boolean headState = false; // false == down

	public static double autoChoice = 0;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		// chooser.addObject("My Auto", new MyAutoCommand());
		// SmartDashboard.putData("Auto mode", chooser);

		// CameraServer.getInstance().addAxisCamera("10.13.91.3");
		// CameraServer.getInstance().addServer("10.13.91.3");

		SmartDashboard.putBoolean("visionTarget", visionTarget);
		SmartDashboard.putNumber("gyroGain", .3);

		SmartDashboard.putNumber("autoChoice", autoChoice);

	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {

		Robot.driveBase.resetGyro();
		Robot.driveBase.lowGear();
		Robot.gear.close();
		autoChoice = SmartDashboard.getNumber("autoChoice", autoChoice);

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {

		Robot.intake.liftMethod();

		autoTimer++;

		if (autoChoice == 0) {
			autoVisRight();
		} else if (autoChoice == 1) {
			autoVisLeft();
		} else if (autoChoice == 2) {
			autoVisCenterGround();
		} else if (autoChoice == 3) {
			autoVisRightGround();
		} else if (autoChoice == 4) {
			autoVisLeftGround();
		} else if (autoChoice == 5) {
			twist();
		} else {
			centerAuto();
		}
		// autoVisRightShoot();

		// visTester();

		/*
		 * if(autoTimer < 200){ driveBase.mecanumDrive(0, .5, 0); }else
		 * if(autoTimer < 300){ driveBase.stop();
		 * driveBase.setGyroPIDControl(45); if(autoTimer == 299){
		 * driveBase.setNoPid(); }
		 * 
		 * }else if(autoTimer < 500){ driveBase.mecanumDrive(0, .5, 0); }
		 */

	}

	@Override
	public void teleopInit() {

		gear.hopperClose();
		Robot.driveBase.setNoPid();

		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {

		// DRIVING
		/*
		 * if (OI.driverLT.get() && visionFlag == false) { gyroVision.execute();
		 * } else if (OI.driverRT.get()) { gyroStop.execute(); }
		 */

		if (!driveBase.getPIDController().isEnabled()) { //&& !Robot.gear.active
			mecanumDrive.executeTelo(orient);
		}

		if (!OI.driverLT.get() && visionFlag == true) {
			visionFlag = false;
		}

		// GEAR SHIFTING
		if (OI.driverLB.get()) {
			driveBase.lowGear();
		} else if (OI.driverRB.get()) {
			driveBase.highGear();
		}

		// INTAKE
		if (OI.operatorLT.get() || OI.driverRT.get()) {
			Robot.intake.intake();
			Robot.intake.lift(.35);
			gearLiftFlag = false;
		} else if (OI.operatorRT.get() || OI.driverLT.get()) {
			outtakeFlag = true;
			Robot.intake.outtakeMethod();
			gearLiftFlag = false;
		} else if (OI.operator.getRawAxis(1) > .25) {
			Robot.intake.lift(OI.operator.getRawAxis(1));
			gearLiftFlag = false;
		} else if (OI.operator.getRawAxis(1) < -.25) {
			Robot.intake.lift(OI.operator.getRawAxis(1));
			gearLiftFlag = false;
		} else if (OI.operator.getRawAxis(0) > .4) { // ball spit
			Robot.intake.outtake();
		} else if (OI.operatorLB.get()) { // down
			headState = false;

			gearLiftFlag = false;
			Robot.intake.stop();
			Robot.intake.lift(.15);

		} else if (OI.operatorRB.get()) { // up
			headState = true;

			if (!gearLiftFlag) {
				gearLiftFlag = true;
				gearLiftCount = 0;
			}

			Robot.intake.stop();
			outtakeFlag = false;
			gearLiftCount++;

			if (gearLiftCount < 100) {
				Robot.intake.lift(-.40);
			} else {
				Robot.intake.lift(-.3);

			}
		} else { // lift state

			if (headState) {
				if (!gearLiftFlag) {
					gearLiftFlag = true;
					gearLiftCount = 0;
				}

				Robot.intake.intakeHold();
				outtakeFlag = false;
				gearLiftCount++;

				if (gearLiftCount < 100) {
					Robot.intake.lift(-.40);
				} else {
					Robot.intake.lift(-.25);

				}
			} else { // default
				gearLiftFlag = false;
				Robot.intake.stop();
				Robot.intake.lift(.15);
			}
		}

		Robot.intake.gearCheck();

		if (OI.operatorJoyL.get()) {
			Robot.intake.hingeOpen();
		} else if (OI.operatorJoyR.get()) {
			Robot.intake.hingeClose();
		}

		// FEEDER

		if (OI.operator.getRawAxis(3) > .3) {
			shootTimer++;
			Robot.shooter.shoot(shootSpeed);
			if (shootTimer > 80) {
				Robot.shooter.feed();
			}
		} else {
			Robot.shooter.stop();
			Robot.shooter.shoot(0);
			shootTimer = 0;
		}

		/*
		 * if(OI.operator.getRawAxis(1)> .3){ Robot.shooter.shoot(shootSpeed);
		 * }else{ Robot.shooter.shoot(0); }
		 */

		// HANGER
		if (OI.operator.getPOV() == 180) {
			Robot.hanger.lift();
		} else {
			Robot.hanger.halt();
		}

		// GEAR SEQUENCE
		/*
		 * if(OI.operatorX.get()){ Robot.gear.sequenceStart(); }else
		 * if(OI.operatorA.get()){ Robot.gear.sequenceEject(); }
		 */

		Robot.gear.sequence();

		if (OI.operatorA.get()) {
			Robot.gear.open();
		} else if (OI.operatorX.get()) {
			Robot.gear.close();
		}

		if (OI.operatorB.get()) {
			Robot.gear.hopperOpen();
		} else if (OI.operatorY.get()) {
			Robot.gear.hopperClose();
		}

		// SET WHAT VISION WANTS
		/*
		 * if (OI.operatorLT.get()) { // gear visionTarget = false;
		 * SmartDashboard.putBoolean("visionTarget", visionTarget); } else if
		 * (OI.operatorRT.get()) { // vision target visionTarget = true;
		 * SmartDashboard.putBoolean("visionTarget", visionTarget); }
		 */

		// SET ORIENTATION
		if (OI.driverA.get()) {
			orient = true;
		} else if (OI.driverB.get()) {
			orientationJoy = 1;
		} else if (OI.driverX.get()) {
			orientationJoy = 2;
		} else if (OI.driverY.get()) {
			orient = false;
		}

		driveBase.getAngle();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}

	public void autoRight() {
		if (autoTimer < 75) { // DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.7, 0);
		} else if (autoTimer < 100) { // TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(-60);
		} else if (autoTimer < 175) { // DRIVE TO GEAR
			if (autoTimer == 101) {
				Robot.driveBase.setNoPid();
			}
			Robot.driveBase.gyroMecanumDrive(0, 0, -.45, -60);
		} else if (autoTimer < 200) { // DROP GEAR
			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.gear.open();

		} else if (autoTimer < 230) { // DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, -60);
			if (autoTimer > 255) {
				Robot.gear.close();
			}
			Robot.shooter.shoot(shootSpeed);
		} else if (autoTimer < 270) { // TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(90);
		} else if (autoTimer < 355) { // DRIVE TO HOPPER
			if (autoTimer == 271) {
				Robot.driveBase.setNoPid();
			}

			Robot.driveBase.gyroMecanumDrive(0, 0, -.5, 90);

		} else if (autoTimer < 370) { // LET HOPPER EMPTY && INTAKE
			Robot.driveBase.mecanumDrive(0, .5, 0);

		} else if (autoTimer < 400) {// POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, -.5, -.3);
		} else {// SHOOT
			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.shooter.feed();
		}
	}

	public void autoLeft() {

		if (autoTimer < 75) { // DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.7, 0);
		} else if (autoTimer < 100) { // TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(60);
		} else if (autoTimer < 175) { // DRIVE TO GEAR
			if (autoTimer == 101) {
				Robot.driveBase.setNoPid();
			}
			Robot.driveBase.gyroMecanumDrive(0, 0, -.45, 60);
		} else if (autoTimer < 200) { // DROP GEAR
			Robot.gear.open();
			Robot.driveBase.mecanumDrive(0, 0, 0);

		} else if (autoTimer < 230) { // DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, 60);
			if (autoTimer > 255) {
				// Robot.gear.close();
			}
			Robot.shooter.shoot(shootSpeed);
		} else if (autoTimer < 270) { // TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(90);
		} else if (autoTimer < 345) { // DRIVE TO HOPPER
			if (autoTimer == 271) {
				Robot.driveBase.setNoPid();
			}

			Robot.driveBase.gyroMecanumDrive(0, 0, .5, 90);

		} else if (autoTimer < 355) {
			Robot.driveBase.gyroMecanumDrive(0, -.2, .5, 90);
		} else if (autoTimer < 370) { // LET HOPPER EMPTY && INTAKE
			Robot.driveBase.mecanumDrive(0, -.7, 0);
		} else if (autoTimer < 415) {// POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, -.3, .3);
		} else {// SHOOT
			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.shooter.feed();
		}

	}

	public void centerAuto() {

		if (autoTimer < 75) { // DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.7, 0);
		} else if (autoTimer < 100) { // DROP GEAR
			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.gear.open();
		} else if (autoTimer < 120) { // DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, 0);
		} else if (autoTimer < 135) {
			Robot.driveBase.gyroMecanumDrive(0, 0, -.3, 0);
		} else {
			Robot.driveBase.mecanumDrive(0, 0, 0);
		}
	}

	public void baseAuto() {

		if (autoTimer < 75) { // DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.7, 0);
		} else {
			Robot.driveBase.mecanumDrive(0, 0, 0);
		}

	}

	public void autoVisRight() {
		if (autoTimer < 71) { // DRIVING FORWARD
			Robot.gear.hopperOpen();
			Robot.driveBase.gyroMecanumDrive(0, 0, -.68, 0);
		} else if (autoTimer < 125) { // TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(-60);
		} else if (autoTimer < 200) { // DRIVE TO GEAR
			if (autoTimer == 126) {
				Robot.driveBase.setNoPid();
			}

			if (SmartDashboard.getBoolean("targetInFrame", false)) {
				visionSeen = true;
			}

			double angle = SmartDashboard.getNumber("angle", 0);

			if (angle > 4) {
				Robot.driveBase.gyroMecanumDrive(0, 0, -.45, -60);
			} else if (angle < -4) {
				Robot.driveBase.gyroMecanumDrive(0, 0, -.45, -60);
			} else {
				Robot.driveBase.gyroMecanumDrive(0, 0, -.45, -60);
			}

		} else if (autoTimer < 250) { // DROP GEAR
			Robot.driveBase.mecanumDrive(0, 0, 0);

			Robot.gear.open();
			Robot.shooter.encoderShoot(shootSpeed);
			// if (visionSeen) {

			// }

		} else if (autoTimer < 275) { // DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, -60);
					//took out ramp up
		} else if (autoTimer < 315) { // TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(90);


		} else if (autoTimer < 400) { // DRIVE TO HOPPER
			if (autoTimer == 316) {
				Robot.driveBase.setNoPid();
			}
			Robot.gear.close();
			Robot.driveBase.gyroMecanumDrive(0, 0, -.5, 90);

			Robot.shooter.encoderShoot(shootSpeed);

		} else if (autoTimer < 460) {// POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, -.3, -.45);

			Robot.shooter.encoderShoot(shootSpeed);

		} else if (autoTimer < 480) {// SHOOT
			Robot.driveBase.mecanumDrive(0, -.55, 0);

			Robot.shooter.feed();

			Robot.shooter.encoderShoot(shootSpeed);

		} else if (autoTimer < 495) {
			Robot.shooter.feed();
			Robot.shooter.shoot(shootSpeed);
			Robot.driveBase.mecanumDrive(-.35, 0, 0);

			Robot.shooter.encoderShoot(shootSpeed);

		} else {
			Robot.shooter.feed();
			Robot.driveBase.mecanumDrive(0, 0, 0);

			Robot.shooter.encoderShoot(shootSpeed);

		}
	}

	public void autoVisLeft() {

		if (autoTimer < 71) { // DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.68, 0);
		} else if (autoTimer < 125) { // TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(60);
		} else if (autoTimer < 200) { // DRIVE TO GEAR
			if (autoTimer == 126) {
				Robot.driveBase.setNoPid();
			}

			if (SmartDashboard.getBoolean("targetInFrame", false)) {
				visionSeen = true;
			}

			double angle = SmartDashboard.getNumber("angle", 0);

			if (angle > 4) {
				Robot.driveBase.gyroMecanumDrive(0, 0, -.45, 60);
			} else if (angle < -4) {
				Robot.driveBase.gyroMecanumDrive(0, 0, -.45, 60);
			} else {
				Robot.driveBase.gyroMecanumDrive(0, 0, -.45, 60);
			}

		} else if (autoTimer < 250) { // DROP GEAR
			// if (visionSeen) {
			Robot.gear.open();
			// }
			Robot.driveBase.mecanumDrive(0, 0, 0);

			Robot.shooter.encoderShoot(shootSpeed);

		} else if (autoTimer < 280) { // DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, 60);


		} else if (autoTimer < 320) { // TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(90);


		} else if (autoTimer < 405) { // DRIVE TO HOPPER
			if (autoTimer == 321) {
				Robot.driveBase.setNoPid();
			}
			Robot.gear.close();
			Robot.driveBase.gyroMecanumDrive(0, 0, .5, 90);

			Robot.shooter.encoderShoot(shootSpeed);

		} else if (autoTimer < 465) {// POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, -.3, .45);

			Robot.shooter.encoderShoot(shootSpeed);

		} else if (autoTimer < 485) {// SHOOT
			Robot.driveBase.mecanumDrive(0, -.4, 0);

			Robot.shooter.feed();

			Robot.shooter.encoderShoot(shootSpeed);

		} else if (autoTimer < 500) {
			Robot.shooter.feed();
			Robot.driveBase.mecanumDrive(.3, 0, 0);

			Robot.shooter.encoderShoot(shootSpeed);

		} else {
			Robot.shooter.feed();
			Robot.driveBase.mecanumDrive(0, 0, 0);

			Robot.shooter.encoderShoot(shootSpeed);
		}

	}

	public void autoVisRightShoot() {
		if (autoTimer < 75) { // DRIVING FORWARD
			Robot.gear.hopperOpen();
			Robot.driveBase.gyroMecanumDrive(0, 0, -.68, 0);
		} else if (autoTimer < 125) { // TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(-60);
		} else if (autoTimer < 225) { // DRIVE TO GEAR
			if (autoTimer == 126) {
				Robot.driveBase.setNoPid();
			}

			if (SmartDashboard.getBoolean("targetInFrame", false)) {
				visionSeen = true;
			}

			double angle = SmartDashboard.getNumber("angle", 0);

			if (angle > 8) {
				Robot.driveBase.gyroMecanumDrive(0, .30, -.25, 0);
			} else if (angle > 3) {
				Robot.driveBase.gyroMecanumDrive(0, .2, -.25, 0);
			} else if (angle > 4) {
				Robot.driveBase.gyroMecanumDrive(0, .2, -.25, 0);
			} else if (angle < -8) {
				Robot.driveBase.gyroMecanumDrive(0, -.3, -.25, 0);
			} else if (angle < -3) {
				Robot.driveBase.gyroMecanumDrive(0, -.2, -.25, 0);
			} else if (angle < -4) {
				Robot.driveBase.gyroMecanumDrive(0, -.2, -.25, 0);
			} else {
				Robot.driveBase.gyroMecanumDrive(0, 0, -.25, 0);
			}

		} else if (autoTimer < 250) { // DROP GEAR
			Robot.driveBase.mecanumDrive(0, 0, 0);

			Robot.gear.open();

			// if (visionSeen) {

			// }

		} else if (autoTimer < 280) { // DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, -60);

			Robot.shooter.shoot(shootSpeed);
		} else if (autoTimer < 320) { // TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(90);
		} else if (autoTimer < 405) { // DRIVE TO HOPPER
			if (autoTimer == 321) {
				Robot.driveBase.setNoPid();
			}
			Robot.gear.close();
			Robot.driveBase.gyroMecanumDrive(0, -.2, -.5, 90);

		} else if (autoTimer < 440) {// POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, -.5, -.3);
		} else if (autoTimer < 460) {// SHOOT
			Robot.driveBase.mecanumDrive(0, -.2, 0);
			Robot.shooter.feed();
		} else {
			Robot.shooter.feed();
			Robot.driveBase.mecanumDrive(0, 0, 0);
		}
	}

	public void autoVisLeftShoot() {

		if (autoTimer < 75) { // DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.68, 0);
		} else if (autoTimer < 125) { // TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(60);
		} else if (autoTimer < 225) { // DRIVE TO GEAR
			if (autoTimer == 126) {
				Robot.driveBase.setNoPid();
			}

			if (SmartDashboard.getBoolean("targetInFrame", false)) {
				visionSeen = true;
			}

			double angle = SmartDashboard.getNumber("angle", 0);

			if (angle > 8) {
				Robot.driveBase.gyroMecanumDrive(0, .30, -.25, 0);
			} else if (angle > 3) {
				Robot.driveBase.gyroMecanumDrive(0, .2, -.25, 0);
			} else if (angle > 4) {
				Robot.driveBase.gyroMecanumDrive(0, .2, -.25, 0);
			} else if (angle < -8) {
				Robot.driveBase.gyroMecanumDrive(0, -.3, -.25, 0);
			} else if (angle < -3) {
				Robot.driveBase.gyroMecanumDrive(0, -.2, -.25, 0);
			} else if (angle < -4) {
				Robot.driveBase.gyroMecanumDrive(0, -.2, -.25, 0);
			} else {
				Robot.driveBase.gyroMecanumDrive(0, 0, -.25, 0);
			}

		} else if (autoTimer < 250) { // DROP GEAR
			// if (visionSeen) {
			Robot.gear.open();
			// }
			Robot.driveBase.mecanumDrive(0, 0, 0);

		} else if (autoTimer < 280) { // DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, 60);

			Robot.shooter.shoot(shootSpeed);
		} else if (autoTimer < 320) { // TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(90);
		} else if (autoTimer < 395) { // DRIVE TO HOPPER
			if (autoTimer == 321) {
				Robot.driveBase.setNoPid();
			}
			Robot.gear.close();
			Robot.driveBase.gyroMecanumDrive(0, 0, .5, 90);

		} else if (autoTimer < 405) {
			Robot.driveBase.gyroMecanumDrive(0, 0, .5, 90);
		} else if (autoTimer < 440) {// POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, -.3, .45);
		} else if (autoTimer < 460) {// SHOOT
			Robot.driveBase.mecanumDrive(0, -.5, 0);

			Robot.shooter.feed();
		} else if(autoTimer < 470){
			
			Robot.driveBase.mecanumDrive(.3, 0, 0);
			
		}else{
			
			Robot.driveBase.mecanumDrive(0, 0, .8);
			Robot.shooter.feed();
		}

	}

	void visTester() {

		double angle = SmartDashboard.getNumber("angle", 0);

		if (angle > 8) {
			Robot.driveBase.gyroMecanumDrive(0, .30, -.25, 0);
		} else if (angle > 3) {
			Robot.driveBase.gyroMecanumDrive(0, .2, -.25, 0);
		} else if (angle > 4) {
			Robot.driveBase.gyroMecanumDrive(0, .2, -.25, 0);
		} else if (angle < -8) {
			Robot.driveBase.gyroMecanumDrive(0, -.3, -.25, 0);
		} else if (angle < -3) {
			Robot.driveBase.gyroMecanumDrive(0, -.2, -.25, 0);
		} else if (angle < -4) {
			Robot.driveBase.gyroMecanumDrive(0, -.2, -.25, 0);
		} else {
			Robot.driveBase.gyroMecanumDrive(0, 0, -.25, 0);
		}

	}

	public void autoVisRightGround() {
		if (autoTimer < 71) { // DRIVING FORWARD
			Robot.gear.hopperOpen();
			Robot.driveBase.gyroMecanumDrive(0, 0, .72, 0);
		} else if (autoTimer < 125) { // TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(-60);
		} else if (autoTimer < 200) { // DRIVE TO GEAR
			if (autoTimer == 126) {
				Robot.driveBase.setNoPid();
			}

			if (SmartDashboard.getBoolean("targetInFrame", false)) {
				visionSeen = true;
			}

			double angle = SmartDashboard.getNumber("angle", 0);

			if (angle > 4) {
				Robot.driveBase.gyroMecanumDrive(0, 0, .45, -60);
			} else if (angle < -4) {
				Robot.driveBase.gyroMecanumDrive(0, 0, .45, -60);
			} else {
				Robot.driveBase.gyroMecanumDrive(0, 0, .45, -60);
			}

		} else if (autoTimer < 225) { // DROP GEAR
			Robot.driveBase.mecanumDrive(0, 0, 0);

			// Robot.gear.open();
			Robot.intake.outtakeMethod();

			Robot.shooter.shoot(shootSpeed);
			// if (visionSeen) {

			// }

		} else if (autoTimer < 255) { // DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, -.6, -60);

			Robot.shooter.shoot(shootSpeed);
		} else if (autoTimer < 295) { // TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(-90);
		} else if (autoTimer < 380) { // DRIVE TO HOPPER
			if (autoTimer == 296) {
				Robot.driveBase.setNoPid();
			}
			Robot.gear.close();
			Robot.driveBase.gyroMecanumDrive(0, 0, -.5, -90);

		} else if (autoTimer < 440) {// POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, .3, -.45);
		} else if (autoTimer < 460) {// SHOOT
			Robot.driveBase.mecanumDrive(0, .5, 0);

			Robot.shooter.feed();
		} else if (autoTimer < 470) {
			Robot.shooter.feed();
			Robot.shooter.shoot(shootSpeed);
			Robot.driveBase.mecanumDrive(-.3, 0, 0);
		} else {
			Robot.shooter.feed();
			Robot.driveBase.mecanumDrive(0, 0, 0);
		}
	}

	public void autoVisLeftGround() {

		if (autoTimer < 71) { // DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, .77, 0);
		} else if (autoTimer < 125) { // TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(60);
		} else if (autoTimer < 200) { // DRIVE TO GEAR
			if (autoTimer == 126) {
				Robot.driveBase.setNoPid();
			}

			if (SmartDashboard.getBoolean("targetInFrame", false)) {
				visionSeen = true;
			}

			double angle = SmartDashboard.getNumber("angle", 0);

			if (angle > 4) {
				Robot.driveBase.gyroMecanumDrive(0, 0, .45, 60);
			} else if (angle < -4) {
				Robot.driveBase.gyroMecanumDrive(0, 0, .45, 60);
			} else {
				Robot.driveBase.gyroMecanumDrive(0, 0, .45, 60);
			}

		} else if (autoTimer < 225) { // DROP GEAR

			// Robot.gear.open();
			Robot.intake.outtake();

			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.shooter.shoot(shootSpeed);

		} else if (autoTimer < 255) { // DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, -.6, 60);

		} else if (autoTimer < 310) { // TURN TO SHOOT  // WAS 295
			Robot.driveBase.setGyroPIDControl(-90);
		} else if (autoTimer < 370) { // DRIVE TO HOPPER
			
			Robot.driveBase.setNoPid();
			
			Robot.gear.close();
			Robot.driveBase.gyroMecanumDrive(0, 0, .5, -90);

		} else if (autoTimer < 440) {// POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, -.3, .45);
		} else if (autoTimer < 460) {// SHOOT
			Robot.driveBase.mecanumDrive(0, -.5, 0);

			Robot.shooter.feed();
		} else if (autoTimer < 470) {
			Robot.shooter.feed();
			Robot.shooter.shoot(shootSpeed);
			Robot.driveBase.mecanumDrive(.3, 0, 0);
		} else {
			Robot.shooter.feed();
			Robot.driveBase.mecanumDrive(0, 0, 0);
		}

	}
	
	public void autoVisCenterGround() {

		if (autoTimer < 125) { // DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, .4, 0);
		}else if (autoTimer < 150) { // DROP GEAR

			// Robot.gear.open();
			Robot.intake.outtake();

			Robot.driveBase.mecanumDrive(0, 0, 0);

		} else if (autoTimer < 245) { // DRIVE BACK
			
			Robot.intake.outtake();
			Robot.driveBase.gyroMecanumDrive(0, 0, -.3, 0);

		} else if (autoTimer < 360){
			
			Robot.driveBase.gyroMecanumDrive(0, 0, .25, 0);
			
		}else {
			
			Robot.driveBase.mecanumDrive(0, 0, 0);
			
		}
		
	}

	public void twist() {
		Robot.driveBase.setGyroPIDControl(60);
		
		SmartDashboard.putNumber("YAW", Robot.driveBase.getAngle());
	}

}
