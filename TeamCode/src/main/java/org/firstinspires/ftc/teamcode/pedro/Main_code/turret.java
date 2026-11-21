package org.firstinspires.ftc.teamcode.pedro.Main_code;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class turret {
    private pose_source robotPosition;
    private DcMotorEx small_M;
    private DcMotorEx big_M;
    private DcMotorEx heading_M;

    private double kp =0.0001;
    private double kD =0.0000;
    private double lastError =0;
    private double angleTolerance =0.2;
    private double MAX_POWER = 0.6;
    private double power =0;


    private final ElapsedTime trackingTimer = new ElapsedTime();

    public void intt(HardwareMap hwmap,String SmallFlywheel_Motor,String BigFlyWheel_Motor,String Heading_Motor){
        small_M=hwmap.get(DcMotorEx.class,SmallFlywheel_Motor);
        small_M.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        PIDFCoefficients S_PIDfCoefficients = new PIDFCoefficients(0,0,0,0);
        small_M.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, S_PIDfCoefficients);

        big_M=hwmap.get(DcMotorEx.class,BigFlyWheel_Motor);
        big_M.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        PIDFCoefficients B_PIDFCoefficients = new PIDFCoefficients(0,0,0,0);
        big_M.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,B_PIDFCoefficients);

        heading_M=hwmap.get(DcMotorEx.class,Heading_Motor);
        heading_M.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        heading_M.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    public void setPoseSource(pose_source position) {
        this.robotPosition = position;
    }

    public void setkP(double newKP){
        kp=newKP;
    }

    public void setKD(double newKD){
        kD=newKD;
    }

    public double getkP(){
        return kp;
    }

    public double getKD(){
        return kD;
    }

    public void resetTimer() {
        trackingTimer.reset();
    }

    private double getTarget (boolean radians){
        robotPosition.update();

        double X = 58;
        double UP_Y = 84;
        double Down_Y = 56;

        /*double Blue_X = 84;
        double blueUP_Y = 84;
        double blueDown_Y = 56;*/

        double xoffset = 0;
        double yoffset = 0;
        double angleToTarget = 0;
        double distanceToTarget = 0;

        if (robotPosition.y() > 70.75) {
            xoffset = X - robotPosition.x();
            yoffset = UP_Y - robotPosition.y();
            angleToTarget = Math.atan2(yoffset, xoffset);
            distanceToTarget = Math.hypot(xoffset, yoffset);
         } else if (robotPosition.y() < 70.75) {
            xoffset = X - robotPosition.x();
            yoffset = Down_Y - robotPosition.y();
            angleToTarget = Math.atan2(yoffset, xoffset);
            distanceToTarget = Math.hypot(xoffset, yoffset);
        }

         /*else if ("blue".equalsIgnoreCase(team)) {
            if (robotPosition.y() > 70.75) {
                xoffset = Blue_X - robotPosition.x();
                yoffset = blueUP_Y - robotPosition.y();
                angleToTarget = Math.atan2(yoffset, xoffset);
                distanceToTarget = Math.hypot(xoffset, yoffset);

            } else if (robotPosition.y() < 70.75) {
                xoffset = Blue_X - robotPosition.x();
                yoffset = blueDown_Y - robotPosition.y();
                angleToTarget = Math.atan2(yoffset, xoffset);
                distanceToTarget = Math.hypot(xoffset, yoffset);
            }
        }*/

        if (radians) {
            return angleToTarget;
        } else if (!radians) {
            return distanceToTarget;
        }
        return 0.0;
    }

    public void UpdateVelocity(){
        double Bvelocity;
        double Svelocity;
        Bvelocity=1+2-getTarget(false);
        Svelocity=1+2-getTarget(false);
        small_M.setVelocity(Svelocity);
        big_M.setVelocity(Bvelocity);

    }

    public void start_tracking() {
        if (robotPosition == null) {
            return;
        }

        double deltaTime = trackingTimer.seconds();
        trackingTimer.reset();
        double error =getTarget(true)-robotPosition.heading();
        final double M_limit =175;
        final double MIN_limit =-175;
        double heading =heading_M.getCurrentPosition();
        double currentDegrees = (heading/ 103.8) * 360.0;

        if (error > Math.PI){error -= 2 * Math.PI;}
        if (error < -Math.PI){error += 2 * Math.PI;}

        double pTerm = error * kp;

        double dTerm =0;
        if(deltaTime > 0){
            dTerm =((error - lastError)/deltaTime)* kD;
        }

        if(Math.abs(error) < angleTolerance){
            power=0;
        }else {
            power= Range.clip(pTerm + dTerm,-MAX_POWER,MAX_POWER);
        }

        if((Math.toDegrees(error)+ currentDegrees) >M_limit || (Math.toDegrees(error)+ currentDegrees) < MIN_limit){
            power=-power;
        }

        UpdateVelocity();

        heading_M.setPower(power);
        lastError = error;

    }

    public void shoot (Intake intake,centerControl centerControl){
            centerControl.shooter_gate(true);
            intake.setOveride(true);
            intake.intake("run");
    }

}
