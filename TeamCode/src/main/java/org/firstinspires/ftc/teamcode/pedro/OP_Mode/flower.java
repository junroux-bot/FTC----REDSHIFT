package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class flower {
    private CRServo left_fServo;
    private CRServo right_fServo;
    private Servo flower_gate;
    private double[] state_flower ={1,2,3,4};
    private double currunt_state;
    boolean servo_runing;
    double servoRunTime=1.2;
    public void intt(HardwareMap hwmap,String left_flower_servo,String right_flower_servo,String flower_gate){
        left_fServo=hwmap.get(CRServo.class,left_flower_servo);
        right_fServo=hwmap.get(CRServo.class,right_flower_servo);
        this.flower_gate=hwmap.get(Servo.class,flower_gate);
        left_fServo.setDirection(DcMotorSimple.Direction.REVERSE);
        right_fServo.setDirection(DcMotorSimple.Direction.FORWARD);
        currunt_state=0;

    }

    public void run_servo(double speed) {
            left_fServo.setPower(speed);
            right_fServo.setPower(speed);
    }

    public void gateOpen(Boolean state){
        if (state==true){
            flower_gate.setPosition(0.0);
        } else if (state==false) {
            flower_gate.setPosition(0.25);
        }

    }
}
