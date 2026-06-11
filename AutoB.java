package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.PathRedFar;
import org.firstinspires.ftc.teamcode.library.Subsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.AutoUtils;
import org.firstinspires.ftc.teamcode.pedroPathing.ConstantsCanada;

@Autonomous
public class AutoB extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    private Timer pathTimer;
    private AutoUtils autoUtils;
    private Follower follower;
    private PathsBFar paths; // Paths defined in the Paths class
    private boolean next = false;
    private boolean next2 = false;
    private boolean next3 = false;
    private boolean next4 = false;
    private int pathState =0;
    private double IntakeOnXRed = 102;


    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        follower = ConstantsCanada.createFollower(hardwareMap);
        follower.setStartingPose(PathRedFar.startPose);
        paths = new PathsBFar(follower); // Build paths

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
        panelsTelemetry.debug("setvel", Subsystem.farVel);
        panelsTelemetry.debug("at power", (Subsystem.outtake.getVelocity() >= Subsystem.farVel-60 && Subsystem.outtake.getVelocity() <= Subsystem.farVel+30 ));
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void start() {
        super.start();
        autoUtils.limelightStart(getRuntime());
        Subsystem.startAutoFar();
        autoUtils.farAutoAimRed();
        Subsystem.t1.setPosition(0.1061);
        Subsystem.t2.setPosition(0.1061);
        pathTimer = new Timer();
        pathState=0;
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                shooting(1, paths.path1);
                break;
            case 1:
                AtIntakeXOnTransferRed();
                notBusyNextState(2, paths.path2);
                break;
            case 2:
                PassIntakeXOffTransferRed();
                shooting(3, paths.path3);
//                shooting(3, paths.path3);
                break;
            case 3:
                AtIntakeXOnTransferRed();
                notBusyNextState(4, paths.path4);
                break;
            case 4:
                PassIntakeXOffTransferRed();
                HoldIntakeShoot(5, paths.path5);
                break;
            case 5:
                next4=false;
                AtIntakeXOnTransferRed();
                notBusyNextState(6, paths.path6);
                break;
            case 6:
                PassIntakeXOffTransferRed();
                shooting(7, paths.path7);
                break;
                /// ////repeat
            case 7:
                AtIntakeXOnTransferRed();
                notBusyNextState(8, paths.path8);
                break;
            case 8:
                PassIntakeXOffTransferRed();
                HoldIntakeShoot(9, paths.path9);
                break;
            case 9:
                next4=false;
                AtIntakeXOnTransferRed();
                notBusyNextState(10, paths.path10);
                break;
            case 10:
                PassIntakeXOffTransferRed();
                HoldIntakeShoot(11, paths.path9);
                break;
//            case 8:
//                PassIntakeXOffTransferRed();
//                shooting(9, paths.path9);
//                break;
//            case 9:
//                notBusyNextState(10, paths.path10);
//                break;
//            case 10:
//                gateIntake(11, paths.path11);
//                break;
//            case 11:
//                PassIntakeXOffTransferRed();
//                shooting(12, paths.path7);
        }
    }

    private void AtIntakeXOnTransferRed() {
        if (follower.getPose().getX() > IntakeOnXRed){
            Subsystem.intake.setPower(0.9);
            Subsystem.transfer.setPower(-.6);
        }
    }

    private void PassIntakeXOffTransferRed() {
        if (follower.getPose().getX() < IntakeOnXRed && follower.isBusy()){
            Subsystem.intake.setPower(0.0);
            Subsystem.transfer.setPower(0.0);
        }
    }
    private void HoldIntakeShoot(int pState, PathChain path) {
        if (!follower.isBusy() && !next3 && !next4){
            next3=true;
            Subsystem.intake.setPower(0.8);
            Subsystem.transfer.setPower(-.6);
            pathTimer.resetTimer();
        }
        if (!follower.isBusy() && next3 && pathTimer.getElapsedTimeSeconds() > .25){
            next3=false;
            next4=true;
            Subsystem.intake.setPower(0.0);
            Subsystem.transfer.setPower(0.0);
        }
        if (!follower.isBusy() && next4){
            shooting(pState, path);
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
        if (!follower.isBusy()  && (Subsystem.outtake.getVelocity() >= Subsystem.farVel-10 && Subsystem.outtake.getVelocity() <= Subsystem.farVel+80 ) && (autoUtils.limelight(getRuntime()) || pathTimer.getElapsedTimeSeconds() > 1)&& next){
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
            Subsystem.setVelocityOuttake(Subsystem.farVel);
            Subsystem.intake.setPower(0);
            Subsystem.transfer.setPower(0);

            setPathStateAndFollow(nextState, nextPath);
            //    Subsystem.stop.setPosition(0.2);
            next=false;
            next2=false;
        }
    }

}

