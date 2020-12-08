package com.edinarobotics.plugin;

import com.edinarobotics.plugin.data.Point2DType;
import com.edinarobotics.plugin.widget.CoordinateWidget;
import com.edinarobotics.plugin.widget.GstreamerWidget;
import com.google.common.collect.ImmutableList;
import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.theme.Theme;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

import java.util.List;


@Description(group = "com.edinarobotics", name = "Green Machine Plugin", version = "2.3.0", summary = "Base Plugin for all Green Machine Widgets")
public class Plugin1816 extends Plugin {

    private static final Theme theme = new Theme(Plugin1816.class, "Green Machine", "/com/edinarobotics/plugin/greenmachinetheme.css");

    public Plugin1816() {
        super();
        CoordinateWidget.setRobot(true);
    }

    @Override
    public List<ComponentType> getComponents() {
        return ImmutableList.of(
                WidgetType.forAnnotatedWidget(CoordinateWidget.class),
                WidgetType.forAnnotatedWidget(GstreamerWidget.class)
        );
    }

    @Override
    public List<DataType> getDataTypes() {
        return ImmutableList.of(
            Point2DType.Instance
        );
    }

    @Override
    public List<Theme> getThemes() {
        return ImmutableList.of(theme);
    }
}
