
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
	public static boolean visionTarget = false; //false = gear; true = target; 
	public static boolean visionSeen = false;
	
	public static int orientationJoy = 0;
	
	public static int autoTimer = 0;
	
	public static double shootSpeed = .77;
	public static int shootTimer = 0; 
	
	public static boolean outtakeFlag = false;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		
		CameraServer.getInstance().addAxisCamera("10.13.91.3");
		CameraServer.getInstance().addServer("10.13.91.3");
		
		SmartDashboard.putBoolean("visionTarget", visionTarget);
		SmartDashboard.putNumber("gyroGain", .65);
		
		
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

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
		autoTimer++;
		
		autoLeft();
		
		/*
		if(autoTimer < 200){
			driveBase.mecanumDrive(0, .5, 0);
		}else if(autoTimer < 300){
			driveBase.stop();
			driveBase.setGyroPIDControl(45);
			if(autoTimer == 299){
				driveBase.setNoPid();
			}
			
		}else if(autoTimer < 500){
			driveBase.mecanumDrive(0, .5, 0);
		}
		*/
		
	}

	@Override
	public void teleopInit() {

		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {

		//DRIVING
		if (OI.driverLT.get() && visionFlag == false) {
			gyroVision.execute();
		}else if(OI.driverRT.get()) {
			gyroStop.execute();
		}else if(!driveBase.getPIDController().isEnabled() && !Robot.gear.active && !outtakeFlag){
			mecanumDrive.executeTelo();
		}
		
		if(!OI.driverLT.get() && visionFlag == true){
			visionFlag = false;
		}
		
		//GEAR SHIFTING
		if(OI.driverLB.get() || OI.operatorLB.get()){
			driveBase.lowGear();
		}else if(OI.driverRB.get() || OI.operatorRB.get()){
			driveBase.highGear();
		}
		
		//INTAKE
		if(OI.operatorB.get()){
			Robot.intake.intake();
		}else if(OI.operatorY.get()){
			outtakeFlag = true;
			Robot.intake.outtakeMethod();
		}else{
			outtakeFlag = false;
			Robot.intake.stop();
		}
		
		Robot.intake.liftMethod();
		
		if(OI.operatorJoyL.get()){
			Robot.intake.hingeOpen();
		}else if(OI.operatorJoyR.get()){
			Robot.intake.hingeClose();
		}
		
		if(OI.operator.getRawAxis(1) > .25){
			Robot.intake.lift(OI.operator.getRawAxis(1));
		}else if(OI.operator.getRawAxis(1) < -.25){
			Robot.intake.lift(OI.operator.getRawAxis(1));
		}
		
		//FEEDER
		
		if(OI.operator.getRawAxis(3) > .3){
			shootTimer++;
			Robot.shooter.shoot(shootSpeed);
			if(shootTimer > 80){
				Robot.shooter.feed();
			}
		}else{
			Robot.shooter.stop();
			Robot.shooter.shoot(0);
			shootTimer = 0;
		}
		
		/*
		if(OI.operator.getRawAxis(1)> .3){
			Robot.shooter.shoot(shootSpeed);
		}else{
			Robot.shooter.shoot(0);
		}
		*/
		
		//HANGER
		if(OI.operator.getPOV() == 180){
			Robot.hanger.lift();
		}else if(OI.operator.getPOV() == 0){
			Robot.hanger.halt();
		}
		
		//GEAR SEQUENCE
		/*
		if(OI.operatorX.get()){
			Robot.gear.sequenceStart();
		}else if(OI.operatorA.get()){
			Robot.gear.sequenceEject();
		}
		*/
		
		Robot.gear.sequence();
		
		if(OI.operatorA.get()){
			Robot.gear.open();
		}else if(OI.operatorX.get()){
			Robot.gear.close();
		}
		
		//SET WHAT VISION WANTS
		if(OI.operatorLT.get()){ //gear
			visionTarget = false;
			SmartDashboard.putBoolean("visionTarget", visionTarget);
		}else if(OI.operatorRT.get()){ //vision target
			visionTarget = true;
			SmartDashboard.putBoolean("visionTarget", visionTarget);
		}
		
		//SET ORIENTATION
		if(OI.driverA.get()){
			orientationJoy = 0;
		}else if(OI.driverB.get()){
			orientationJoy = 1;
		}else if(OI.driverX.get()){
			orientationJoy = 2;
		}else if(OI.driverY.get()){
			orientationJoy = 3;
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
	
	public void autoRight(){
		if(autoTimer < 75){ //DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.7, 0);
		}else if (autoTimer<100){ //TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(-60);
		}else if (autoTimer<175){ //DRIVE TO GEAR
			if(autoTimer == 101){
				Robot.driveBase.setNoPid();
			}
			Robot.driveBase.gyroMecanumDrive(0, 0, -.45, -60);
		}else if (autoTimer<200){ //DROP GEAR
			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.gear.open();
			
		}else if (autoTimer<230){ //DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, -60);
			if(autoTimer >255){
				Robot.gear.close();
			}
			Robot.shooter.shoot(shootSpeed);
		}else if (autoTimer<270){ //TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(90);
		}else if (autoTimer<355){ //DRIVE TO HOPPER
			if(autoTimer == 271){
				Robot.driveBase.setNoPid();
			}
			
			Robot.driveBase.gyroMecanumDrive(0, 0, -.5, 90);
			
		}else if(autoTimer < 370){ //LET HOPPER EMPTY && INTAKE
			Robot.driveBase.mecanumDrive(0, .5, 0);
			
		}else if(autoTimer < 400){//POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, -.5, -.3);
		}else {//SHOOT
			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.shooter.feed();
		}
	}
	
	public void autoLeft(){
		
		if(autoTimer < 75){ //DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.7, 0);
		}else if (autoTimer<100){ //TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(60);
		}else if (autoTimer<175){ //DRIVE TO GEAR
			if(autoTimer == 101){
				Robot.driveBase.setNoPid();
			}
			Robot.driveBase.gyroMecanumDrive(0, 0, -.45, 60);
		}else if (autoTimer<200){ //DROP GEAR
			//Robot.gear.open();
			Robot.driveBase.mecanumDrive(0, 0, 0);
			
		}else if (autoTimer<230){ //DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, 60);
			if(autoTimer >255){
				//Robot.gear.close();
			}
			Robot.shooter.shoot(shootSpeed);
		}else if (autoTimer<270){ //TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(90);
		}else if (autoTimer<345){ //DRIVE TO HOPPER
			if(autoTimer == 271){
				Robot.driveBase.setNoPid();
			}
			
			Robot.driveBase.gyroMecanumDrive(0, 0, .5, 90);
			
		}else if(autoTimer < 355){
			Robot.driveBase.gyroMecanumDrive(0, -.2, .5, 90);
		}else if(autoTimer < 370){ //LET HOPPER EMPTY && INTAKE
			Robot.driveBase.mecanumDrive(0, -.7, 0);
		}else if(autoTimer < 415){//POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, -.3, .3);
		}else {//SHOOT
			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.shooter.feed();
		}
		
	}
	
	public void centerAuto(){
		
		if(autoTimer < 75){ //DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.7, 0);
		}else if (autoTimer<100){ //DROP GEAR
			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.gear.open();
		}else if (autoTimer<120){ //DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, -60);
		}else if (autoTimer < 135){
			Robot.driveBase.gyroMecanumDrive(0, 0, -.3, -60);
		}else {
			Robot.driveBase.mecanumDrive(0, 0, 0);
		}
	}
	
	public void baseAuto(){
		
		if(autoTimer < 75){ //DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.7, 0);
		}else {
			Robot.driveBase.mecanumDrive(0, 0, 0);
		}
		
	}

	public void autoVisRight(){
		if(autoTimer < 75){ //DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.7, 0);
		}else if (autoTimer<100){ //TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(-60);
		}else if (autoTimer<175){ //DRIVE TO GEAR
			if(autoTimer == 101){
				Robot.driveBase.setNoPid();
			}
			
			if(SmartDashboard.getBoolean("targetInFrame", false)){
				visionSeen = true;
			}
			
			if(SmartDashboard.getNumber("angle", 0) > 5){
				Robot.driveBase.gyroMecanumDrive(0, 0, -.45, -60);
			}else if(SmartDashboard.getNumber("angle", 0) < -5){
				Robot.driveBase.gyroMecanumDrive(0, -.2, -.45, -60);
			}else{
				Robot.driveBase.gyroMecanumDrive(0, .2, -.45, -60);
			}
			
		}else if (autoTimer<200){ //DROP GEAR
			Robot.driveBase.mecanumDrive(0, 0, 0);
			
			if(visionSeen){
				Robot.gear.open();
			}
			
		}else if (autoTimer<230){ //DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, -60);
			
			Robot.shooter.shoot(shootSpeed);
		}else if (autoTimer<270){ //TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(90);
		}else if (autoTimer<355){ //DRIVE TO HOPPER
			if(autoTimer == 271){
				Robot.driveBase.setNoPid();
			}
			Robot.gear.close();
			Robot.driveBase.gyroMecanumDrive(0, 0, -.5, 90);
			
		}else if(autoTimer < 370){ //LET HOPPER EMPTY && INTAKE
			Robot.driveBase.mecanumDrive(0, .5, 0);
			
		}else if(autoTimer < 390){//POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, .3, -.3);
		}else {//SHOOT
			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.shooter.feed();
		}
	}
	
public void autoVisLeft(){
		
		if(autoTimer < 75){ //DRIVING FORWARD
			Robot.driveBase.gyroMecanumDrive(0, 0, -.7, 0);
		}else if (autoTimer<100){ //TURN TO GEAR
			Robot.driveBase.setGyroPIDControl(60);
		}else if (autoTimer<175){ //DRIVE TO GEAR
			if(autoTimer == 101){
				Robot.driveBase.setNoPid();
			}
			
			if(SmartDashboard.getNumber("angle", 0) > 5){
				Robot.driveBase.gyroMecanumDrive(0, 0, -.45, 60);
			}else if(SmartDashboard.getNumber("angle", 0) < -5){
				Robot.driveBase.gyroMecanumDrive(0, -.2, -.45, 60);
			}else{
				Robot.driveBase.gyroMecanumDrive(0, .2, -.45, 60);
			}
			
		}else if (autoTimer<200){ //DROP GEAR
			Robot.gear.open();
			Robot.driveBase.mecanumDrive(0, 0, 0);
			
		}else if (autoTimer<230){ //DRIVE BACK
			Robot.driveBase.gyroMecanumDrive(0, 0, .6, 60);
			if(autoTimer >255){
				Robot.gear.close();
			}
			Robot.shooter.shoot(shootSpeed);
		}else if (autoTimer<270){ //TURN TO SHOOT
			Robot.driveBase.setGyroPIDControl(-90);
		}else if (autoTimer<355){ //DRIVE TO HOPPER
			if(autoTimer == 271){
				Robot.driveBase.setNoPid();
			}
			Robot.driveBase.gyroMecanumDrive(0, 0, -.5, -90);
			
		}else if(autoTimer < 370){ //LET HOPPER EMPTY && INTAKE
			Robot.driveBase.mecanumDrive(0, -.5, 0);
		}else if(autoTimer < 390){//POSITION FOR SHOOT && RAMP UP
			Robot.driveBase.mecanumDrive(0, -.3, -.3);
		}else {//SHOOT
			Robot.driveBase.mecanumDrive(0, 0, 0);
			Robot.shooter.feed();
		}
		
	}
	
}
