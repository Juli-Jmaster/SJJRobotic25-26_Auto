package org.firstinspires.ftc.teamcode.library;

public class Utils {

    public static boolean isWithinTolerance(double angle, double target, double tolerance) {
        double error = normalizeDegrees(angle - target);
        return Math.abs(error) <= tolerance;
    }

    private static double normalizeDegrees(double degrees) {
        while (degrees > 180) degrees -= 360;
        while (degrees <= -180) degrees += 360;
        return degrees;
    }
    public static boolean notFacing(double startYaw, double currentYaw, double targetYaw, double tolerance) {

        // Compute shortest angular differences
        double totalError = normalizeDegrees(targetYaw - startYaw);      // Total turn needed
        double currentError = normalizeDegrees(targetYaw - currentYaw);  // Remaining error

        // Determine turn direction
        boolean turningClockwise = totalError > 0;

        // Check if passed the target
        if (turningClockwise && currentError <= 0) return false;
        if (!turningClockwise && currentError >= 0) return false;

        // Check if within tolerance
        return Math.abs(currentError) > tolerance;
    }


}
