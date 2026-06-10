package org.firstinspires.ftc.teamcode.library;



public interface RobotConstants {
    //* Drive settings
    // all drive motor set to brake for Zero Power Behavior
    boolean driveMotorBrake  = true;
    //if all motor have encoders cables attached and want to use those instead
    boolean drivemotorEncoders = false;

    String frontRightMotorName = "frontRight" ;
    boolean frontRightReversed = false;

    String frontLeftMotorName = "frontLeft";
    boolean frontLeftReversed = true;

    String backRightMotorName = "backRight";
    boolean backRightReversed = false;

    String backLeftMotorName = "backLeft";
    boolean backLeftReversed = true ;

    //* Overall Drive power
    double defaultPower = 0.8;
    //THRESHOLD should be equal to the minimum amount or little higher of power it takes to move the robot forward or directly sideways
    double minimumPowerStraight = 0.1;  // or .2
    double minimumPowerSideways = 0.2;  // or .2
    double minimumPowerToTurn = 0.1;

    int inchesForMinPower = 2;
    int inchesForTwiceMinPower = 6;
    /* the default tolerance for when turning
       how many degrees off it can be before stopping
       Ex. is 3 degreees of from target say robot got to the position */
    int defaultToleranceFace = 1;
    /*time before the notFacing() return false as it may be stuck in being exact*/
    int defaultTimeWaitForTurn = 3;

    //const for amount of ticks before to stop the motors for;
    int defaultTicksStopAmount = 10;
    double defaultDriveTurnAdjustMultiplier = 0.4;

    //* MovementCurves.java
    //* Straight MovementCurves
    int defaultStraightMovementCurve = MovementCurves.EXPEASEOUT;
    double defaultStraightMultipier = 1;

    //* Sideways MovementCurves
    int defaultSidewaysMovementCurve = MovementCurves.EXPEASEOUT;
    double defaultSidewaysMultipier = 1;

    //* IMU MovementCurves
    int defaultIMUTurnMovementCurve = MovementCurves.QUADRATIC;
    double defaultIMUTurnMultipier = .6;


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