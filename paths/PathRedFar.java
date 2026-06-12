package org.firstinspires.ftc.teamcode.paths;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class PathRedFar {
    public static Pose startPose = new Pose(92, 7.5, Math.toRadians(0));
    public static Pose scorePose = new Pose(92, 8.5, Math.toRadians(0));

    public static Pose pickup3Pose = new Pose(126, 35.5, Math.toRadians(0));
    public static Pose pickup3Control1Pose = new Pose(90, 40, Math.toRadians(0));
    public static Pose pickup3Control2Pose = new Pose(93, 35.5, Math.toRadians(0));

    public static Pose pickupHumanPose = new Pose(130, 8.5, Math.toRadians(0));

    public static Pose pickupLinePose = new Pose(130, 23.5, Math.toRadians(0));
    public static Pose pickupLineControlPose = new Pose(90, 26, Math.toRadians(0));

//    public static FuturePose scorePose;
}
