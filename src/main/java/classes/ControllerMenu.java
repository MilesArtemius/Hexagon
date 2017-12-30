package classes;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ControllerMenu {

    @FXML
    public Button server_button;

    @FXML
    public Button client_button;

    @FXML
    private void initialize() {
        try {
            System.out.println(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        server_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    HolderServer server = new HolderServer();
                    server.connect();
                    HolderConnection.keep(server);

                    Parent root = FXMLLoader.load(getClass().getResource("/game.fxml"));
                    ((Stage) server_button.getScene().getWindow()).setScene(new Scene(root, 600, 600));
                } catch (IOException e) {
                    System.out.println("Something went wrong...");
                }
            }
        });

        client_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    HolderClient client = new HolderClient();
                    client.connect();
                    HolderConnection.keep(client);

                    Parent root = FXMLLoader.load(getClass().getResource("/game.fxml"));
                    ((Stage) client_button.getScene().getWindow()).setScene(new Scene(root, 600, 600));
                } catch (IOException e) {
                    System.out.println("Something went wrong...");
                }
            }
        });
    }
}
