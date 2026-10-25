package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
public class test extends OpMode {
    flower flower = new flower();

    private boolean flower_state;
    private boolean lastX = false;

    @Override
    public void init() {
        flower.intt(hardwareMap,"lfs","rfs","fg");

    }

    @Override
    public void loop() {
        boolean currentX= gamepad1.x;
        if(currentX && !lastX){
            flower.Run_flower(getRuntime());
        }
        lastX = currentX;

        telemetry.addData("state",flower.getFlower_state());


        flower.Update_time(getRuntime());

    }
}
