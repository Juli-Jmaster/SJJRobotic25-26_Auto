package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.IgnoreConfigurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.library.RobotConstantsNEW;
import org.firstinspires.ftc.teamcode.library.Subsystem;
public class AutoUtils {
    private Timer pathTimer;
    private boolean next = false;
    private boolean next2 = false;
    public AutoUtils(){

    }


    public boolean running = false;

    //limelight
    private Limelight3A limelight;
    public IMU imu;
    public double error = 0;
    double lastError = 0;
    private double angleTolerance;
    public double goalX = 0;
    private double kP = 0.00;
    private double kD = 0.000;
    private double curTime = 0;
    private double lastTime;
    private double rotate=0;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    public boolean shooting = false;
    public double kPoutside = .15;
    public double kDoutside = 0;
    public double kPinside = .15;
    public double kDinside = 0;
    public double shootingRange = 2;

//    public void farAutoAim() {
//        goalX = -3;
//    }
    public void closeAutoAimRed (){
        goalX=2;
        shootingRange=2;
    }
    public void closeAutoAimBlue (){
        goalX=-2;
        shootingRange=2;
    }
    public void farAutoAimRed(){
        goalX=-3;
        shootingRange=1.2;
    }
    public void farAutoAimBlue(){
        goalX=3;
        shootingRange=1.2;
    }
    public boolean turretAdjust(double runtime) {
        boolean ret = false;
       // error = goalX - result.getTx();
        //log to DriverHub Tx value

        //how far off from target Tx
        curTime = runtime;
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            running=true;
            error = goalX - result.getTx();

            //kP and kD values for how much by
            //currently using: Servo
            if (Math.abs(error) > 5) {
                //outside kPDSwitch on each side
                kP = 0.00015;
                kD = 0;
            } else {
                //inside kPDSwitch on each side
                kP = 0.00015;
                kD = 0;
            }

            //calculate adjustment
            double pTerm = error * kP;
            double dT = Math.max(curTime - lastTime, 0.001);
            double dTerm = ((error - lastError) / dT) * kD;
            //limit the rotate so not too powerfull
            //could not use this and just have a smaller kP and kD
            rotate = Range.clip(pTerm + dTerm, -0.7, 0.7);

            //so does not rotate if within range
            if (Math.abs(error) < 1.2) {
                rotate = 0;
            } else if (Math.abs(error) > 1.2) {
                rotate *= -1;

                //get last direction to turret search
                //method: Servo
            }
            if (Math.abs(error) < 2){
                ret = true;
            }

            //reset for next time
            lastError = error;
            lastTime = curTime;
            rotateTurret(rotate);
        }
        return ret;
    }

    public boolean limelight(double runtime) {
        boolean ret = false;
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation(orientation.getYaw());
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {

                //addjust to the goal
                curTime = runtime;
                error = goalX - result.getTx();

                if (Math.abs(error) > 5) {
                    // outside 5
                    kP = kPoutside;
                    kD = kDoutside;
                } else {
                    // inside 5
                kP = kPinside;
                kD = kDinside;
                }

                double pTerm = error * kP;
                double dT = Math.max(curTime - lastTime, 0.001);
                double dTerm = ((error - lastError) / dT) * kD;

                rotate = Range.clip(pTerm + dTerm, -0.4, 0.4);
                rotate*=-1;
                if (Math.abs(error) < shootingRange) {
                    ret=true;
                } else {
                    ret=false;
                }

                lastError = error;
                lastTime = curTime;
            }
        } else {
            lastError = 0;
            lastTime = runtime;
            rotate=0.0;
        }
        if (!shooting) {
            drive((float) 0, (float) 0, (float) rotate);
        }
        return ret;
    }

    public void limelightInit(HardwareMap map){
        imu = map.get(IMU.class, RobotConstantsNEW.imuName);

        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP)));

        limelight = map.get(Limelight3A.class, RobotConstantsNEW.limelightName);
        limelight.pipelineSwitch(1);

        //motors
        backLeft = map.get(DcMotor.class, RobotConstantsNEW.backLeftMotorName);
        backRight = map.get(DcMotor.class, RobotConstantsNEW.backRightMotorName);
        frontLeft = map.get(DcMotor.class, RobotConstantsNEW.frontLeftMotorName);
        frontRight = map.get(DcMotor.class, RobotConstantsNEW.frontRightMotorName);

        backLeft.setDirection(RobotConstantsNEW.backLeftReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(RobotConstantsNEW.backRightReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        frontLeft.setDirection(RobotConstantsNEW.frontLeftReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(RobotConstantsNEW.frontRightReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);

        pathTimer = new Timer();
    }
    public void limelightStart(double runtime){
        limelight.start();
        imu.resetYaw();
        curTime = runtime;
        lastTime = runtime;
    }

    public void drive(float x, float y, float rx){
        y=-y;
        x=-x;
        backLeft.setPower(RangeLimit(x,y,rx,y+x+rx)); //backR
        backRight.setPower(RangeLimit(x,y,rx,y-x-rx)); //frontL
        frontLeft.setPower(RangeLimit(x,y,rx,y-x+rx));  //frontR
        frontRight.setPower(RangeLimit(x,y,rx,y+x-rx));
    }

    private double RangeLimit(float x,float y, float rx,double value){
        double denominator = Math.max(Math.abs(y) + Math.abs(x)+ Math.abs(rx), 1);
//        panelsTelemetry.debug((value /  denominator)*.925);
        return (value /  denominator)*.925;
    }

    private void rotateTurret(double rotate) {
        if (rotate != 0) {
            running = true;
            Subsystem.t1.setPosition(Subsystem.t1.getPosition() + rotate);
            Subsystem.t2.setPosition(Subsystem.t2.getPosition() + rotate);
//            t1.setPositon(t1.getPosition() + rotate);
//            t2.setPositon(t2.getPosition() + rotate);
        }// else if (boolean shooting = false; {.

//            running = false;
//            Subsystem.t1.setPosition(Subsystem.t1.getPosition() + rotate);
//            Subsystem.t2.setPosition(Subsystem.t2.getPosition() + rotate);
//              t1.setPositon(t1.getPosition() + rotate);
//              t2.setPositon(t2.getPosition() + rotate);
//      }
    }

}
