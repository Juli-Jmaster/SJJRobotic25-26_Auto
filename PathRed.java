package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class PathRed {
    //from score to something angle is **0** not scorePos.getHeading()
    public static Pose startPose = new Pose(114, 133, Math.toRadians(270));
    public static Pose scorePose = new Pose(87, 77, Math.toRadians(40));

    public static Pose pickup1Pose = new Pose(126, 84.5, Math.toRadians(0));
    public static Pose pickup2Pose = new Pose(126, 57, Math.toRadians(0));
    public static Pose pickup3Pose = new Pose(126, 35.5, Math.toRadians(0));

    public static Pose pickup1PoseCenter = new Pose(102, 82, Math.toRadians(0));
    public static Pose pickup2PoseCenter = new Pose(92, 55, Math.toRadians(0));
    public static Pose pickup3PoseCenter = new Pose(87, 30, Math.toRadians(0));

    public static Pose clearGatePoseFrom1 = new Pose(124, 75, Math.toRadians(0));
    public static Pose clearGateControlPoseFrom1 = new Pose(105, 75, Math.toRadians(0));

    public static Pose clearGatePoseFromScore = new Pose(125, 71, Math.toRadians(0));
    public static Pose clearGateControlPoseFromScore = new Pose(95, 63, Math.toRadians(0));

    public static Pose pickupGatePose = new Pose(136, 57, Math.toRadians(60));
    public static Pose pickupGateControlPose = new Pose(122, 61, Math.toRadians(0));

}
