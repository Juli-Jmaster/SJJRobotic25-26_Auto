
package org.firstinspires.ftc.teamcode.old; // make sure this aligns with class location

import static org.firstinspires.ftc.teamcode.library.Subsystem.intake;

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
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.library.Subsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.AutoUtils;
import org.firstinspires.ftc.teamcode.pedroPathing.ConstantsNEW;

//@Autonomous(name = "AutoGate", group = "Examples")
public class AutoGate extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    private Timer pathTimer;
    private  AutoUtils autoUtils;
    private Follower follower;
    private PathsGate paths; // Paths defined in the Paths class
    private boolean next = false;
    private boolean next2 = false;
    private boolean clear = false;
    private int pathState =0;


    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        follower = ConstantsNEW.createFollower(hardwareMap);
        follower.setStartingPose(Paths.startPose);
        paths = new PathsGate(follower); // Build paths

        autoUtils = new AutoUtils();
//        Subsystem.init(hardwareMap);
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
        telemetry.addData("isBusy", follower.isBusy());
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.debug("next", next);
//        panelsTelemetry.debug("vel", outtake.getVelocity());
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void start() {
        super.start();
        autoUtils.limelightStart(getRuntime());
//        Subsystem.startAuto();
        next=false;
        pathTimer = new Timer();
//        Subsystem.intake.setPower(0.6);
//        hood.setPosition(0.4167);
//        outtake.setPower(1);
        pathState=0;
    }

    public void autonomousPathUpdate() {
        boolean gg=false;
        switch (pathState) {
            case 0:
                follower.followPath(paths.path1);
                setPathState(1);
                break;
            case 1:
                //shooting the ball
                //at shoot
                //   autoUtils.startVelocity();
                shooting(2, paths.path2);
//                setPathStateAndFollow(2, paths.Path2);
                break;
            case 2:

                //at start of pickup
                if(!follower.isBusy()) {
                    follower.followPath(paths.path3,true);
                    intake.setPower(0.9);
                    setPathState(3);
                }
                break;
            case 3:

                //at end of pickup
                if(!follower.isBusy()) {
                    follower.followPath(paths.path4,true);
                    intake.setPower(0.0);
                    setPathState(4);

                }

                break;
            case 4:
                //at scorePos
                Subsystem.intake.setPower(0.8);
                if(!follower.isBusy() && !next) {
                    next=true;
                    pathTimer.resetTimer();
                }
                if(!follower.isBusy() && next && pathTimer.getElapsedTimeSeconds() > 2) {
                    follower.followPath(paths.path5, true);
                    setPathState(5);
                    next=false;
                }
            case 5:
                follower.breakFollowing();
                //at start pickup 2
//                if(!follower.isBusy()) {
//                    follower.followPath(paths.path6,true);
//                    intake.setPower(0.9);
//                    setPathState(6);
//                }
                while(true){
                    intake.setPower(0.9);
                }
//                break;
            case 6:

                //at end pickup 2
                if(!follower.isBusy()) {
                    follower.followPath(paths.path7, true);
                    intake.setPower(0.6);
                    setPathState(7);
                }
                break;
            case 7:
                //at score
                shooting(8, paths.path8);
//                setPathStateAndFollow();
                break;
            case 8:
                //at start pickup 3
                if(!follower.isBusy()) {
                    intake.setPower(0.9);
                    follower.followPath(paths.path9, true);
                    setPathState(9);
                }
                break;
            case 9:
                //at end pickup 3
                if(!follower.isBusy()) {
                    intake.setPower(0.6);
                    follower.followPath(paths.path10, true);
                    setPathState(10);
                }
                break;
            case 10:
                shooting(12, paths.path11);
//                setPathStateAndFollow();
                break;
            case 11:
                if(!follower.isBusy() && clear && pathTimer.getElapsedTimeSeconds() < 0.2)  {
                    follower.followPath(paths.path4, true);
                    setPathState(4);
                    clear=false;
                }
                if(!follower.isBusy() && !clear) {
                    clear=true;
                    pathTimer.resetTimer();
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
        follower.followPath(path);
        pathTimer.resetTimer();
    }
    public void shooting(int nextState, PathChain nextPath){
        if (!follower.isBusy() && !next){
    //        Subsystem.stop.setPosition(0);
            follower.breakFollowing();
            next2=true;
        }
        if (!follower.isBusy()  && (Subsystem.outtake.getVelocity() >= Subsystem.closeVel-10 && Subsystem.outtake.getVelocity() <= Subsystem.closeVel+20 ) && autoUtils.limelight(getRuntime()) && next){
            next=false;
            next2=true;

            Subsystem.intake.setPower(0.9);
            Subsystem.transfer.setPower(0.9);
            Subsystem.outtake.setPower(1);

            pathTimer.resetTimer();

        }
        if(pathTimer.getElapsedTimeSeconds()>= 1 && next2){
            Subsystem.outtake.setVelocity(Subsystem.closeVel);
            Subsystem.intake.setPower(0);
            Subsystem.transfer.setPower(0);

            follower.followPath(nextPath,true);
            setPathState(nextState);
        //    Subsystem.stop.setPosition(0.2);
            next=false;
            next2=false;
        }
    }


}
class PathsGate {
    public static Pose startPose = new Pose(109, 132.5, Math.toRadians(270));
    public static Pose scorePose = new Pose(93, 92, Math.toRadians(40));
    public static Pose pickup1StartPose = new Pose(102, 83.5, Math.toRadians(0));
    public static Pose pickup1EndPose = new Pose(122, 83.5, Math.toRadians(0));
    public static Pose pickup2StartPose = new Pose(102, 60, Math.toRadians(0));
    public static Pose pickup2EndPose = new Pose(130, 60, Math.toRadians(40));
    public static Pose pickup3StartPose = new Pose(102, 35.5, Math.toRadians(0));
    public static Pose pickup3EndPose = new Pose(129, 35.5, Math.toRadians(0));

    public static Pose clearGatePose = new Pose(124, 75, Math.toRadians(0));
    public static Pose controlClearGatePose = new Pose(105, 75, Math.toRadians(0));

    public static Pose pickupGatePose = new Pose(132, 55, Math.toRadians(60));
    public static Pose pickupGateControlPose = new Pose(122, 61, Math.toRadians(0));

    @IgnoreConfigurable
    public PathChain path1, path2, path3, path4, path5, path6, path7, path8,path9, path10, path11, path12;

    public PathsGate(Follower follower) {
        path1 = follower.pathBuilder().
                addPath(new BezierLine(
                        startPose,
                        scorePose)
                ).setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();
        path2 = follower.pathBuilder().
                addPath(new BezierLine(
                        scorePose,
                        pickup1StartPose)
                ).setLinearHeadingInterpolation(scorePose.getHeading(), pickup1StartPose.getHeading())
                .build();
        path3 = follower.pathBuilder().
                addPath(new BezierLine(
                        pickup1StartPose,
                        pickup1EndPose)
                ).setLinearHeadingInterpolation(pickup1StartPose.getHeading(), pickup1EndPose.getHeading())
                .build();
        path4 = follower.pathBuilder().addPath(new BezierCurve(
                clearGatePose, pickupGateControlPose ,pickupGatePose
        )).setLinearHeadingInterpolation(clearGatePose.getHeading(), pickupGatePose.getHeading()).build();

        path5 = follower.pathBuilder().addPath(new BezierLine(
                pickupGatePose, scorePose
        )).setLinearHeadingInterpolation(pickupGatePose.getHeading(), scorePose.getHeading()).build();
//        path6 = follower.pathBuilder().
//                addPath(new BezierLine(
//                        pickup2StartPose,
//                        pickup2EndPose)
//                ).setLinearHeadingInterpolation(pickup2StartPose.getHeading(), pickup2EndPose.getHeading())
//                .build();
        path7 = follower.pathBuilder().
                addPath(new BezierLine(
                        pickup2EndPose,
                        scorePose)
                ).setLinearHeadingInterpolation(pickup2EndPose.getHeading(), scorePose.getHeading())
                .build();

        path8 = follower.pathBuilder().
                addPath(new BezierLine(
                        scorePose,
                        pickup3StartPose)
                ).setLinearHeadingInterpolation(scorePose.getHeading(), pickup3StartPose.getHeading())
                .build();
        path9 = follower.pathBuilder().
                addPath(new BezierLine(
                        pickup3StartPose,
                        pickup3EndPose)
                ).setLinearHeadingInterpolation(pickup3StartPose.getHeading(), pickup3EndPose.getHeading())
                .build();
        path10 = follower.pathBuilder().
                addPath(new BezierLine(
                        pickup3EndPose,
                        scorePose)
                ).setLinearHeadingInterpolation(pickup3EndPose.getHeading(), scorePose.getHeading())
                .build();
        path11 = follower.pathBuilder()
                .addPath(new BezierLine(
                        scorePose,
                        pickup1EndPose)
                ).setLinearHeadingInterpolation(scorePose.getHeading(), pickup1EndPose.getHeading())
                .build();
        path12 = follower.pathBuilder()
                .addPath(new BezierCurve(
                        pickup1EndPose,
                        controlClearGatePose,
                        clearGatePose)
                ).setLinearHeadingInterpolation(pickup1EndPose.getHeading(), clearGatePose.getHeading())
                .build();


    }

}
