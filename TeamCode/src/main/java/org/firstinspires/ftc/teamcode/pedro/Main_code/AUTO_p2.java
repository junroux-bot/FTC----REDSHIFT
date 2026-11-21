package org.firstinspires.ftc.teamcode.pedro.Main_code;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import static com.pedropathing.api.Paths.*;
import static com.pedropathing.ivy.groups.Groups.sequential;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;


import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.util.ElapsedTime;
@Autonomous
public class AUTO_p2 extends OpMode{

    private Follower follower;
    turret turret = new turret();
    centerControl gate = new centerControl();
    Intake intake = new Intake();
    private final PoseFactory p = PoseFactory.degrees();
    private final Pose startPose = p.of(62.75, 135.75, 180);
    private final Pose path1 = p.of(46.75,129.75,90);
    private final Pose cppath1 =p.of(56.75,106.75,90);
    private final Pose path2 = p.of(12.75,46.75,180);
    private final Pose path3 = p.of(55.75,5.75,180);
    private final Pose path4 = p.of(8.75,5.75,180);
    private final Pose path5 =p.of(56.75,16.75,90);
    private final Pose park = p.of(7, 94, 90);

    private Path path1() {
        return curve(startPose,cppath1,path1).linear(startPose,path1);
    }

    private Path path2(){
        return line(path1, path2).linear(path1, path2);
    }

    private Path path3(){
        return line(path2,path3).linear(path2,path3);
    }

    private Path path4(){
        return line(path3,path4).linear(path3,path4);
    }

    private Path path5(){
        return line(path4,path5).tangent();
    }

    private Path end(){
        return line(path5,park).tangent();
    }

    private Command delay(double seconds) {
        ElapsedTime timer = new ElapsedTime();
        return new CommandBuilder()
                .setStart(timer::reset)                     // Reset timer when command starts
                .setDone(() -> timer.seconds() >= seconds); // Done when timer reaches 1.0s
    }

    private Command Gate(boolean mode) {
        return new CommandBuilder()
                .setStart(() -> gate.shooter_gate(mode))
                .setDone(() -> true);
    }

    private Command resetIntake() {
        return new CommandBuilder()
                .setStart(() -> intake.reset())
                .setDone(() -> true);
    }

    private Command runIntake() {
        return new CommandBuilder()
                .setStart(() -> {
                    intake.setOveride(true);
                    intake.intake("run");
                })
                .setDone(() -> true);
    }

    private Command stopintake(){
        return new CommandBuilder()
                .setStart(() -> {
                    intake.intake("stop");
                    intake.setOveride(false);
                })
                .setDone(() -> true);
    }
    private Command shoot(){
        return sequential(
                Gate(true),
                runIntake(),
                delay(1.5),
                stopintake(),
                Gate(false),
                resetIntake()

        );
    }

    private Command FlowerRamp(){
        return new CommandBuilder()
                .setStart(() -> intake.flower_gate("open"))
                .setDone(() -> true);
    }


    private Command autoRoutine() {
        return sequential(
                delay(1),
                shoot(),
                follow(follower,path1()),
                follow(follower,path2()),
                shoot(),
                FlowerRamp(),
                follow(follower,path3()),
                shoot(),
                delay(4),
                follow(follower, path4()),
                follow(follower,path5()),
                shoot(),
                follow(follower,end())

        );
    }
}
