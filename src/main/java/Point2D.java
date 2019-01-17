import edu.wpi.first.shuffleboard.api.data.ComplexData;

import java.util.HashMap;
import java.util.Map;

public class Point2D extends ComplexData<Point2D> {

    private double measX;
    private double measY;


    public Point2D() { // default values gained from robot
        this(0, 0);
    }

//    public Point2D(Map<String, Object> map) {
//        this((Double) map.getOrDefault("x", 0), (Double) map.getOrDefault("y", 0));
//    }

    public Point2D(double x, double y) {
        this.measX = x;
        this.measY = y;
    }



    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("RobotPositionTable_X", measX);
        map.put("RobotPositionTable_Y", measY);
        return map;
    }

    public double getX() {
        return this.measX;
    }

    public double getY() {
        return this.measY;
    }


}
