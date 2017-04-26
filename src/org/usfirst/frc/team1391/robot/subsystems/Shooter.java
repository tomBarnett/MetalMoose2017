package org.usfirst.frc.team1391.robot.subsystems;

import org.usfirst.frc.team1391.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {

	Victor shooter = new Victor(RobotMap.shooterMotor);
	Victor feeder = new Victor(RobotMap.feedMotor);
	Encoder encoder = new Encoder(RobotMap.shooterEncoder[0], RobotMap.shooterEncoder[1], false,
			Encoder.EncodingType.k1X);

	double desiredRate = 1300;
	double currentRate = 0;
	//double shootSpeed = .765;
	double speed = 0;
	double speedMod = 1;
	double deadband = 1;

	int sampleRate = 50;
	double dpp = 1;

	public void initDefaultCommand() {
		encoder.setMinRate(0);
		encoder.setReverseDirection(true);
		encoder.setPIDSourceType(PIDSourceType.kRate);

		encoder.setSamplesToAverage(sampleRate);
		encoder.setDistancePerPulse(dpp);
	}

	public void shoot(double shootSpeed) {

		shooter.set(shootSpeed);

	}

	public void stop() {

		feeder.set(0);

	}

	public void feed() {

		feeder.set(1);

	}

	public void encoderShoot(double shootSpeed) {

		currentRate = -encoder.getRate();

		if (currentRate == 0) {
			speed = 1;
		} else if (currentRate >= 0) {
			if (currentRate > desiredRate) {
				speed = shootSpeed;
			} else if (currentRate * deadband <= desiredRate) {
				speed = shootSpeed * (desiredRate / currentRate) * speedMod;
				if (speed < -1 || speed > 1) {
					speed = 1;
				}
			} else {
				speed = shootSpeed;
			}

		}

		shooter.setSpeed(speed);

	}

}
