package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class centerControl {
    private Servo gate;
    private Servo leftFlower_gate;
    private Servo rightFlower_gate;
    private double[] step={1,2};
    private double flower_state=0;
    private double shooter_state=0;


    private Follower follower;
    public void intt(HardwareMap hwmap,String gate_Srevo,String LeftFlowerServo,String RightFlowerServo){
        gate=hwmap.get(Servo.class,gate_Srevo);
        leftFlower_gate=hwmap.get(Servo.class,LeftFlowerServo);
        rightFlower_gate=hwmap.get(Servo.class,RightFlowerServo);
    }

    public void flower_gate (){
        flower_state=(flower_state +1)% step.length;
        if(flower_state==0){
            leftFlower_gate.setPosition(0.25);
            rightFlower_gate.setPosition(0.25);
        } else if (flower_state==1) {
            leftFlower_gate.setPosition(0.0);
            rightFlower_gate.setPosition(0.0);
        }
    }

    public void shooter_gate(){
        shooter_state=(shooter_state +1)% step.length;
        if(shooter_state==1){
            gate.setPosition(0.25);
        } else if (shooter_state==0) {
            gate.setPosition(0.0);
        }
    }


}
