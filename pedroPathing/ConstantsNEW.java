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
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.library.RobotConstantsNEW;

public class ConstantsNEW {


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



    private static FilteredPIDFCoefficients drivePIDF = new FilteredPIDFCoefficients(0.025,0.0,0.00001,0.6,0.01);
    private static PIDFCoefficients translationPIDF = new PIDFCoefficients(0.03, 0, 0.0, 0.03);
    private static PIDFCoefficients secondaryTranslationPIDF = new PIDFCoefficients(0.10, 0, 0.000, 0.025);
    //P minla overshoot
    //D value can dampen the P value if over
    private static PIDFCoefficients headingPIDF = new PIDFCoefficients(.5, 0, 0.0, 0.05);
    private static PIDFCoefficients secondaryHeadingPIDF = new PIDFCoefficients(0.3, 0, 0.08, 0.03);
    private static double centripetalScaling = 0.005;

    //secondary if want

    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(RobotConstantsNEW.massKG)
            .useSecondaryTranslationalPIDF(true).useSecondaryHeadingPIDF(true)//.useSecondaryDrivePIDF(false)
             .forwardZeroPowerAcceleration(RobotConstantsNEW.forwardDeceleration)
             .lateralZeroPowerAcceleration(RobotConstantsNEW.lateralZDeceleration)
            .translationalPIDFCoefficients(translationPIDF)
            .secondaryTranslationalPIDFCoefficients(secondaryTranslationPIDF)
             .headingPIDFCoefficients(headingPIDF)
            .secondaryHeadingPIDFCoefficients(secondaryHeadingPIDF)
             .drivePIDFCoefficients(drivePIDF);
    //         .centripetalScaling(centripetalScaling);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(RobotConstantsNEW.defaultPower)
            .rightFrontMotorName(RobotConstantsNEW.frontRightMotorName)
            .rightRearMotorName(RobotConstantsNEW.backRightMotorName)
            .leftRearMotorName(RobotConstantsNEW.backLeftMotorName)
            .leftFrontMotorName(RobotConstantsNEW.frontLeftMotorName)
            .leftFrontMotorDirection(RobotConstantsNEW.frontLeftReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(RobotConstantsNEW.backLeftReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(RobotConstantsNEW.frontRightReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(RobotConstantsNEW.backRightReversed ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD)
            .xVelocity(RobotConstantsNEW.forwardVelocity)
            .yVelocity(RobotConstantsNEW.strafeVelocity);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(3.9)
            .strafePodX(-7.9)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName(RobotConstantsNEW.pinpointName)
            .encoderResolution(RobotConstantsNEW.encoderResolution)
            .forwardEncoderDirection(RobotConstantsNEW.forwardEncoderDirection)
            .strafeEncoderDirection(RobotConstantsNEW.sidewaysEncoderDirection);



    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints).mecanumDrivetrain(driveConstants).pinpointLocalizer(localizerConstants)
                .build();
    }


}