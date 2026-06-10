
package org.firstinspires.ftc.teamcode.old; // make sure this aligns with class location

import static org.firstinspires.ftc.teamcode.library.Subsystem.intake;
import static org.firstinspires.ftc.teamcode.library.Subsystem.outtake;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.IgnoreConfigurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.library.Subsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.AutoUtils;
import org.firstinspires.ftc.teamcode.pedroPathing.ConstantsNEW;

//@Autonomous(name = "Auto2Far", group = "Examples")
public class Auto2Far extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    private Timer pathTimer;
    private  AutoUtils autoUtils;
    private Follower follower;
    private Paths2far paths; // Paths defined in the Paths class
    private boolean next = false;
    private boolean next2 = false;
    private boolean clear = false;
    private int pathState =0;


    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        follower = ConstantsNEW.createFollower(hardwareMap);
        follower.setStartingPose(Paths2far.startPose);
        paths = new Paths2far(follower); // Build paths

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
        telemetry.addData("isBusy", follower.isBusy());
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.debug("next", next);
        panelsTelemetry.debug("clear", clear);
        panelsTelemetry.debug("vel", outtake.getVelocity());
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void start() {
        super.start();
        autoUtils.limelightStart(getRuntime());
//        autoUtils.farAutoAim();
        Subsystem.startAutoFar();
        next=false;
        pathTimer = new Timer();
        Subsystem.intake.setPower(0.6);
//        hood.setPosition(0.4167);
//        outtake.setPower(1);
        pathState=0;
    }

    public void autonomousPathUpdate() {
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
                intake.setPower(0.9);
                //at pickup
                if(!follower.isBusy() && clear==false) {
                    clear=true;
                    pathTimer.resetTimer();
                }

                if(clear==true && pathTimer.getElapsedTimeSeconds() > 1){
                    follower.followPath(paths.path3,true);
                    setPathState(3);
                }
                break;
            case 3:

                //at end of pickup
                if(!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 1) {
                    follower.followPath(paths.path4,true);
//                    intake.setPower(0.6);
                    setPathState(4);
                }
                break;
            case 4:
                if(!follower.isBusy()) {
                    follower.followPath(paths.path5,true);
//                    intake.setPower(0.9);
                    setPathState(5);
                }
                break;
                //at scorePos
//                setPathStateAndFollow();
            case 5:
                //at start pickup 2
                shooting(6, paths.path6);
                break;

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
            follower.breakFollowing();
            next=true;
        }
        if (!follower.isBusy()  && (Subsystem.outtake.getVelocity() >= 0/*Subsystem.farVel*/ && Subsystem.outtake.getVelocity() <= 20/*Subsystem.farVel+20*/ ) && autoUtils.limelight(getRuntime()) && next){
            next=false;
            next2=true;
        //    Subsystem.stop.setPosition(0);
            Subsystem.intake.setPower(0.9);
            Subsystem.transfer.setPower(0.9);
            Subsystem.setPowerOuttake(1);

            pathTimer.resetTimer();

        }
        if(pathTimer.getElapsedTimeSeconds() >= 1 && next2){
            Subsystem.setVelocityOuttake(Subsystem.farVel);
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
class Paths2far {
    public static Pose startPose = new Pose(88, 9, Math.toRadians(90));
    public static Pose scorePose = new Pose(85, 20, Math.toRadians(67));
    public static Pose pickup1StartPose = new Pose(130, 8.5, Math.toRadians(0));
    public static Pose pickup1EndPose = new Pose(134, 8.5, Math.toRadians(0));

    @IgnoreConfigurable
    public PathChain path1, path2, path3, path4, path5, path6, path7, path8,path9, path10, path11, path12;

    public Paths2far(Follower follower) {
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
                ).setLinearHeadingInterpolation(pickup1StartPose.getHeading(), pickup1StartPose.getHeading())
                .build();
        path3 = follower.pathBuilder().
                addPath(new BezierLine(
                        pickup1StartPose,
                        pickup1EndPose)
                ).setLinearHeadingInterpolation(pickup1StartPose.getHeading(), pickup1EndPose.getHeading())
                .build();
        path4 = follower.pathBuilder().
                addPath(new BezierLine(
                        pickup1EndPose,
                        scorePose)
                ).setLinearHeadingInterpolation(pickup1EndPose.getHeading(), scorePose.getHeading())
                .build();
//        path5 = follower.pathBuilder().
//                addPath(new BezierLine(
//                        pickup1EndPose,
//                        scorePose)
//                ).setLinearHeadingInterpolation(pickup1EndPose.getHeading(), scorePose.getHeading())
//                .build();
//        path6 = follower.pathBuilder().
//                addPath(new BezierLine(
//                        pickup2StartPose,
//                        pickup2EndPose)
//                ).setLinearHeadingInterpolation(pickup2StartPose.getHeading(), pickup2EndPose.getHeading())
//                .build();
//        path7 = follower.pathBuilder().
//                addPath(new BezierLine(
//                        pickup2EndPose,
//                        scorePose)
//                ).setLinearHeadingInterpolation(pickup2EndPose.getHeading(), scorePose.getHeading())
//                .build();
//
//        path8 = follower.pathBuilder().
//                addPath(new BezierLine(
//                        scorePose,
//                        pickup3StartPose)
//                ).setLinearHeadingInterpolation(pickup3StartPose.getHeading(), pickup3EndPose.getHeading())
//                .build();
//        path9 = follower.pathBuilder().
//                addPath(new BezierLine(
//                        pickup3StartPose,
//                        pickup3EndPose)
//                ).setLinearHeadingInterpolation(pickup3StartPose.getHeading(), pickup3EndPose.getHeading())
//                .build();
//        path10 = follower.pathBuilder().
//                addPath(new BezierLine(
//                        pickup3EndPose,
//                        scorePose)
//                ).setLinearHeadingInterpolation(pickup3EndPose.getHeading(), scorePose.getHeading())
//                .build();
//        path11 = follower.pathBuilder()
//                .addPath(new BezierLine(
//                        scorePose,
//                        pickup1EndPose)
//                ).setLinearHeadingInterpolation(scorePose.getHeading(), pickup1EndPose.getHeading())
//                .build();
//        path12 = follower.pathBuilder()
//                .addPath(new BezierCurve(
//                        pickup1EndPose,
//                        controlClearGatePose,
//                        clearGatePose)
//                ).setLinearHeadingInterpolation(pickup1EndPose.getHeading(), clearGatePose.getHeading())
//                .build();


    }

}
