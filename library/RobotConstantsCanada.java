package org.firstinspires.ftc.teamcode.library;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

@Configurable
public interface RobotConstantsCanada {

    //* Drive settings
    String frontRightMotorName = "FR" ;
    boolean frontRightReversed = false;

    String frontLeftMotorName = "FL";
    boolean frontLeftReversed = true;

    String backRightMotorName = "BR";
    boolean backRightReversed = false;

    String backLeftMotorName = "BL";
    boolean backLeftReversed = true ;

    //* Overall Drive power
    double defaultPower = .9;
    //THRESHOLD should be equal to the minimum amount or little higher of power it takes to move the robot forward or directly sideways
    double minimumPowerStraight = 0.1;  // or .2
    double minimumPowerSideways = 0.2;  // or .2
    double minimumPowerToTurn = 0.1;


    
    //*padro pathing
    double massKG =11.34;
    double forwardVelocity = 69.4223;
    double strafeVelocity = 59.4106;
    double forwardDeceleration = -38.3422; // 61.5343
    double lateralZDeceleration = -65.2492;

    //pinpoint
    String pinpointName = "Pinpoint";
    GoBildaPinpointDriver.GoBildaOdometryPods encoderResolution = GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
    GoBildaPinpointDriver.EncoderDirection forwardEncoderDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
    GoBildaPinpointDriver.EncoderDirection sidewaysEncoderDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
    double forwardOffset = 0;
    double sidewaysoffset = 0;


    //IMU of robot;
    String imuName = "imu";
    RevHubOrientationOnRobot Orientation = new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
            RevHubOrientationOnRobot.UsbFacingDirection.UP);

    //limelight
    String limelightName = "limelight";

    
    
    
    //* Default Odometry settings
    //also found in class OdemetryMotor.java
    //the wheel type is the type of units used for the diameter of the wheel
    //  Ex. if the diamemter of the wheel is 48mm then it is WHEELTYPE.MM
    //this is the type of data the is found on the GoBuilda website
    //  which is called Encoder Resolution on the specs of motor or odemetry pod
    //  it either be  "Parts Per Revolution (PPR)" or "Countable Events per Revolution"
    //  "PPR" is TYPE.PPR and "Countable Events per Revolution" is TYPE.TICKPERREV
//    OdometryMotor.WHEELTYPE diameterLengthType = OdometryMotor.WHEELTYPE.MM;
//    int diameterLength = 48;
//
//    OdometryMotor.TYPE ticksPerType = OdometryMotor.TYPE.TICKPERREV;
//    int ticksPerTypeNumber = 2000;

//    AvgOdomMotor straightOdometry = new AvgOdomMotor(
//            new OdometryMotor("straight", OdometryMotor.WHEELTYPE.MM, 48, OdometryMotor.TYPE.TICKPERREV, 2000)
//            //           new OdometryMotor("straight2", OdometryMotor.WHEELTYPE.MM, 48, OdometryMotor.TYPE.TICKPERREV, 2000),
//    );
//    AvgOdomMotor sidewaysOdometry = new AvgOdomMotor(
//            new OdometryMotor("sideways", OdometryMotor.WHEELTYPE.MM, 48, OdometryMotor.TYPE.TICKPERREV, 2000)
////          new OdometryMotor("sideways2", OdometryMotor.WHEELTYPE.MM, 48, OdometryMotor.TYPE.TICKPERREV, 2000)
//    );

}