package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class flywheel_tuner extends OpMode {
    private DcMotorEx Sm_big;
    private DcMotorEx Sm_small;
    public double highVelocity = 1500;
    public double lowVelocity = 900;
    public double B_highVelocity = 1500;
    public double B_LowVelocity = 900;
    double curTargetVelocity = highVelocity;
    double B_curTargetVelocity = B_highVelocity;
    double sF = 0;
    double sP = 0;
    double bF = 0;
    double bP = 0;
    double[] tuneSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};
    int stepIndex = 0;

    @Override
    public void init() {
        Sm_big = hardwareMap.get(DcMotorEx.class, "Big_motor");
        Sm_small = hardwareMap.get(DcMotorEx.class, "Small_motor");
        Sm_big.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Sm_small.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Sm_big.setDirection(DcMotorSimple.Direction.REVERSE);
        Sm_small.setDirection(DcMotorSimple.Direction.REVERSE);
        PIDFCoefficients smallPidfCoefficients = new PIDFCoefficients(sP, 0, 0, sF);
        PIDFCoefficients bigPidfCoefficients = new PIDFCoefficients(bP, 0, 0, bF);
        Sm_small.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, smallPidfCoefficients);
        Sm_big.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, bigPidfCoefficients);
    }

    @Override
    public void loop() {
        if (gamepad1.rightBumperWasPressed()) {
            if (curTargetVelocity == highVelocity) {
                curTargetVelocity = lowVelocity;
                B_curTargetVelocity = B_LowVelocity;
            } else {
                curTargetVelocity = highVelocity;
                B_curTargetVelocity = B_highVelocity;
            }
        }

        if (gamepad1.leftBumperWasPressed()) {
            stepIndex = (stepIndex + 1) % tuneSizes.length;
        }

        if (gamepad1.yWasPressed()) {
            sF += tuneSizes[stepIndex];
        }
        if (gamepad1.xWasPressed()) {
            sF -= tuneSizes[stepIndex];
        }
        if (gamepad1.aWasPressed()) {
            sP += tuneSizes[stepIndex];
        }
        if (gamepad1.bWasPressed()) {
            sP -= tuneSizes[stepIndex];
        }

        if (gamepad1.dpadUpWasPressed()) {
            bF += tuneSizes[stepIndex];
        }
        if (gamepad1.dpadDownWasPressed()) {
            bF -= tuneSizes[stepIndex];
        }
        if (gamepad1.dpadLeftWasPressed()) {
            bP += tuneSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed()) {
            bP -= tuneSizes[stepIndex];
        }

        PIDFCoefficients smallPidfCoefficients = new PIDFCoefficients(sP, 0, 0, sF);
        PIDFCoefficients bigPidfCoefficients = new PIDFCoefficients(bP, 0, 0, bF);
        Sm_small.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, smallPidfCoefficients);
        Sm_big.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, bigPidfCoefficients);

        Sm_small.setVelocity(curTargetVelocity);
        Sm_big.setVelocity(B_curTargetVelocity);

        double curVelocity = Sm_small.getVelocity();
        double B_curVelocity = Sm_big.getVelocity();
        double error = curTargetVelocity - curVelocity;
        double B_error = B_curTargetVelocity - B_curVelocity;

        telemetry.addData("tune step index", stepIndex);
        telemetry.addData("tune step size", tuneSizes[stepIndex]);
        telemetry.addData("Small wheel F", sF);
        telemetry.addData("Small wheel P", sP);

        telemetry.addData("Big wheel F", bF);
        telemetry.addData("Big wheel P", bP);

        telemetry.addData("sm_small Target Velocity", curTargetVelocity);
        telemetry.addData("sm_small Current Velocity", "%.2f", curVelocity);
        telemetry.addData("sm_small error", "%.2f", error);
        telemetry.addData("sm_big Target Velocity", B_curTargetVelocity);
        telemetry.addData("sm_big Current Velocity", "%.2f", B_curVelocity);
        telemetry.addData("sm_big error", "%.2f", B_error);
    }
}
