package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
public class test extends OpMode {
    flower flower = new flower();

    private boolean flower_state;
    private boolean lastX = false;
    private double flower_time=1.2;
    boolean lastA = false;
    boolean isForwardState = false;
    boolean isMoving = false;
    double startTime = 0;

    @Override
    public void init() {
        flower.intt(hardwareMap,"lfs","rfs","fg");


    }

    @Override
    public void loop() {
        boolean currentX = gamepad1.x;
        boolean currentA = gamepad1.a;
        if (currentX && !lastX) {
            flower_state = !flower_state;
            flower.gateOpen(flower_state);
        }
        lastX = currentX;


        if(currentA && !lastA && !isMoving){
            isMoving = true;
            startTime = getRuntime();
            if (!isForwardState) {
               flower.run_servo(1.0);
            } else {
                flower.run_servo(-1.0);
            }
        }
        lastA = currentA;
        if (isMoving && (getRuntime() - startTime >= flower_time)) {
            flower.run_servo(0.0);
            isMoving = false;
            isForwardState = !isForwardState;
        }
    }
}
