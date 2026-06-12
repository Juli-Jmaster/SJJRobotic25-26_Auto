package org.firstinspires.ftc.teamcode.paths;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class PathBlueFar {
    public static Pose startPose = new Pose(52, 7.5, Math.toRadians(180));
    public static Pose scorePose = new Pose(52, 8.5, Math.toRadians(180));

    public static Pose pickup3Pose = new Pose(18, 35.5, Math.toRadians(180));
    public static Pose pickup3Control1Pose = new Pose(54, 40, Math.toRadians(180));
    public static Pose pickup3Control2Pose = new Pose(51, 35.5, Math.toRadians(180));

    public static Pose pickupHumanPose = new Pose(14, 8.5, Math.toRadians(180));

    public static Pose pickupLinePose = new Pose(14, 23.5, Math.toRadians(180));
    public static Pose pickupLineControlPose = new Pose(54, 26, Math.toRadians(180));

//    public static FuturePose scorePose;
}
