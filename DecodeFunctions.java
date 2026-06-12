package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.library.Subsystem;

public class DecodeFunctions {
    private Follower follower;
    private double IntakeOnXRed = 102;
    private double IntakeOnXBlue = 42;
    private boolean next;
    private boolean next2;
    private boolean next3;
    private boolean next4;
    private Timer pathTimer;


    public DecodeFunctions(Follower follower){
        this.follower = follower;

    }

    private void AtIntakeXOnTransferBlue() {
        if (follower.getPose().getX() < IntakeOnXBlue){
            Subsystem.intake.setPower(0.8);
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

    public void GateIntake(int pState, PathChain paths) {
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
    private void HoldIntakeShoot(int pState, PathChain path) {
        if (!follower.isBusy() && !next3 && !next4){
            next3=true;
            Subsystem.intake.setPower(0.8);
            Subsystem.transfer.setPower(-.6);
            pathTimer.resetTimer();
        }
        if (!follower.isBusy() && next3 && pathTimer.getElapsedTimeSeconds() > .5){
            next3=false;
            next4=true;
            Subsystem.intake.setPower(0.0);
            Subsystem.transfer.setPower(0.0);
        }
        if (!follower.isBusy() && next4){
            shooting(pState, path);
        }
    }

    public void setPathStateAndFollow(int pState, PathChain path) {
//ENABLE        pathState = pState;
        follower.followPath(path, true);
//ENABLE        pathTimer.resetTimer();
    }

    public void shooting(int nextState, PathChain nextPath){
        if (!follower.isBusy() && !next){
            next=true;
            pathTimer.resetTimer();
        }
        if (!follower.isBusy()  && (Subsystem.outtake.getVelocity() >= Subsystem.closeVel-10 && Subsystem.outtake.getVelocity() <= Subsystem.closeVel+80 )  /* && (autoUtils.limelight(getRuntime()) || pathTimer.getElapsedTimeSeconds() > 1.25)*/ && next){
            next=false;
            next2=true;
//ENABLE            autoUtils.shooting=true;
            follower.breakFollowing();
            //      Subsystem.stop.setPosition(0);
            Subsystem.intake.setPower(0.9);
            Subsystem.transfer.setPower(1);
            Subsystem.setPowerOuttake(1);

            pathTimer.resetTimer();

        }
        if(!follower.isBusy() && pathTimer.getElapsedTimeSeconds()>= .65 && next2){
//ENABLE            autoUtils.shooting=false;
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
