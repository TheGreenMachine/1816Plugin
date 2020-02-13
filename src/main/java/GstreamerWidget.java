import java.nio.ByteOrder;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.scene.layout.Pane;
import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.message.Message;
import org.freedesktop.gstreamer.elements.AppSink;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/*
 * The main idea is to create a pipeline that has an appsink to display the images.
 * Connect the AppSink to rest of the pipeline.
 * Connect the AppSinkListener to the AppSink.
 * The AppSink writes frames to the ImageContainer.
 * if you want to display the Videoframes simply add a changeListener to the container who will draw the current
 * Image to a Canvas or ImageView.
 */

@Description(name = "GStreamer", dataTypes = AppSinkListener.class)

public class GstreamerWidget extends SimpleAnnotatedWidget {


    private ImageView imageView;
    private AppSink videosink;
    private Pipeline pipe;
    private Bin bin;
    private BorderPane borderPane;
    Bus bus;
    private StringBuilder caps;
    private ImageContainer imageContainer;
    private  int port;

    public GstreamerWidget() {
        port = 1234;

        // Create Element
        Element source = ElementFactory.make("udpsrc", "source");
        videosink = new AppSink("GstVideoComponent");
        videosink.set("emit-signals", true);
        AppSinkListener GstListener = new AppSinkListener();
        videosink.connect(GstListener);
        caps = new StringBuilder("video/x-raw, ");
        // JNA creates ByteBuffer using native byte order, set masks according to that.
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            caps.append("format=BGRx");
        } else {
            caps.append("format=xRGB");
        }
        source.set("port", port);
        videosink.setCaps(new Caps(caps.toString()));
        videosink.set("max-buffers", 5000);
        videosink.set("drop", true);
        bin = Gst.parseBinFromDescription("rtph264depay ! avdec_h264 ! videoconvert", true);

        pipe = new Pipeline();
        pipe.addMany(source, bin, videosink);
        Pipeline.linkMany(source, bin, videosink);
        imageView = new ImageView();

        imageContainer = GstListener.getImageContainer();
        imageContainer.addListener(new ChangeListener<Image>() {

            @Override
            public void changed(ObservableValue<? extends Image> observable, Image oldValue, final
            Image newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImage(newValue);
                    }
                });

            }

        });

        bus = pipe.getBus();
        bus.connect(new Bus.MESSAGE() {

            @Override
            public void busMessage(Bus arg0, Message arg1) {

                System.out.println(arg1.getStructure());
            }
        });

        pipe.play();
    }

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setTitle("Video Stream Test");
//        BorderPane grid = new BorderPane();
//        grid.setCenter(imageView);
//        imageView.fitWidthProperty().bind(grid.widthProperty());
//        imageView.fitHeightProperty().bind(grid.heightProperty());
//        imageView.setPreserveRatio(true);
//        primaryStage.setScene(new Scene(grid, 960, 540));
//        primaryStage.show();
//    }

    @Override
    public Pane getView() {
        return borderPane;
    }
}