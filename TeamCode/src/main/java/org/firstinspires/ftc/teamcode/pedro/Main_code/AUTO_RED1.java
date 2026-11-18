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
public class AUTO_RED1 extends OpMode {

    private Follower follower;
    turret turret = new turret();
    centerControl gate = new centerControl();
    Intake intake = new Intake();
    private final PoseFactory p = PoseFactory.degrees();
    private final Pose startPose = p.of(56, 8, 90);
    private final Pose path1 = p.of(47,132,90);
    private final Pose cppath1 =p.of(75,83,90);
    private final Pose path2 = p.of(10,47,180);
    private final Pose path3 = p.of(31,9,90);
    private final Pose park = p.of(8, 96, 90);


    private Path path1() {
        return curve(startPose, cppath1, path1).linear(startPose, path1);
    }

    private Path path2(){
        return line(path1, path2).linear(path1, path2);
    }

    private Path path3(){
        return line(path2,path3).linear(path2,path3);
    }

    private Path end(){
        return line(path3,park).linear(path3,park);
    }

    private Command delay(double seconds) {
        ElapsedTime timer = new ElapsedTime();
        return new CommandBuilder()
                .setStart(timer::reset)                     // Reset timer when command starts
                .setDone(() -> timer.seconds() >= seconds); // Done when timer reaches 1.0s
    }

    private Command Gate() {
        return new CommandBuilder()
                .setStart(() -> gate.shooter_gate())
                .setDone(() -> true);
    }

    private Command resetIntake() {
        return new CommandBuilder()
                .setStart(() -> intake.reset())
                .setDone(() -> true);
    }

    private Command runIntake(){
        return new CommandBuilder()
                .setStart(() -> intake.intake("run"))
                .setDone(() -> true);
    }

    private Command shoot(){
        return sequential(
                Gate(),
                delay(1.5),
                Gate(),
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
                parallel(follow(follower,path1()), FlowerRamp()),
                follow(follower,path2()),
                follow(follower,path3()),
                shoot(),
                follow(follower, end())

        );
    }

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        Scheduler.reset();
        follower.update();

        turret.intt(hardwareMap,"sfm","bfm","hm");
        intake.intt(hardwareMap,"dss","css","im","is");
        gate.intt(hardwareMap,"gs","lfs","rfs");
        turret.setup("red");


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
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}
