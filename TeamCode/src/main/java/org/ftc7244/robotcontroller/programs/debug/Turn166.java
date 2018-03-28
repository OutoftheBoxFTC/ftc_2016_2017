package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

@Autonomous(name = "Turn 166°")
@Disabled
public class Turn166 extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        gyroscopePID.rotate(166);
    }
}
