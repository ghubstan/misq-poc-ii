package misq.jfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationMain extends Application {

    // Add JVM Opts to IDE Launcher:
    //  --module-path <path-to>/javafx-sdk-16/lib --add-modules=javafx.controls,javafx.graphics

    // Package the jfx jar:
    //  $ export BISQ_SHARED_FOLDER=/tmp
    //  $ ./gradlew clean build
    //  $ ./gradlew --console=plain packageInstallers

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new Pane());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
