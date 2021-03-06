package org.ftc7244.robotcontroller.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.drivers.PIDGyroscopeDrive;
import org.ftc7244.robotcontroller.autonomous.drivers.SPGyroscopeDrive;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Contains all the code for different drive types including ${@link PIDGyroscopeDrive}.
 * Not much happens here beyond the essentials for each control method. It also
 * automatically handles wait for startImageReading since most of the setup is completed and only driving
 * instructions are needed.
 */
public abstract class ControlSystemAutonomous extends LinearOpMode {

    public final static long AUTONOMOUS_DURATION = 30 * 1000;

    protected final GyroscopeProvider gyroProvider;

    protected final PIDGyroscopeDrive gyroscopePID;

    protected final ImageTransformProvider imageProvider;

    protected Westcoast robot;
    private long end;

    /**
     * Loads hardware, pid drives, and sensor providers
     */
    protected ControlSystemAutonomous() {
        robot = new Westcoast(this);

        gyroProvider = new RevIMUGyroscopeProvider();
        imageProvider = new ImageTransformProvider(robot);

        gyroscopePID = new PIDGyroscopeDrive(robot, gyroProvider);
    }

    @Override
    public void runOpMode(){
        Logger.init();

        robot.init();
        robot.initServos();
        robot.getDriveBackLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveBackRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveFrontLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveFrontRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveBackLeft().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getDriveBackRight().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getDriveFrontLeft().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getDriveFrontRight().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Status.setAutonomous(this);
        //Initializes robot and debug features

        try {
            gyroProvider.start(hardwareMap);
            imageProvider.start(hardwareMap);
            while (!isStarted()) {
                if (gyroProvider.isCalibrated()) {
                    telemetry.addLine("Gyroscope calibrated!");
                    telemetry.update();
                } else  {
                    telemetry.addLine("No Connection");
                    telemetry.update();
                }
                idle();
            }

            gyroscopePID.resetOrientation();
            end = System.currentTimeMillis() + AUTONOMOUS_DURATION;
            //Calibrates and starts providers
            run();
        } catch (Throwable t) {
            RobotLog.e(t.getMessage());
            t.printStackTrace();
            //Logs unexpected errors to prevent app crashing
        } finally {
            gyroProvider.stop();
            imageProvider.stop();
            Status.setAutonomous(null);
            //Stops all providers regardless of error
        }
    }

    public long getAutonomousEnd() {
        return end;
    }

    protected void outtake(){
        robot.getIntakeBottomRight().setPower(0.5);
        robot.getIntakeBottomLeft().setPower(0.5);
        robot.getIntakeTop().setPower(1);
        robot.getIntakeBottom().setPower(1);
        robot.getIntakePusher().setPosition(Westcoast.INTAKE_PUSHER_OUT);
        sleep(750);
        robot.getIntakeBottomRight().setPower(0);
        robot.getIntakeBottomLeft().setPower(0);
    }

    /**
     * The custom autonomous procedure
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public abstract void run();

}