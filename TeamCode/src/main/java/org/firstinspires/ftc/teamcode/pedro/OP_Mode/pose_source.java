package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;      // Pedro 3: com.pedropathing.math.Pose  (Pedro 2 used com.pedropathing.geometry.Pose)
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.Constants;   // ADJUST: wherever YOUR Pedro Constants class is

/**
 * The ONLY file that touches the Pedro API. Pedro only localizes here (Pinpoint pose);
 * it does not drive the robot (we never call followPath / startTeleOpDrive), Motor_Setup drives.
 *
 * If your Pedro version has different names, fix them in this file only:
 *   - the Pose import above
 *   - now(): follower.pose()  or  follower.getPose()
 *   - getX()/getY()/getHeading() on Pose (heading must be RADIANS)
 */
public class pose_source {
    private Follower follower;

    public void init(HardwareMap hw, double x, double y, double headingRad) {
        follower = Constants.create(hw);
        follower.setPose(new Pose(x, y, headingRad));
    }

    public void setPose(double x, double y, double headingRad) {
        follower.setPose(new Pose(x, y, headingRad));
    }

    /** Call EVERY loop. */
    public void update() {
        follower.update();
    }

    private Pose now() {
        return follower.pose();
    }

    public double x()       { return now().x(); }
    public double y()       { return now().y(); }
    public double heading() { return now().heading(); }   // radians
}