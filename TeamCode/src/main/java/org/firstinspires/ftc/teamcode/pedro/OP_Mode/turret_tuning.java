package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp

public class turret_tuning extends OpMode {

    turret turret = new turret();

    @Override
    public void init() {
        turret.intt(hardwareMap,"sfm","bfm","hm");
        turret.setup("red");
    }

    @Override
    public void loop() {

    }
}
