import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


@Description(name = "Robot XY Grapher", dataTypes = Point2D.class)
@ParametrizedController(value = "/sample.fxml")


public class Controller extends SimpleAnnotatedWidget {

    private Point2D coord = new Point2D();

//    DataSource<?> robotPosition = NetworkTableSource.forKey("positions");
//

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("positions");

    NetworkTableEntry xPos = table.getEntry("xPos");
    NetworkTableEntry yPos = table.getEntry("yPos");

//    final ObjectProperty<DataSource> positionDataSource = new SimpleObjectProperty<>(this, "RobotSource", DataSource.none());
//    final PropertyBinding<Point2D> positionData = EasyBind.monadic(positionDataSource).selectProperty(DataSource::dataProperty);



    @FXML
    AnchorPane root;

    @FXML
    NumberAxis xAxis = new NumberAxis();


    @FXML
    NumberAxis yAxis = new NumberAxis();

    @FXML
    LineChart<Number, Number> coordinate;

    XYChart.Series<Number, Number> series = new XYChart.Series();

    // hold data point for y
    public ConcurrentLinkedQueue<Number> pointY = new ConcurrentLinkedQueue<>();

    // hold data point for x
    public ConcurrentLinkedQueue<Number> pointX = new ConcurrentLinkedQueue<>();

    // generate random values
    public ExecutorService executor;


    @FXML
    public void initialize() {

//        positionDataSource.set(robotPosition);
//        robotPosition.addClient(this);

        // Creates the LineChart
        coordinate.setTitle("Live Robot Coordinates");
        coordinate.setAnimated(true);
        coordinate.setHorizontalGridLinesVisible(true);
        coordinate.setVerticalGridLinesVisible(true);
        coordinate.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        series.setName("Coords");

        // Add Chart Series
        coordinate.getData().addAll(series);

        executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r); // makes a new thread and runs
                thread.setDaemon(true);
                return thread;
            }
        });

        // live update
        AddToQueue addToQueue = new AddToQueue();
        executor.execute(addToQueue);

        prepareTimeline();
    }

    public void addDataToSeries() {
        for (int i = 0; i < 20; i++) { //-- add robot coords to Map/arraylist, get the size of Map or arraylist
            if (pointY.isEmpty())
                break;
            series.getData().add(new XYChart.Data<>(pointX.remove(), pointY.remove()));
            // x-value is pointX.remove()
            // y-value is pointY.remove()
        }
    }

    public void prepareTimeline() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        } .start();
    }


    public class AddToQueue implements Runnable {
        private double x;
        private double y;

        public void run() {
            try {

                this.x = xPos.getDouble(0);
                this.y = yPos.getDouble(0);

                pointY.add(y); // add robot coordinates here only y-values being updated here??
                pointX.add(x); // robot coordinates x-values updating
                // not sure if the robot coords are always going to be updated

                Thread.sleep(1000); //graphing is time based
                executor.execute(this::run);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Pane getView() { // return the graph
        return root;
    }
}
