package server;

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

        // THIS MAY CAUSE ISSUES IN THE FUTURE, JUST IMAGINE AVERAGING -179 & 179 THIS WAY
        // CHECK OUT https://en.wikipedia.org/wiki/Circular_mean
        double rx = (origin.rx + pX.rx + pY.rx) / 3.0;
        double ry = (origin.ry + pX.ry + pY.ry) / 3.0;
        double rz = (origin.rz + pX.rz + pY.rz) / 3.0;

        return new Pose(x, y, z, rx, ry, rz);
    }
}
