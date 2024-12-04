package server;

import static java.lang.Math.*;

class Plane {
    private final Pose origin, pX, pY;

    public Plane(Pose origin, Pose pX, Pose pY) {
        this.origin = origin;
        this.pX = pX;
        this.pY = pY;
    }

    public Pose getBarycentric(VRData vrData) {
        double u = vrData.getX() / 100.0;
        double v = vrData.getY() / 100.0;

        double x = origin.x + u * (pX.x - origin.x) + v * (pY.x - origin.x);
        double y = origin.y + u * (pX.y - origin.y) + v * (pY.y - origin.y);
        double z = origin.z + u * (pX.z - origin.z) + v * (pY.z - origin.z);

        // Compute the interpolated rotations (rx, ry, rz) using circular mean
        double rx = calculateCircularMean(new double[]{origin.rx, pX.rx, pY.rx});
        double ry = calculateCircularMean(new double[]{origin.ry, pX.ry, pY.ry});
        double rz = calculateCircularMean(new double[]{origin.rz, pX.rz, pY.rz});

        return new Pose(x, y, z, rx, ry, rz);
    }

    private static double calculateCircularMean(double[] angles) {
        double sinSum = 0, cosSum = 0;
        
        // Convert each angle to radians, compute sine and cosine, and sum them
        for (double angle : angles) {
            double radians = toRadians(angle);
            sinSum += sin(radians);
            cosSum += cos(radians);
        }
        // Calculate the mean angle in degrees using atan2 for accurate quadrant handling
        double meanAngle = toDegrees(atan2(sinSum / angles.length, cosSum / angles.length));
        
        // Normalize the mean angle to the range [-180, 180]
        if (meanAngle < -180) meanAngle += 360;
        if (meanAngle > 180) meanAngle -= 360;
        return meanAngle;
    }
}
