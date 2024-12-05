package server;

class VRData {
    private final float x;
    private final float y;

    public VRData(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public VRData(String data) {
        String[] parts = data.split(",");
        if (parts.length == 2) {
            try {
                this.x = Float.parseFloat((parts[0]));
                this.y = Float.parseFloat(parts[1]);

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid message format: " + data);
            }
        } else {
            throw new IllegalArgumentException("Invalid message format: " + data);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
