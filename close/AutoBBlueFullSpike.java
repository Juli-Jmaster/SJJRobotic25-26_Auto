package org.firstinspires.ftc.teamcode.close;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.paths.PathBlue;
import org.firstinspires.ftc.teamcode.library.Subsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.AutoUtils;
import org.firstinspires.ftc.teamcode.pedroPathing.ConstantsCanada;

@Autonomous(name = "Blue full spike", group = "blue")
public class AutoBBlueFullSpike extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    private Timer pathTimer;
    private AutoUtils autoUtils;
    private Follower follower;
    private PathBBlueFullSpike paths; // Paths defined in the Paths class
    private boolean next = false;
    private boolean next2 = false;
    private boolean next3 = false;
    private boolean next4 = false;
    private int pathState =0;
    private double IntakeOnXBlue = 42;


    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        follower = ConstantsCanada.createFollower(hardwareMap);
        follower.setStartingPose(PathBlue.startPose);
        paths = new PathBBlueFullSpike(follower); // Build paths

        autoUtils = new AutoUtils();
        Subsystem.init(hardwareMap);
        autoUtils.limelightInit(hardwareMap);


        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void loop() {
        autonomousPathUpdate(); // Update autonomous state machine
        follower.update(); // Update Pedro Pathing


        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("isBusy", follower.isBusy());
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.debug("vel", Subsystem.outtake.getVelocity());
        panelsTelemetry.debug("tValue", follower.getCurrentTValue());
        panelsTelemetry.debug("setvel", Subsystem.closeVel);
        panelsTelemetry.debug("at power", (Subsystem.outtake.getVelocity() >= Subsystem.closeVel-60 && Subsystem.outtake.getVelocity() <= Subsystem.closeVel+30 ));
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void start() {
        super.start();
        autoUtils.limelightStart(getRuntime());
        Subsystem.startAutoClose();
        autoUtils.closeAutoAimBlue();
        Subsystem.t1.setPosition(0.5);
        Subsystem.t2.setPosition(0.5);
        pathTimer = new Timer();
        pathState=0;
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                setPathStateAndFollow(1, paths.path1);
                break;
            case 1:
                shooting(2, paths.path2);
                break;
            case 2:
                AtIntakeXOnTransferBlue();
                notBusyNextState(3, paths.path3);
                break;
            case 3:
                PassIntakeXOffTransferBlue();
                shooting(4, paths.path4);
                break;
            case 4:
                notBusyNextState(5, paths.path5);
                break;
            case 5:
                gateIntake(6, paths.path6);
                break;
            case 6:
                PassIntakeXOffTransferBlue();
                shooting(7, paths.path7);
                break;
            case 7:
                AtIntakeXOnTransferBlue();
                notBusyNextState(8, paths.path8);
                break;
            case 8:
                PassIntakeXOffTransferBlue();
                shooting(9, paths.path9);
                break;
            case 9:
                AtIntakeXOnTransferBlue();
                notBusyNextState(10, paths.path10);
                break;
            case 10:
                PassIntakeXOffTransferBlue();
                shooting(11, paths.path7);
                break;
        }
    }

    public void gateIntake(int pState, PathChain paths) {
        Subsystem.intake.setPower(0.9);
        Subsystem.transfer.setPower(-0.5);
        if (!follower.isBusy() && !next3) {
            next3 = true;
            pathTimer.resetTimer();
        }
        if (!follower.isBusy() && next3 && pathTimer.getElapsedTimeSeconds() > 1.75) {
            setPathStateAndFollow(pState, paths);
        }
    }


    private void AtIntakeXOnTransferBlue() {
        if (follower.getPose().getX() < IntakeOnXBlue){
            Subsystem.intake.setPower(0.9);
            Subsystem.transfer.setPower(-.6);
        }
    }
    
    private void PassIntakeXOffTransferBlue() {
        if (follower.getPose().getX() > IntakeOnXBlue && follower.isBusy()){
            Subsystem.intake.setPower(0.0);
            Subsystem.transfer.setPower(0.0);
        }
    }
    private void notBusyNextState(int pState, PathChain path){
        if(!follower.isBusy()) {
            setPathStateAndFollow(pState, path);
        }
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }
    public void setPathStateAndFollow(int pState, PathChain path) {
        pathState = pState;
        follower.followPath(path, true);
        pathTimer.resetTimer();
    }
    public void shooting(int nextState, PathChain nextPath){
        if (!follower.isBusy() && !next){
            next=true;
            pathTimer.resetTimer();
        }
        if (!follower.isBusy()  && (Subsystem.outtake.getVelocity() >= Subsystem.closeVel-10 && Subsystem.outtake.getVelocity() <= Subsystem.closeVel+80 ) && (autoUtils.limelight(getRuntime()) || pathTimer.getElapsedTimeSeconds() > 1.25)&& next){
            next=false;
            next2=true;
            autoUtils.shooting=true;
            follower.breakFollowing();
            //Subsystem.stop.setPosition(0);
            Subsystem.intake.setPower(0.9);
            Subsystem.transfer.setPower(1);
            Subsystem.setPowerOuttake(1);

            pathTimer.resetTimer();

        }
        if(!follower.isBusy() && pathTimer.getElapsedTimeSeconds()>= .65 && next2){
            autoUtils.shooting=false;
            Subsystem.setVelocityOuttake(Subsystem.closeVel);
            Subsystem.intake.setPower(0);
            Subsystem.transfer.setPower(0);

            setPathStateAndFollow(nextState, nextPath);
            //    Subsystem.stop.setPosition(0.2);
            next=false;
            next2=false;
        }
    }

}

