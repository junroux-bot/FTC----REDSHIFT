package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Motor_Setup {
    private DcMotor flm, blm, frm, brm;
    public IMU_Setup imu = new IMU_Setup();

    public void init(HardwareMap hwmap,String fl,String bl,String fr,String br) {
        flm = hwmap.get(DcMotor.class, fl);
        blm = hwmap.get(DcMotor.class, bl);
        frm = hwmap.get(DcMotor.class, fr);
        brm = hwmap.get(DcMotor.class, br);

        flm.setDirection(DcMotor.Direction.REVERSE);
        blm.setDirection(DcMotor.Direction.REVERSE);

        flm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        blm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        brm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        flm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        blm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        brm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu.intt(hwmap);
    }

    public void drive(double y, double x, double rx) {
        double flp = y + x + rx;
        double blp = y - x + rx;
        double frp = y - x - rx;
        double brp = y + x - rx;

        double maxp = Math.max(1.0, Math.max(Math.abs(flp), Math.max(Math.abs(blp),
                Math.max(Math.abs(frp), Math.abs(brp)))));

        flm.setPower(flp / maxp);
        blm.setPower(blp / maxp);
        frm.setPower(frp / maxp);
        brm.setPower(brp / maxp);
    }

    public void drive_field(double y, double x, double rx) {
        double botHeading = imu.getHeading(AngleUnit.RADIANS);

        double rotStrafe = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotForward = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        this.drive(rotForward, rotStrafe, rx);
    }

    public void Reset_heading(){
        imu.Reset_imu();
    }
}