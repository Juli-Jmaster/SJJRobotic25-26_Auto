package org.firstinspires.ftc.teamcode.library;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Subsystem {

    public static DcMotorEx outtake;
    public static DcMotorEx outtake2;
    public static DcMotor transfer;
    public static DcMotor intake;
    public static Servo hood;

    public static int closeVel = 720+30;
    public static int farVel =10;//= 1000;
    public static double closeAngle = 0.0;
    public static double farAngle = 0.0;
    public static double faceGoalBlue = Math.toRadians(180);
    public static double faceGoalRed = 0;
    public static Servo t1;
    public static Servo t2;

    public static void init(HardwareMap map){
        outtake = map.get(DcMotorEx.class, "shoot");
        outtake2 = map.get(DcMotorEx.class, "shoot2");
        transfer = map.get(DcMotor.class, "transfer");
        intake = map.get(DcMotor.class, " intake");
        hood =  map.get(Servo.class, "hood");
        t1 = map.get(Servo.class, "t1");
        t2 = map.get(Servo.class, "t2");

        transfer.setDirection(DcMotorSimple.Direction.REVERSE);

        outtake.setVelocityPIDFCoefficients(

                100,   // P
                0., // I
                0,    // D
                15.2  // F
        );
        outtake2.setVelocityPIDFCoefficients(

                100,   // P
                0., // I
                0,    // D
                15.2  // F
        );
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtake2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public static void startTele(){
        hood.setPosition(.49);
        outtake.setVelocity(900);
    }

    public static void startAuto(){
        hood.setPosition(0.0928);
     //   stop.setPosition(0.2);
        outtake.setVelocity(closeVel+30);
        outtake2.setVelocity(closeVel+30);
    }
    public static void startAutoFar(){
        hood.setPosition(.09);
        //   stop.setPosition(0.2);
      //  outtake.setVelocity(farVel+30);
        outtake.setVelocity(10);
    } public static void setVelocityOuttake(double velocity) {
        outtake.setVelocity(velocity);
        outtake2.setVelocity(velocity);
    } public static void setPowerOuttake(double power) {
        outtake.setPower(power);
        outtake2.setPower(power);
    }
}
