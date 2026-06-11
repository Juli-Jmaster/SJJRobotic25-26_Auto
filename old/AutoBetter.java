package org.firstinspires.ftc.teamcode.old;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.IgnoreConfigurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.library.Subsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.AutoUtils;
import org.firstinspires.ftc.teamcode.pedroPathing.ConstantsCanada;

//@Autonomous(name = "AutoBetter", group = "Examples")
public class AutoBetter extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    private Timer pathTimer;
    private AutoUtils autoUtils;
    private Follower follower;
    private PathsBetter paths; // Paths defined in the Paths class
    private boolean next = false;
    private boolean next2 = false;
    private boolean clear = false;
    private int pathState =0;
    private double IntakeOnX = 102;


    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        follower = ConstantsCanada.createFollower(hardwareMap);
        follower.setStartingPose(PathsBetter.startPose);
        paths = new PathsBetter(follower); // Build paths

        autoUtils = new AutoUtils();
        Subsystem.init(hardwareMap);
        autoUtils.limelightInit(hardwareMap);


        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void loop() {
//        autoUtils.turretAdjust(getRuntime());
        autonomousPathUpdate(); // Update autonomous state machine
        follower.update(); // Update Pedro Pathing

//        autoUtils.limelight(getRuntime());

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("isBusy", follower.isBusy());
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.debug("vel", Subsystem.outtake.getVelocity());
        panelsTelemetry.debug("tValue", follower.getCurrentTValue());
        panelsTelemetry.debug("error", autoUtils.error);
        panelsTelemetry.debug("imu",autoUtils.imu.getRobotYawPitchRollAngles().getYaw());
        panelsTelemetry.debug("running", autoUtils.running);
        panelsTelemetry.debug("setvel", Subsystem.closeVel);
        panelsTelemetry.debug("at power", (Subsystem.outtake.getVelocity() >= Subsystem.closeVel-60 && Subsystem.outtake.getVelocity() <= Subsystem.closeVel+30 ));
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void start() {
        super.start();
        autoUtils.limelightStart(getRuntime());
        Subsystem.startAutoClose();
        Subsystem.t1.setPosition(0.5);
        Subsystem.t2.setPosition(0.5);
//        Subsystem.outtake.setVelocity(0);
        pathTimer = new Timer();
//        Subsystem.intake.setPower(0.6);
//        hood.setPosition(0.4167);
//        outtake.setPower(1);
        pathState=0;
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(paths.path1, true);
                setPathState(1);
                break;
            case 1:
                shooting(2, paths.path2);
                break;
            case 2:
                if (follower.getPose().getX() > IntakeOnX){
                    Subsystem.intake.setPower(0.8);
//                    follower.setHeading(0);
                }
//                if(!follower.isBusy() && !next) {
//                    next=true;
//                    pathTimer.resetTimer();
//
//                }
//                if(!follower.isBusy() && next && pathTimer.getElapsedTimeSeconds() > 0.5) {
//                    follower.followPath(paths.path3, true);
//                    setPathState(3);
//                    next=false;
//                }
                if(!follower.isBusy()) {
                    follower.followPath(paths.path3, true);
                    setPathState(3);
                    next=false;
                }
                break;
            case 3:
                if (follower.getCurrentTValue() > .5){
                    Subsystem.intake.setPower(0.0);
                }
                if(!follower.isBusy()) {
                    follower.followPath(paths.path10, true);
                    setPathState(10);
                }
                break;
            case 4:
               shooting(5, paths.path5);
               break;
            case 5:
                if (follower.getPose().getX() > IntakeOnX){
                    Subsystem.intake.setPower(0.8);
//                    follower.setHeading(0);
                }
//                if(!follower.isBusy() && !next) {
//                    next=true;
//                    pathTimer.resetTimer();
//
//
//                }
//                if(!follower.isBusy() && next && pathTimer.getElapsedTimeSeconds() > 0.5) {
//                    follower.followPath(paths.path6, true);
//                    setPathState(6);
//                    next=false;
//                }
                if(!follower.isBusy()) {
                    follower.followPath(paths.path6, true);
                    setPathState(6);
                    next=false;
                }
                break;
            case 6:
                if (follower.getPose().getX() < IntakeOnX){
                    Subsystem.intake.setPower(0.0);
                }
                shooting(7, paths.path7);
                break;
            case 7:
                if (follower.getPose().getX() > IntakeOnX){
                    Subsystem.intake.setPower(0.8);
//                    follower.setHeading(0);
                }
//                if(!follower.isBusy() && !next) {
//                    next=true;
//                    pathTimer.resetTimer();
//
//                }
//                if(!follower.isBusy() && next && pathTimer.getElapsedTimeSeconds() > 0.5) {
//                    follower.followPath(paths.path8, true);
//                    setPathState(8);
//                    next=false;
//                }
                if(!follower.isBusy()) {
                    follower.followPath(paths.path8, true);
                    setPathState(8);
                    next=false;
                }
                break;
            case 8:
                if (follower.getPose().getX() < IntakeOnX){
                    Subsystem.intake.setPower(0.0);
                }
                shooting(9, paths.path2);
                //running to path 8
                break;
            case 9:
                break;
            case 10:
                Subsystem.intake.setPower(0.8);
                if(!follower.isBusy() && !next) {
//                    follower.followPath(paths.path11, true);
//                    setPathState(11);
                    next=true;
                    pathTimer.resetTimer();
                }
                if(!follower.isBusy() && next && pathTimer.getElapsedTimeSeconds() > 2) {
                    follower.followPath(paths.path11, true);
                    setPathState(11);
                    next=false;
                }
            case 11:
//                Subsystem.intake.setPower(0.0);
                if(!follower.isBusy()) {
                    follower.followPath(paths.path5, true);
                    setPathState(5);
                }
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
        if (!follower.isBusy()  && (Subsystem.outtake.getVelocity() >= Subsystem.closeVel-10 && Subsystem.outtake.getVelocity() <= Subsystem.closeVel+30 ) && autoUtils.limelight(getRuntime()) && !next){
            next=true;
            next2=true;
            follower.breakFollowing();
//            follower.setHeading();
            //Subsystem.stop.setPosition(0);
            Subsystem.intake.setPower(0.9);
            Subsystem.transfer.setPower(1);
            Subsystem.setPowerOuttake(1);

            pathTimer.resetTimer();

        }
        if(!follower.isBusy() && pathTimer.getElapsedTimeSeconds()>= 0.85 && next2){
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

class PathsBetter {
    public static Pose startPose = new Pose(114, 133, Math.toRadians(270));
    public static Pose scorePose = new Pose(93, 92, Math.toRadians(40));

    public static Pose pickup1Pose = new Pose(120, 84.5, Math.toRadians(0));
    public static Pose pickup2Pose = new Pose(120, 60, Math.toRadians(0));
    public static Pose pickup3Pose = new Pose(120, 35.5, Math.toRadians(0));

    public static Pose pickup1PoseCenter = new Pose(102, 82, Math.toRadians(0));
    public static Pose pickup2PoseCenter = new Pose(92, 55, Math.toRadians(0));
    public static Pose pickup3PoseCenter = new Pose(87, 30, Math.toRadians(0));

    public static Pose clearGatePose = new Pose(124, 75, Math.toRadians(0));
    public static Pose controlClearGatePose = new Pose(105, 75, Math.toRadians(0));

    public static Pose pickupGatePose = new Pose(132, 55, Math.toRadians(60));
    public static Pose pickupGateControlPose = new Pose(122, 61, Math.toRadians(0));


    @IgnoreConfigurable
    public PathChain path1, path2, path3, path4, path5, path6, path7, path8,path9, path10, path11, path12;

    public PathsBetter(Follower follower) {
        //start
        path1 = follower.pathBuilder().
                addPath(new BezierLine(
                        startPose,
                        scorePose)
                ).setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();
        //pick1 with gate score
        path2 = follower.pathBuilder().
                addPath(new BezierCurve(
                    scorePose,
                    pickup1PoseCenter,
                    pickup1Pose)
                ).setLinearHeadingInterpolation(0, pickup1Pose.getHeading())
                .build();
        path3 = follower.pathBuilder().
                addPath(new BezierCurve(
                        pickup1Pose,
                        controlClearGatePose,
                        clearGatePose)
                ).setLinearHeadingInterpolation(pickup1Pose.getHeading(), clearGatePose.getHeading())
                .build();
        path4 = follower.pathBuilder().
                addPath(new BezierLine(
                        clearGatePose,
                        scorePose)
                ).setLinearHeadingInterpolation(clearGatePose.getHeading(), scorePose.getHeading())
                .build();
        //pick2 score
        path5 = follower.pathBuilder().
                addPath(new BezierCurve(
                        scorePose,
                        pickup2PoseCenter,
                        pickup2Pose)
                ).setLinearHeadingInterpolation(0, pickup2Pose.getHeading())
                .build();
        path6 = follower.pathBuilder().
                addPath(new BezierLine(
                        pickup2Pose,
                        scorePose)
                ).setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
                .build();
        //pick3 score
        path7 = follower.pathBuilder().
                addPath(new BezierCurve(
                        scorePose,
                        pickup3PoseCenter,
                        pickup3Pose)
                ).setLinearHeadingInterpolation(0, pickup3Pose.getHeading())
                .build();
        path8 = follower.pathBuilder().
                addPath(new BezierLine(
                        pickup3Pose,
                        scorePose)
                ).setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .build();
        path10 = follower.pathBuilder().addPath(new BezierCurve(
                clearGatePose, pickupGateControlPose ,pickupGatePose
        )).setLinearHeadingInterpolation(clearGatePose.getHeading(), pickupGatePose.getHeading()).build();

        path11 = follower.pathBuilder().addPath(new BezierLine(
                pickupGatePose, scorePose
        )).setLinearHeadingInterpolation(pickupGatePose.getHeading(), scorePose.getHeading()).build();
    }

}