package org.ftc7244.robotcontroller;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;
import org.ftc7244.robotcontroller.autonomous.pid.drivers.GyroscopeDrive;

/**
 * Created by OOTB on 11/12/2016.
 */
@Autonomous(name = "Beacon Red", group = "Red")
public class BeaconRed extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        gyroscope.drive(-0.35, 3);
        sleep(500);
        gyroscope.rotate(51);
        gyroscope.drive(-0.35, 14);
        robot.shootLoop(2, 500);
        gyroscope.drive(-0.35, 31);

        sleep(500);
        gyroscope.rotate(-44);

        sleep(100);
        ultrasonic.parallelize();
        sleep(300);
        gyroscope.resetOrientation();

        gyroscope.driveUntilLine(-0.2, GyroscopeDrive.Sensor.Trailing);
        sleep(500);
        gyroscope.drive(.2, 2);
        if (robot.isColor(Color.RED)) {
            robot.pushBeacon();
        } else {
            gyroscope.drive(-0.3, 2.5);
            robot.pushBeacon();
        }

        gyroscope.driveUntilLine(-0.2, GyroscopeDrive.Sensor.Trailing, 0, 30, 60);
        sleep(500);
        gyroscope.drive(.2, 2);
        if (robot.isColor(Color.RED)) {
            robot.pushBeacon();
        } else {
            gyroscope.drive(-0.3, 4);
            robot.pushBeacon();
        }

        gyroscope.rotate(45);
        gyroscope.drive(1, 45);
        sleep(2000);
        gyroscope.drive(.75, 10);
    }
}
