package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class PathBlue {
    //from score to something angle is **180(in radians)** not scorePos.getHeading()
    public static Pose startPose = new Pose(30, 133, Math.toRadians(270));
    public static Pose scorePose = new Pose(57, 77, Math.toRadians(140));

    public static Pose pickup1Pose = new Pose(18, 84.5, Math.toRadians(180));
    public static Pose pickup2Pose = new Pose(18, 57, Math.toRadians(180));
    public static Pose pickup3Pose = new Pose(18, 35.5, Math.toRadians(180));

    public static Pose pickup1PoseCenter = new Pose(102, 82, Math.toRadians(180));
    public static Pose pickup2PoseCenter = new Pose(52, 55, Math.toRadians(180));
    public static Pose pickup3PoseCenter = new Pose(57, 30, Math.toRadians(180));

    public static Pose clearGatePoseFrom1 = new Pose(124, 75, Math.toRadians(180));
    public static Pose clearGateControlPoseFrom1 = new Pose(39, 75, Math.toRadians(180));

    public static Pose clearGatePoseFromScore = new Pose(19, 71, Math.toRadians(180));
    public static Pose clearGateControlPoseFromScore = new Pose(49, 63, Math.toRadians(180));

    public static Pose pickupGatePose = new Pose(8, 57, Math.toRadians(120));
    public static Pose pickupGateControlPose = new Pose(122, 61, Math.toRadians(180));

}
