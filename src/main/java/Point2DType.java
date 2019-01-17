import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

import java.util.Map;
import java.util.function.Function;

public class Point2DType extends ComplexDataType<Point2D> {
    public static final Point2DType Instance = new Point2DType();

    private Point2DType() {
        super("Point2D", Point2D.class);
    }

    @Override
    public Function<Map<String, Object>, Point2D> fromMap() {
        return map ->
        {
            return new Point2D();
        };
    }

    @Override
    public Point2D getDefaultValue() {
        return new Point2D(0, 0);
    }

}
