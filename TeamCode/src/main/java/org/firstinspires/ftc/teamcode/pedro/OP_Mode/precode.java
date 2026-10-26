package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class precode extends OpMode {
    Motor_Setup drive = new Motor_Setup();
    IMU_Setup imu = new IMU_Setup();
    limelight_Setup limelight = new limelight_Setup();
    Intake intake = new Intake();
    flower flower = new flower();
    centerControl center = new centerControl();

    private boolean lastX = false;
    private boolean lastrightB = false;
    private boolean lastY = false;
    @Override
    public void init() {
        drive.init(hardwareMap,"fl","bl","fr","br");
        imu.intt(hardwareMap);
        imu.Reset_imu();
        limelight.camera_init(hardwareMap,"limelight",8);
        limelight.start();
        intake.intt(hardwareMap,"distance sensor","Color sensor","intake motor","Intake Servo");
        intake.set_ball_munber(4);
        flower.intt(hardwareMap,"lfs","rfs","fg");
        center.intt(hardwareMap,"g","lfg","rfg");


    }

    @Override
    public void loop() {
        boolean currentX= gamepad1.x;
        boolean currentY= gamepad1.y;
        boolean currentRB= gamepad1.right_bumper;

        drive.drive(-gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);
        intake.run();

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

        telemetry.addData("state",flower.getFlower_state());

        flower.Update_time(getRuntime());



    }
}
