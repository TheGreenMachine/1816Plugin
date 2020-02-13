package com.edinarobotics.plugin.widget;

import com.edinarobotics.plugin.data.Point2D;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.DoubleSupplier;


@Description(name = "Robot XY Grapher", dataTypes = Point2D.class)
@ParametrizedController(value = "/com/edinarobotics/plugin/coordinategui.fxml")
public class CoordinateWidget extends SimpleAnnotatedWidget {

    private NetworkTableInstance inst;
    private NetworkTable table;

    private NetworkTableEntry xPos;
    private NetworkTableEntry yPos;

    @FXML
    AnchorPane root;

    @FXML
    NumberAxis xAxis = new NumberAxis();

    @FXML
    NumberAxis yAxis = new NumberAxis();

    @FXML
    LineChart<Number, Number> coordinate;

    @FXML
    CheckBox checkBox;

    @FXML
    ChoiceBox<Number> choiceBox;

    private XYChart.Series<Number, Number> series = new XYChart.Series<>();

    // hold data point for y
    public ConcurrentLinkedQueue<Number> pointY = new ConcurrentLinkedQueue<>();

    // hold data point for x
    public ConcurrentLinkedQueue<Number> pointX = new ConcurrentLinkedQueue<>();

    // generate random values
    public ExecutorService executor;

    private boolean isReversed = false;

    private static boolean isRobot = false;

    public CoordinateWidget() {
        if (isRobot) {
            this.inst = NetworkTableInstance.getDefault();
            this.table = inst.getTable("positions");
            this.xPos = table.getEntry("xPos");
            this.yPos = table.getEntry("yPos");
        }
    }

    @FXML
    public void initialize() {

        // Creates the LineChart
        coordinate.setTitle("Live Robot Coordinates");
        coordinate.setAnimated(true);
        coordinate.setHorizontalGridLinesVisible(true);
        coordinate.setVerticalGridLinesVisible(true);
        coordinate.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        series.setName("Coords");

        // add series to chart
        coordinate.getData().addAll(series);

        choiceBox.getItems().add(2000);
        choiceBox.getItems().add(1000);
        choiceBox.getItems().add(500);
        choiceBox.getItems().add(50);

        choiceBox.setValue(2000);


        executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r); // makes a new thread and runs
                thread.setDaemon(true);
                return thread;
            }
        });

        reset();

        // live update
        AddToQueue addToQueue = new AddToQueue();
        executor.execute(addToQueue);

        prepareTimeline();
    }

    @FXML
    public void reset() {
        coordinate.setAnimated(false);
        series.getData().clear();
        coordinate.setAnimated(true);

        isReversed = checkBox.isSelected();
        if (isReversed) {
            coordinate.nodeOrientationProperty().set(NodeOrientation.RIGHT_TO_LEFT);

        } else {
            coordinate.nodeOrientationProperty().set(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    public void addDataToSeries() {
        for (int i = 0; i < 20; i++) {
            if (pointY.isEmpty())
                break;
            series.getData().add(new XYChart.Data<>(pointX.remove(), pointY.remove()));
        }
    }

    public void prepareTimeline() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    public static void setRobot(boolean robot) {
          isRobot = robot;
    }

    public class AddToQueue implements Runnable {
        private DoubleSupplier x;
        private DoubleSupplier y;

        public AddToQueue() {
            if (isRobot) {
                x = () -> xPos.getDouble(0);
                y = () -> yPos.getDouble(0);
            } else {
                x = () -> Math.random() * 10;
                y = () -> Math.random() * 10;
            }
        }

        public void run() {
            try {

                if (isReversed) {
                        pointY.add(x.getAsDouble());
                        pointX.add(y.getAsDouble());

                } else {
                    pointY.add(y.getAsDouble());
                    pointX.add(x.getAsDouble());
                }

                Thread.sleep(choiceBox.getValue().longValue()); // recognize that graphing is time based.
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
