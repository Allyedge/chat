package com.allyedge;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    GlobalState globalState = GlobalState.getInstance();

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("home"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() {
        globalState.getWebSocketClient();

        if (globalState.getWebSocketClient() != null) {
            globalState.getWebSocketClient().close();

            System.out.println("WebSocket connection closed.");
        }
    }

    public static void main(String[] args) {
        launch();

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    GlobalState globalState = GlobalState.getInstance();
                    globalState.getWebSocketClient();

                    if (globalState.getWebSocketClient() != null) {
                        globalState.getWebSocketClient().close();

                        System.out.println("WebSocket connection closed.");
                    }
                }));
    }
}