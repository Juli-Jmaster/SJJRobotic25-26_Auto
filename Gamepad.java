//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.*;
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//
//@TeleOp
//
//public class DriveChain extends LinearOpMode {
//    private Blinker control_Hub;
//    private DcMotor backLeft;
//    private DcMotor backRight;
//    private DcMotor frontLeft;
//    private DcMotor frontRight;
//    private DcMotorEx outtake;
//    private DcMotor intake1;
//    private DcMotor intake2;
//    //    private boolean kicks = true;
//    private boolean oncea = false;
//    private boolean onceb = false;
//    private boolean intake = false;
//    private boolean transfer = false;
//    //    private Servo kick;
//    private boolean lastA = false;
//    private boolean lastB = false;
//    private int inc = 970;
//
//
//    private IMU imu;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
//        backRight = hardwareMap.get(DcMotor.class, "backRight");
//        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
//        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
//        outtake = hardwareMap.get(DcMotorEx.class, "outtake");
//        intake1 = hardwareMap.get(DcMotor.class, "intake1");
//        intake2 = hardwareMap.get(DcMotor.class, " intake2");
////        kick = hardwareMap.get(Servo.class, "kick");
////        kick.setPosition(0.8);
//
//
//
//        // frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
//        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
//        //  outtake.setDirection(DcMotorSimple.Direction.REVERSE);
//        intake1.setDirection(DcMotorSimple.Direction.REVERSE);
//        intake2.setDirection(DcMotorSimple.Direction.REVERSE);
//
//        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        outtake.setVelocityPIDFCoefficients(
//                3.2,   // P
//                0.0,   // I
//                1,   // D
//                13.8   // F
//        );
//        intake1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        intake2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
////        frontLeft.setPower(1);
//
//        waitForStart();
//        while (opModeIsActive()){
//            drive();
//            if (gamepad1.left_bumper){
//                outtake.setVelocity(2800);
//                //.55 for close to goal 3900rpm
//                //.65 for far to goal 3300rpm
//            } else
//            if (gamepad1.left_trigger >= 0.2){
//                outtake.setVelocity(0);
//                //.55 for close to goal 3900rpm
//                //.65 for far to goal 3300rpm
//            } else if (gamepad1.right_trigger >= 0.2){
//                outtake.setVelocity(inc);
//                //.55 for close to goal 3900rpm
//                //.65 for far to goal 3300rpm
//            }
//
//            // if(gamepad1.b && !(onceb)){
//            //     if(intake){
//            //         intake1.setPower(1);
//            //         intake=false;
//            //     } else if(!(intake)){
//            //         intake1.setPower(0);
//            //         intake=true;
//            //     }
//            //     onceb =true;
//            // } else {
//            //     onceb =false;
//            // }
//
//
//
//
//
//
//
//
//            if (gamepad1.b) {
////                transfer = !transfer;
//                intake1.setPower(1);
//                intake2.setPower(1);
//            } else {
//                intake1.setPower(0);
//                if (lastB){
//                    intake2.setPower(0);
//                }
//                if (gamepad1.a && !lastA) {
//                    intake = !intake;
//                    intake2.setPower(intake ? 1 : 0);
//                }
//                lastA = gamepad1.a;
//            }
//            lastB = gamepad1.b;
//
//
//
//
//            if(gamepad1.dpad_up && !(oncea)){
//                inc+=10;
//                oncea =true;
//            } else {
//                oncea =false;
//            }
//
//            if(gamepad1.dpad_down && !(onceb)){
//                inc-=10;
//                onceb =true;
//            } else {
//                onceb =false;
//            }
//
////            if (gamepad1.dpad_up){
////
////            } if (gamepad1.dpad_down){
////                inc-=10;
////            }
//
//
//
////
////            if(gamepad1.y && !(oncey)){
////                if(kicks){
//////                    kick.setPosition(0.7-.1);
////                    kicks=false;
////                } else if(kicks==false){
//////                    kick.setPosition(0.8);
////                    kicks=true;
////                }
////                oncey =true;
////            } else {
////                oncey =false;
////            }
//            // telemetry.update();
//            // backRight.setPower(1);
//            //long before time had a name, the first spinjitsu master created ninjago GET OUT
//            telemetry.addData("setVel", inc);
//            telemetry.addData("vel", outtake.getVelocity());
//            telemetry.update();
//        }
//
//    }
//    public void drive(){
//        float y=-gamepad1.left_stick_x;
//        float x=-gamepad1.left_stick_y;
//        float rx=gamepad1.right_stick_x;
//
//        backLeft.setPower(RangeLimit(x,y,rx,y+x+rx)); //backR
//        backRight.setPower(-RangeLimit(x,y,rx,y-x+rx)); //frontL
//        frontLeft.setPower(RangeLimit(x,y,rx,y-x-rx));  //frontR
//        frontRight.setPower(RangeLimit(x,y,rx,y+x-rx));
//
//
//        // telemetry.addData("x", gamepad1.left_stick_x);
//        // telemetry.addData("y", gamepad1.left_stick_y);
//        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//    }
//
//    private double RangeLimit(float x,float y, float rx,double value){
//        double denominator = Math.max(Math.abs(y) + Math.abs(x)+ Math.abs(rx), 1);
//        // telemetry.addData("x", x);
//        // telemetry.addData("y", y);
//        // telemetry.addData("rx", rx);
//        // telemetry.addData("dem", denominator);
//        // telemetry.addData("value", value /  denominator);
//
//        return (value /  denominator)*1;
//    }
//
//
//    // todo: write your code here
//}