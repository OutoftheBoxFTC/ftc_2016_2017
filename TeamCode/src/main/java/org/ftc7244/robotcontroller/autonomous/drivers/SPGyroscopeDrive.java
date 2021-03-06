package org.ftc7244.robotcontroller.autonomous.drivers;

import org.ftc7244.robotcontroller.autonomous.controllers.DriveControl;
import org.ftc7244.robotcontroller.autonomous.controllers.semi_proportional.SPControllerBuilder;
import org.ftc7244.robotcontroller.autonomous.terminators.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonomous.terminators.TimerTerminator;
import org.ftc7244.robotcontroller.hardware.Hardware;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

/**
 * An attempt of using a simpler control system, implementing and building off of a solely proportional based
 * control loop
 *
 * Deprecated due to inconsistency and dependence of battery charge
 */
@Deprecated
public class SPGyroscopeDrive extends DriveControl {

    private GyroscopeProvider gyroProvider;
    private double target;

    public SPGyroscopeDrive(Hardware robot, GyroscopeProvider gyroProvider) {
        super(new SPControllerBuilder()
                .setBasePower(0.3)
                .setProportionalRange(60)
                .setMinimumPower(0.025)
                .createController(), robot);
        this.gyroProvider = gyroProvider;
    }

    @Override
    public double getReading() {
        double reading = this.gyroProvider.getZ();
        if (Math.abs(target) > 180) {
            if (target > 0 && reading < 0) {
                return 360 + reading;
            } else if (target < 0 && reading > 0) {
                return -360 + reading;
            }
        }
        return reading;
    }

    public void rotate(double degrees) throws InterruptedException {
        this.target = degrees;
        control(degrees, 0, new ConditionalTerminator(new TimerTerminator(4000), new SensitivityTerminator(this, target, 1, 30)));
        resetOrientation();
    }


    /**
     * Resets the current orientation to 0 and waits till the change occurs
     *
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public void resetOrientation() throws InterruptedException {
        do {
            gyroProvider.setZToZero();
        } while (Math.abs(Math.round(gyroProvider.getZ())) > 1);
    }
}
