package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class flower {
    private CRServo left_fServo;
    private CRServo right_fServo;
    private Servo flower_gate;

    private boolean ServoRuning=false ;
    private boolean flowerUP=false;
    private double startTime = 0;
    private double servoRunTime=1.2;
    private double[] step = {1, 2, 3,};
    private double flower_state=0;
    public void intt(HardwareMap hwmap,String left_flower_servo,String right_flower_servo,String flower_gate){
        left_fServo=hwmap.get(CRServo.class,left_flower_servo);
        right_fServo=hwmap.get(CRServo.class,right_flower_servo);
        this.flower_gate=hwmap.get(Servo.class,flower_gate);
        left_fServo.setDirection(DcMotorSimple.Direction.REVERSE);
        right_fServo.setDirection(DcMotorSimple.Direction.FORWARD);

    }

    public void run_servo(double speed) {
            left_fServo.setPower(speed);
            right_fServo.setPower(speed);
    }

    public void gateOpen(Boolean state){
        if (state){
            flower_gate.setPosition(0.0);
        } else if (!state) {
            flower_gate.setPosition(0.25);
        }
    }

    public void UP(double currentTime){
        if(!ServoRuning){
            startTime=currentTime;
            ServoRuning=true;
            if(!flowerUP){
                run_servo(1.0);
            } else if (flowerUP) {
                run_servo(-1.0);
            }
        }
    }

    public void Update_time(double currentTime){
        if(ServoRuning && (currentTime - startTime >=servoRunTime )){
            run_servo(0.0);
            ServoRuning=false;
            flowerUP=!flowerUP;
        }
    }

    public void Run_flower(double currentTime){
        flower_state=(flower_state+1)%step.length;
        if(flower_state==1){
            UP(currentTime);
        } else if (flower_state==2) {
            gateOpen(true);
        } else if (flower_state==0) {
            UP(currentTime);
            gateOpen(false);
        }
    }

    public double getFlower_state(){
        return flower_state;
    }
}
