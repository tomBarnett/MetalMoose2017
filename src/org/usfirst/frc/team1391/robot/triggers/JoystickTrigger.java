package org.usfirst.frc.team1391.robot.triggers;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 *
 */
public class JoystickTrigger extends Trigger {

    public boolean get() {
    	if(DriverStation.getInstance().isOperatorControl()){
        return true;
    	}else{
    	return false;
    	}
    }
}
