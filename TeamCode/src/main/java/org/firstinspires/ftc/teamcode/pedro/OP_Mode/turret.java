package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class turret {
    private pose_source robotPosition;
    private DcMotorEx small_M;
    private DcMotorEx big_M;
    private DcMotorEx heading_M;
    private String Team;
    private double kp =0.0001;
    private double kD =0.0000;
    private double lastError =0;
    private double angleTolerance =0.2;
    private double MAX_POWER = 0.6;
    private double power =0;
    private final ElapsedTime trackingTimer = new ElapsedTime();

    public void intt(HardwareMap hwmap,String SmallFlywheel_Motor,String BigFlyWheel_Motor,String Heading_Motor){
        small_M=hwmap.get(DcMotorEx.class,SmallFlywheel_Motor);
        big_M=hwmap.get(DcMotorEx.class,BigFlyWheel_Motor);
        heading_M=hwmap.get(DcMotorEx.class,Heading_Motor);
        heading_M.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        heading_M.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    public void setPoseSource(pose_source position) {
        this.robotPosition = position;
    }

    public void setup(String team) {
        if ("red".equalsIgnoreCase(team)) {
            Team = "Red";
        } else if ("blue".equalsIgnoreCase(team)) {
            Team = "Blue";
        }
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

    private double getTarget (String team,boolean angle){
        robotPosition.update();

        double Red_X = 58;
        double redUP_Y = 84;
        double redDown_Y = 56;

        double Blue_X = 84;
        double blueUP_Y = 84;
        double blueDown_Y = 56;

        double xoffset = 0;
        double yoffset = 0;
        double angleToTarget = 0;
        double distanceToTarget = 0;

        if ("red".equalsIgnoreCase(team)) {
            if (robotPosition.y() > 70.75) {
                xoffset = Red_X - robotPosition.x();
                yoffset = redUP_Y - robotPosition.y();
                angleToTarget = Math.atan2(yoffset, xoffset);
                distanceToTarget = Math.hypot(xoffset, yoffset);

            } else if (robotPosition.y() < 70.75) {
                xoffset = Red_X - robotPosition.x();
                yoffset = redDown_Y - robotPosition.y();
                angleToTarget = Math.atan2(yoffset, xoffset);
                distanceToTarget = Math.hypot(xoffset, yoffset);
            }

        } else if ("blue".equalsIgnoreCase(team)) {
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
        }

        if (angle) {
            return angleToTarget;
        } else if (!angle) {
            return distanceToTarget;
        }
        return 0.0;
    }

    public void start_tracking() {
        if (robotPosition == null) {
            return;
        }

        double deltaTime = trackingTimer.seconds();
        double error =getTarget(Team,true)-Math.toDegrees(robotPosition.heading());
        double pTerm = error * kp;
        trackingTimer.reset();
        if (error > 180){error -= 360;}
        if (error < 180){error += 360;}



        double dTerm =0;
        if(deltaTime > 0){
            dTerm =((error - lastError)/deltaTime)* kD;
        }

        if(Math.abs(error) < angleTolerance){
            power=0;
        }else {
            power= Range.clip(pTerm + dTerm,-MAX_POWER,MAX_POWER);
        }

        heading_M.setPower(power);
        lastError = error;

    }

}
