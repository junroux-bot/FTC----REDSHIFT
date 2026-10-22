package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class precode extends OpMode {
    Motor_Setup drive = new Motor_Setup();
    IMU_Setup imu = new IMU_Setup();
    limelight_Setup limelight = new limelight_Setup();
    Intake intake = new Intake();
    @Override
    public void init() {
        drive.init(hardwareMap,"fl","bl","fr","br");
        imu.intt(hardwareMap);
        imu.Reset_imu();
        limelight.camera_init(hardwareMap,"limelight",8);
        limelight.start();
        intake.intt(hardwareMap,"distance sensor","Color sensor","intake motor");
        intake.set_ball_munber(4);


    }

    @Override
    public void loop() {
        drive.drive(-gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);
        intake.run();


    }
}