class PathBBlueFullSpike {

    public PathChain path1, path2, path3, path4, path5, path6, path7, path8,path9, path10, path11, path12;

    public PathBBlueFullSpike(Follower follower) {
        //start
        path1 = follower.pathBuilder().addPath( new BezierLine(
                         PathBlue.startPose,  PathBlue.scorePose))
                .setLinearHeadingInterpolation( PathBlue.startPose.getHeading(),  PathBlue.scorePose.getHeading())
                .build();
        path2 = follower.pathBuilder().addPath(new BezierCurve(
                         PathBlue.scorePose,  PathBlue.pickup2PoseCenter,  PathBlue.pickup2Pose))
                .setLinearHeadingInterpolation(Subsystem.faceGoalBlue,  PathBlue.pickup2Pose.getHeading())
                .build();
        path3 = follower.pathBuilder().addPath( new BezierLine(
                         PathBlue.pickup2Pose,  PathBlue.scorePose))
                .setLinearHeadingInterpolation( PathBlue.pickup2Pose.getHeading(),  PathBlue.scorePose.getHeading())
                .build();
        path4 = follower.pathBuilder().addPath( new BezierCurve(
                         PathBlue.scorePose,  PathBlue.clearGateControlPoseFromScore,  PathBlue.clearGatePoseFromScore))
                .setLinearHeadingInterpolation(Subsystem.faceGoalBlue,  PathBlue.clearGatePoseFromScore.getHeading())
                .build();
        path5 = follower.pathBuilder().addPath(new BezierLine(
                         PathBlue.clearGatePoseFromScore,  PathBlue.pickupGatePose))
                .setLinearHeadingInterpolation( PathBlue.clearGatePoseFromScore.getHeading(),  PathBlue.pickupGatePose.getHeading())
                .build();
        path6 = follower.pathBuilder().addPath(new BezierCurve(
                         PathBlue.pickupGatePose,  PathBlue.pickup2PoseCenter,  PathBlue.scorePose))
                .setLinearHeadingInterpolation( PathBlue.pickupGatePose.getHeading(),  PathBlue.scorePose.getHeading())
                .build();
        path7 = follower.pathBuilder().addPath(new BezierLine(
                         PathBlue.scorePose,  PathBlue.pickup1Pose))
                .setLinearHeadingInterpolation(Subsystem.faceGoalBlue,  PathBlue.pickup1Pose.getHeading())
                .build();
        path8 = follower.pathBuilder().addPath(new BezierLine(
                         PathBlue.pickup1Pose,  PathBlue.scorePose))
                .setLinearHeadingInterpolation( PathBlue.pickup1Pose.getHeading(),  PathBlue.scorePose.getHeading())
                .build();
        path9 = follower.pathBuilder().addPath( new BezierCurve(
                         PathBlue.scorePose,  PathBlue.pickup3PoseCenter,  PathBlue.pickup3Pose))
                .setLinearHeadingInterpolation(Subsystem.faceGoalBlue,  PathBlue.pickup3Pose.getHeading())
                .build();
        path10 = follower.pathBuilder().addPath(new BezierLine(
                         PathBlue.pickup3Pose,  PathBlue.scorePose))
                .setLinearHeadingInterpolation( PathBlue.pickup3Pose.getHeading(),  PathBlue.scorePose.getHeading())
                .build();
    }

}
