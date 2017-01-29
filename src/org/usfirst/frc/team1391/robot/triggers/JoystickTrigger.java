package org.usfirst.frc.team1391.robot.triggers;

import org.usfirst.frc.team1391.robot.OI;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 *
 */
public class JoystickTrigger extends Trigger {

    public boolean get() {
    	
    	double driverX = OI.driver.getAxis(AxisType.kX);
    	double driverY = OI.driver.getAxis(AxisType.kY);
    	double driverZ = OI.driver.getAxis(AxisType.kZ);
    	double driverThrottle = OI.driver.getAxis(AxisType.kThrottle);
    	if(DriverStation.getInstance().isOperatorControl() && (driverX != 0 || driverY != 0 || driverZ != 0 || driverThrottle != 0)){
    		return true;
    	}
    	
    	return false;
    }
}
