package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;
import org.ftc7244.robotcontroller.input.PressButton;

/**
 * Created by Eeshwar Laptop on 10/16/2017.
 */

@TeleOp(name = "Relic Recovery Westcoast")
public class RelicRecoveryWestcoastTeleop extends OpMode {
    RelicRecoveryWestcoast robot;
    private Button left_trigger;
    private Button right_trigger;
    private PressButton a_button;
    private static final double SLOW_DRIVE_COEFFICIENT = 0.5;
    public void init(){
        robot = new RelicRecoveryWestcoast(this);
        robot.init();
        left_trigger = new Button(gamepad1, ButtonType.LEFT_TRIGGER);
        right_trigger = new Button(gamepad1, ButtonType.RIGHT_TRIGGER);
        a_button = new PressButton(gamepad1, ButtonType.A);

    }
    @Override
    public void loop(){
        if(left_trigger.isPressed()){
            robot.drive(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
        }
        else if(right_trigger.isPressed()){
            robot.drive(-gamepad1.left_stick_y* SLOW_DRIVE_COEFFICIENT,
                    -gamepad1.right_stick_y* SLOW_DRIVE_COEFFICIENT);
        }
        if(a_button.isPressed()){

        }
        else {

        }
    }
}