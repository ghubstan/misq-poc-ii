package org.misq.application.jfx;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApplicationMain extends Application {
    private static final Logger log = LoggerFactory.getLogger(ApplicationMain.class);

    // Add JVM Opts to IDE Launcher:
    //  --module-path <path-to>/javafx-sdk-16/lib --add-modules=javafx.controls,javafx.fxml,javafx.graphics

    // Package the jfx-demo jar:
    //  $ export BISQ_SHARED_FOLDER=/tmp
    //  $ ./gradlew clean build
    //  $ ./gradlew --console=plain packageInstallers


    public static void main(String[] args) {
        log.info("launch");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("start");
        FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource("/SearchController.fxml"));
        AnchorPane page = loader.load();
        Scene scene = new Scene(page);

        scene.getStylesheets().add("/search.css");

        primaryStage.setTitle("Title goes here");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
