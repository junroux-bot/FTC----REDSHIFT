package org.firstinspires.ftc.teamcode.pedro.Plugin;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp

public class turret_tuning extends OpMode {

    turret turret = new turret();
    Motor_Setup base = new Motor_Setup();
    pose_source pose = new pose_source();
    double[] tuneSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};
    int stepIndex = 0;

    @Override
    public void init() {
        turret.intt(hardwareMap,"sfm","bfm","hm");
        turret.setup("red");
        base.init(hardwareMap,"fl","bl","fr","br");
        pose.init(hardwareMap,56,8,1.5708);
        turret.setPoseSource(pose);


    }

    @Override
    public void loop() {
        base.drive(-gamepad1.left_stick_y,gamepad1.left_stick_x, gamepad1.right_stick_x);
        turret.start_tracking();

        if (gamepad1.leftBumperWasPressed()) {
            stepIndex = (stepIndex + 1) % tuneSizes.length;
        }

        if (gamepad1.yWasPressed()) {
            turret.setkP(turret.getkP()+tuneSizes[stepIndex]);
        }
        if (gamepad1.xWasPressed()) {
            turret.setkP(turret.getkP()-tuneSizes[stepIndex]);
        }
        if (gamepad1.aWasPressed()) {
            turret.setKD(turret.getKD()+tuneSizes[stepIndex]);
        }
        if (gamepad1.bWasPressed()) {
            turret.setKD(turret.getKD()-tuneSizes[stepIndex]);
        }

        telemetry.addData("size",stepIndex);
        telemetry.addData("kp",turret.getkP());
        telemetry.addData("kD",turret.getKD());
        pose.update();

    }
}
