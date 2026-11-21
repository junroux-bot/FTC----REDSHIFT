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
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;


import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class AUTO_wait extends OpMode {
    private Follower follower;
    turret turret = new turret();
    centerControl gate = new centerControl();
    Intake intake = new Intake();
    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startpose = p.of(56.75,134.75,270);
    private final Pose end = p.of(8.75,133.75,0);

    private Path path1() {
        return line(startpose,end).linear(startpose,end);
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
                delay(5),
                shoot(),
                follow(follower, path1())

        );

    }

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        follower.setPose(startpose);
        Scheduler.reset();
        follower.update();

        turret.intt(hardwareMap,"sfm","bfm","hm");
        intake.intt(hardwareMap,"dss","css","im","is");
        gate.intt(hardwareMap,"gs","lfs","rfs");

        pose_source Position = new pose_source();
        Position.init(hardwareMap, startpose.x(), startpose.y(), startpose.heading());
        turret.setPoseSource(Position);


    }

    @Override
    public void start() {
        schedule(autoRoutine());
        intake.set_ball_munber(4);
        turret.start_tracking();


    }

    @Override
    public void loop() {
        follower.update();
        turret.start_tracking();
        intake.run();

        Scheduler.execute();

    }

    @Override
    public void stop() {
        // SAVE ENDING POSE so TeleOp knows where Auto left the robot!
        intake.flower_gate("close");
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}




