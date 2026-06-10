package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.library.RobotConstants;

public class ConstantsOld {


    //TODO: move into RobotConts
    //when tuning first double check the type of localizerConstants your have on the robot
    //when adjust the forwardOffsetFromMidddle, sidewaysoffsetFromMiddle values

    //when in the program showly add and adjust the vaules

    //now go ahead and autotune xVelocity using the Tuner program; ForwardVelocityTuner
    //next yVelocity; LateralVelocityTuner
    //forwardDeceleration; ForwardZeroPowerAccelerationTuner
    //lateralZDeceleration; LateralZeroPowerAccelerationTuner

    //manuel tune each one had a tuner program to use; does not move robot
    //translationPIDF
    //then headingPIDF
    //then drivePIDF after doing translationPIDF, headingPIDF configured

    //then centripetalScaling program which does move robot
    //If the robot corrects towards the inside of the curve, decrease centripetalScaling
    //If the robot corrects towards the outside of the curve, increase centripetalScaling


    private static double massKG =11.84;
    private static double forwardVelocity = 66.7;
    private static double strafeVelocity = 46.9;
    private static double forwardDeceleration = -30.25;
    private static double lateralZDeceleration = -75.7;
    private static String pinpointName = "Pinpoint";
    private static GoBildaPinpointDriver.GoBildaOdometryPods encoderResolution = GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
    private static GoBildaPinpointDriver.EncoderDirection forwardEncoderDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
    private static GoBildaPinpointDriver.EncoderDirection sidewaysEncoderDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
    private static double forwardOffsetFromMidddle = -7;
    private static double sidewaysoffsetFromMiddle = -3.1;

    private static FilteredPIDFCoefficients drivePIDF = new FilteredPIDFCoefficients(0.006,0.0,0.00,0.6,0.0000);
    private static PIDFCoefficients translationPIDF = new PIDFCoefficients(0.06, 0, 0.0, 0.017);
    private static PIDFCoefficients secondaryTranslationPIDF = new PIDFCoefficients(0.15, 0, 0.003, 0.03);
    //P minla overshoot
    //D value can dampen the P value if over
    private static PIDFCoefficients headingPIDF = new PIDFCoefficients(.5, 0, 0.0, 0.0215);
    private static PIDFCoefficients secondaryHeadingPIDF = new PIDFCoefficients(0.3, 0, 0.0, 0.02);
    private static double centripetalScaling = 0.005;

    //secondary if want

    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(massKG)
            .useSecondaryTranslationalPIDF(true).useSecondaryHeadingPIDF(true).useSecondaryDrivePIDF(false)
             .forwardZeroPowerAcceleration(forwardDeceleration)
             .lateralZeroPowerAcceleration(lateralZDeceleration)
            .translationalPIDFCoefficients(translationPIDF)
            .secondaryTranslationalPIDFCoefficients(secondaryTranslationPIDF)
             .headingPIDFCoefficients(headingPIDF)
            .secondaryHeadingPIDFCoefficients(secondaryHeadingPIDF)
             .drivePIDFCoefficients(drivePIDF);
    //         .centripetalScaling(centripetalScaling);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, .85, 0.6);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(RobotConstants.defaultPower)
            .rightFrontMotorName(RobotConstants.frontRightMotorName)
            .rightRearMotorName(RobotConstants.backRightMotorName)
            .leftRearMotorName(RobotConstants.backLeftMotorName)
            .leftFrontMotorName(RobotConstants.frontLeftMotorName)
            .leftFrontMotorDirection(RobotConstants.frontLeftReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(RobotConstants.backLeftReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(RobotConstants.frontRightReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(RobotConstants.backRightReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD)
            .xVelocity(forwardVelocity)
            .yVelocity(strafeVelocity);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(-2.93)
            .strafePodX(-6.75)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName(pinpointName)
            .encoderResolution(encoderResolution)
            .forwardEncoderDirection(forwardEncoderDirection)
            .strafeEncoderDirection(sidewaysEncoderDirection);



    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints).mecanumDrivetrain(driveConstants).pinpointLocalizer(localizerConstants)
                .build();
    }


}