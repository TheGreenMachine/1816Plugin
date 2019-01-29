# 1816 Plugin

This is the robot position grapher which intakes the x and y coordinates of 
the robot through a `NetworkTable`. 


### What it includes

This project includes the base plugin `Plugin1816` for future development of widgets. 

In order to add your widget to the plugin do as such:
```
return ImmutableList.of(WidgetType.forAnnotatedWidget(insert_YourWidgetClassName.class))
``` 
within your `getComponents()` method in the Plugin class.

It also includes one widget (Robot Grapher) which receives the coordinates of the robot through Networktables and visualizes it on a coordinate plane.

For further information on how to develop a widget (you won't need to know how to make a plugin because it's already made)
[End-to-end custom widget example](https://github.com/wpilibsuite/shuffleboard/wiki/End-to-end-custom-data---widget-example).

### Compiling your widget

Before compiling your widget and plugin into a jar (or even working on widget), make sure in your `build.gradle` file that you include these dependencies:

```
'edu.wpi.first.shuffleboard:api:'  + current-shuffleboard-version
'edu.wpi.first.shuffleboard.plugin:networktables:' + current-shuffleboard-version` 
```

To compile your widget and the plugin into a jar go into your terminal and redirect the source
to your local project and do `./gradlew jar` BEFORE you do this, make sure your in your `build.gradle` file
that you have 
```gradle
jar {
    manifest {
        attributes 'Main-Class' : ''
    }
    baseName = 'RobotGraphWidget'
}
``` 
which allows you to create your jar file, which then you can drag into the 'plugins' folder inside wherever your Shuffleboard folder is located.

### Keeping up-to-date
Inside your `build.gradle` file, particularly the `openjfx` plugin which includes JavaFX version 11 since
Java 11+ no longer includes JavaFX, be sure to change the version to the latest JFX version
whenever there is a new release, along with any new Java SDK releases.

The same applies to the Shuffleboard version, which you can also find your running version in the
`build.gradle` file. 


