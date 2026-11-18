package org.firstinspires.ftc.teamcode.pedro.Main_code;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class robot_test extends OpMode {
    flower flower = new flower();
    centerControl center = new centerControl();

    private boolean lastX = false;
    private boolean lastrightB = false;
    private boolean lastY = false;

    @Override
    public void init() {
        flower.intt(hardwareMap,"llfs","lrfs","flg");
        center.intt(hardwareMap,"gs","lfs","rfs");

    }

    @Override
    public void loop() {
        boolean currentX= gamepad1.x;
        boolean currentY= gamepad1.y;
        boolean currentRB= gamepad1.right_bumper;

        if(currentX && !lastX){
            flower.Run_flower(getRuntime());
        }

        if(currentY && !lastY){
            center.flower_gate();
        }

        if (currentRB && !lastrightB){
            center.shooter_gate();
        }


        lastX = currentX;
        lastY =currentY;
        lastrightB =currentRB;

        flower.Update_time(getRuntime());

    }
}
