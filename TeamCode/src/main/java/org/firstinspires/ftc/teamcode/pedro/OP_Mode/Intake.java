package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Intake {
    private DcMotor Intake_Motor;
    private DistanceSensor Intake_DistanceSensor;
    double mun_ball;
    private boolean ballDetected;
    public void intt(HardwareMap hwmap,String distance_ss,String Color_ss,String Motor){
        Intake_Motor=hwmap.get(DcMotor.class,Motor);
        Intake_Motor.setDirection(DcMotorSimple.Direction.FORWARD);
        Intake_DistanceSensor=hwmap.get(DistanceSensor.class,distance_ss);
        mun_ball=0;
        ballDetected=false;

    }
    private double GetDistance(){
        return Intake_DistanceSensor.getDistance(DistanceUnit.MM);
    }
    public void intake(String mode) {
        if ("run".equalsIgnoreCase(mode)) {
            Intake_Motor.setPower(1.0);
        } else if ("stop".equalsIgnoreCase(mode)) {
            Intake_Motor.setPower(0.0);
        }
    }

    public void set_ball_munber(double munber){
        mun_ball+=munber;
    }

    public void run(){
        if(mun_ball==4){
            intake("stop");
        }else{
            intake("run");
            if(GetDistance()<= 94 && !ballDetected){
                mun_ball+=1;
                ballDetected=true;
            }else if(GetDistance()>94){
                ballDetected=false;
            }
        }
    }
}
