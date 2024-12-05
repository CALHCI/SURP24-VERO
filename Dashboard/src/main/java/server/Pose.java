package server;

class Pose {
    final double x, y, z, rx, ry, rz;

    public Pose(double x, double y, double z, double rx, double ry, double rz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    @Override
    public String toString() {
        return String.format("p[%.4f, %.4f, %.4f, %.4f, %.4f, %.4f]", x, y, z, rx, ry, rz);
    }
}