class PathsBFar {
    public PathChain path1, path2, path3, path4, path5, path6, path7, path8,path9, path10, path11, path12;

    public PathsBFar(Follower follower) {
        //start
        path1 = follower.pathBuilder().addPath( new BezierCurve(
                        PathRedFar.startPose, PathRedFar.pickup3Control1Pose, PathRedFar.pickup3Control2Pose, PathRedFar.pickup3Pose))
                .setLinearHeadingInterpolation(PathRedFar.startPose.getHeading(), PathRedFar.pickup3Pose.getHeading())
                .build();
        path2 = follower.pathBuilder().addPath( new BezierLine(
                        PathRedFar.pickup3Pose, PathRedFar.scorePose))
                .setLinearHeadingInterpolation(PathRedFar.pickup3Pose.getHeading(), PathRedFar.scorePose.getHeading())
                .build();
        path3 = follower.pathBuilder().addPath( new BezierLine(
                        PathRedFar.scorePose, PathRedFar.pickupHumanPose))
                .setLinearHeadingInterpolation(PathRedFar.scorePose.getHeading(), PathRedFar.pickupHumanPose.getHeading())
                .build();
        path4 = follower.pathBuilder().addPath(new BezierLine(
                        PathRedFar.pickupHumanPose,  PathRedFar.scorePose))
                .setLinearHeadingInterpolation(0, PathRedFar.scorePose.getHeading())
                .build();
        path5 = follower.pathBuilder().addPath(new BezierCurve(
                        PathRedFar.scorePose, PathRedFar.pickupLineControlPose, PathRedFar.pickupLinePose))
                .setLinearHeadingInterpolation(PathRedFar.scorePose.getHeading(), PathRedFar.pickupLinePose.getHeading())
                .build();
        path6 = follower.pathBuilder().addPath(new BezierLine(
                        PathRedFar.pickupLinePose, PathRedFar.scorePose))
                .setLinearHeadingInterpolation(PathRedFar.pickupLinePose.getHeading(), PathRedFar.scorePose.getHeading())
                .build();

        path7 = follower.pathBuilder().addPath( new BezierLine(
                        PathRedFar.scorePose, PathRedFar.pickupHumanPose))
                .setLinearHeadingInterpolation(PathRedFar.scorePose.getHeading(), PathRedFar.pickupHumanPose.getHeading())
                .build();
        path8 = follower.pathBuilder().addPath(new BezierLine(
                        PathRedFar.pickupHumanPose,  PathRedFar.scorePose))
                .setLinearHeadingInterpolation(0, PathRedFar.scorePose.getHeading())
                .build();
        path9 = follower.pathBuilder().addPath( new BezierLine(
                        PathRedFar.scorePose, PathRedFar.pickupHumanPose))
                .setLinearHeadingInterpolation(PathRedFar.scorePose.getHeading(), PathRedFar.pickupHumanPose.getHeading())
                .build();
        path10 = follower.pathBuilder().addPath(new BezierLine(
                        PathRedFar.pickupHumanPose,  PathRedFar.scorePose))
                .setLinearHeadingInterpolation(0, PathRedFar.scorePose.getHeading())
                .build();
//        path7 = follower.pathBuilder().addPath(new BezierLine(
//                        PathRed.scorePose, PathRed.pickup1Pose))
//                .setLinearHeadingInterpolation(0, PathRed.pickup1Pose.getHeading())
//                .build();
//        path8 = follower.pathBuilder().addPath(new BezierLine(
//                        PathRed.pickup1Pose, PathRed.scorePose))
//                .setLinearHeadingInterpolation(PathRed.pickup1Pose.getHeading(), PathRed.scorePose.getHeading())
//                .build();
//        path9 = follower.pathBuilder().addPath( new BezierCurve(
//                        PathRed.scorePose, PathRed.clearGateControlPoseFromScore, PathRed.clearGatePoseFromScore))
//                .setLinearHeadingInterpolation(0, PathRed.clearGatePoseFromScore.getHeading())
//                .build();
//        path10 = follower.pathBuilder().addPath(new BezierLine(
//                        PathRed.clearGatePoseFromScore, PathRed.pickupGatePose))
//                .setLinearHeadingInterpolation(PathRed.clearGatePoseFromScore.getHeading(), PathRed.pickupGatePose.getHeading())
//                .build();
//        path11 = follower.pathBuilder().addPath(new BezierCurve(
//                        PathRed.pickupGatePose, PathRed.pickup2PoseCenter, PathRed.scorePose))
//                .setLinearHeadingInterpolation(PathRed.pickupGatePose.getHeading(), PathRed.scorePose.getHeading())
//                .build();
    }

}
