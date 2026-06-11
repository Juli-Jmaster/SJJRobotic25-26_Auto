//package org.firstinspires.ftc.teamcode;
//
//import com.bylazar.telemetry.PanelsTelemetry;
//import com.bylazar.telemetry.TelemetryManager;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.geometry.BezierCurve;
//import com.pedropathing.geometry.BezierLine;
//import com.pedropathing.paths.PathChain;
//import com.pedropathing.util.Timer;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.firstinspires.ftc.teamcode.PathRed;
//import org.firstinspires.ftc.teamcode.library.Subsystem;
//import org.firstinspires.ftc.teamcode.pedroPathing.AutoUtils;
//import org.firstinspires.ftc.teamcode.pedroPathing.ConstantsCanada;
//
//@Autonomous
//public class AutoRedFar extends OpMode {
//    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
//    private Timer pathTimer;
//    private AutoUtils autoUtils;
//    private Follower follower;
//    private PathsBRedFar paths; // Paths defined in the Paths class
//    private boolean next = false;
//    private boolean next2 = false;
//    private boolean next3 = false;
//    private boolean next4 = false;
//    private int pathState =0;
//    private double IntakeOnXRed = 102;
//
//
//    @Override
//    public void init() {
//        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
//        follower = ConstantsCanada.createFollower(hardwareMap);
//        follower.setStartingPose(PathRedFar.startPose);
//        paths = new PathsBRedFar(follower); // Build paths
//
//        autoUtils = new AutoUtils();
//        Subsystem.init(hardwareMap);
//        autoUtils.limelightInit(hardwareMap);
//
//
//        panelsTelemetry.debug("Status", "Initialized");
//        panelsTelemetry.update(telemetry);
//    }
//
//    @Override
//    public void loop() {
//        autonomousPathUpdate(); // Update autonomous state machine
//        follower.update(); // Update Pedro Pathing
//
//
//        // Log values to Panels and Driver Station
//        panelsTelemetry.debug("Path State", pathState);
//        panelsTelemetry.debug("isBusy", follower.isBusy());
//        panelsTelemetry.debug("X", follower.getPose().getX());
//        panelsTelemetry.debug("Y", follower.getPose().getY());
//        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
//        panelsTelemetry.debug("vel", Subsystem.outtake.getVelocity());
//        panelsTelemetry.debug("tValue", follower.getCurrentTValue());
//        panelsTelemetry.debug("setvel", Subsystem.closeVel);
//        panelsTelemetry.debug("at power", (Subsystem.outtake.getVelocity() >= Subsystem.closeVel-60 && Subsystem.outtake.getVelocity() <= Subsystem.closeVel+30 ));
//        panelsTelemetry.update(telemetry);
//    }
//
//    @Override
//    public void start() {
//        super.start();
//        autoUtils.limelightStart(getRuntime());
//        Subsystem.startAutoFar();
//        autoUtils.farAutoAimRed();
//        Subsystem.t1.setPosition(.1061);
//        Subsystem.t2.setPosition(.1061);
//        pathTimer = new Timer();
//        pathState=0;
//    }
//
//    public void autonomousPathUpdate() {
//        switch (pathState) {
//            case 0:
//                shooting(1, paths.path1);
//                break;
//            case 1:
//                AtIntakeXOnTransferRed();
//                notBusyNextState(2, paths.path2);
//            case 2:
//                PassIntakeXOffTransferRed();
//                shooting(3, paths.path3);
//            case 3:
//                AtIntakeXOnTransferRed();
//                notBusyNextState(4, paths.path4);
//            case 4:
////                PassIntakeXOffTransferRed();
////                shooting(5, paths.path1);
//        }
//    }
//
//    private void AtIntakeXOnTransferRed() {
//        if (follower.getPose().getX() > IntakeOnXRed){
//            Subsystem.intake.setPower(0.9);
//            Subsystem.transfer.setPower(-.6);
//        }
//    }
//
//    private void PassIntakeXOffTransferRed() {
//        if (follower.getPose().getX() < IntakeOnXRed && follower.isBusy()){
//            Subsystem.intake.setPower(0.0);
//            Subsystem.transfer.setPower(0.0);
//        }
//    }
//
//    private void notBusyNextState(int pState, PathChain path){
//        if(!follower.isBusy()) {
//            setPathStateAndFollow(pState, path);
//        }
//    }
//
//    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
//    public void setPathState(int pState) {
//        pathState = pState;
//        pathTimer.resetTimer();
//    }
//    public void setPathStateAndFollow(int pState, PathChain path) {
//        pathState = pState;
//        follower.followPath(path, true);
//        pathTimer.resetTimer();
//    }
//    public void shooting(int nextState, PathChain nextPath){
//        if (!follower.isBusy() && !next){
//            next=true;
//            pathTimer.resetTimer();
//        }
//        if (!follower.isBusy() && next){
//            panelsTelemetry.debug("adjust", (autoUtils.limelight(getRuntime()) || pathTimer.getElapsedTimeSeconds() > 1.25));
//            panelsTelemetry.debug("outtakespeed", (Subsystem.outtake.getVelocity() >= Subsystem.farVel-10 && Subsystem.outtake.getVelocity() <= Subsystem.farVel+80 ));
//        }
//        if (!follower.isBusy()  && (Subsystem.outtake.getVelocity() >= Subsystem.farVel-10 && Subsystem.outtake.getVelocity() <= Subsystem.farVel+80 ) && (autoUtils.limelight(getRuntime()) || pathTimer.getElapsedTimeSeconds() > 1.25)&& next){
//            next=false;
//            next2=true;
//            autoUtils.shooting=true;
//            follower.breakFollowing();
//            //Subsystem.stop.setPosition(0);
//            Subsystem.intake.setPower(.8);
//            Subsystem.transfer.setPower(.8);
//            Subsystem.setPowerOuttake(1);
//
//            pathTimer.resetTimer();
//
//        }
//        if(!follower.isBusy() && pathTimer.getElapsedTimeSeconds()>= .65 && next2){
//            autoUtils.shooting=false;
//            Subsystem.setVelocityOuttake(Subsystem.farVel);
//            Subsystem.intake.setPower(0);
//            Subsystem.transfer.setPower(0);
//
//            setPathStateAndFollow(nextState, nextPath);
//            //    Subsystem.stop.setPosition(0.2);
//            next=false;
//            next2=false;
//        }
//    }
//
//}
//
//class PathsBRedFar {
//    public PathChain path1, path2, path3, path4, path5, path6, path7, path8,path9, path10, path11, path12;
//    public PathsBRedFar(Follower follower) {
//        path1 = follower.pathBuilder().addPath(new BezierLine(
//                PathRedFar.startPose, PathRedFar.pickupHumanPose))
//                .setLinearHeadingInterpolation(PathRedFar.startPose.getHeading(), PathRedFar.pickupHumanPose.getHeading())
//                .build();
//
//        path2 = follower.pathBuilder().addPath(new BezierLine(
//                PathRedFar.pickupHumanPose, PathRedFar.shootPose))
//                .setLinearHeadingInterpolation(PathRedFar.pickupHumanPose.getHeading(), PathRedFar.shootPose.getHeading())
//                .build();
//        path3 = follower.pathBuilder().addPath(new BezierCurve(
//                PathRedFar.shootPose, PathRedFar.pickup3Control1Pose, PathRedFar.pickup3Control2Pose, PathRedFar.pickup3Pose))
//                .setLinearHeadingInterpolation(PathRedFar.shootPose.getHeading(), PathRedFar.pickup3Pose.getHeading())
//                .build();
//        path4  = follower.pathBuilder().addPath(new BezierLine(
//                PathRedFar.pickup3Pose, PathRedFar.shootPose))
//                .setLinearHeadingInterpolation(PathRedFar.pickup3Pose.getHeading(), PathRedFar.shootPose.getHeading())
//                .build();
//    }
//
//}
//
