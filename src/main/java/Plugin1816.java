import com.google.common.collect.ImmutableList;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

import java.util.List;

@Description(group = "edu.wpi.first.shuffleboard", name = "Green Machine Plugin", version = "1.1.0", summary = "Base Plugin for all Green Machine Widgets")
public class Plugin1816 extends Plugin {

    public Plugin1816() {
        super();
        CoordinateWidget.setRobot(true);
    }

    @Override
    public List<ComponentType> getComponents() {
        return ImmutableList.of(
                WidgetType.forAnnotatedWidget(CoordinateWidget.class),
                WidgetType.forAnnotatedWidget(GstreamerWidget.class));

    }
}
