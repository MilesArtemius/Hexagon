package classes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.beans.EventHandler;
import java.io.IOException;

public class ControllerGame {

    private Pane [][] layout;
    private int [][] field = new int[3][3];
    private HolderConnection server;

    @FXML
    public Pane item00;

    @FXML
    public Pane item01;

    @FXML
    public Pane item02;

    @FXML
    public Pane item10;

    @FXML
    public Pane item11;

    @FXML
    public Pane item12;

    @FXML
    public Pane item20;

    @FXML
    public Pane item21;

    @FXML
    public Pane item22;

    @FXML
    private void initialize() {
        layout = new Pane [][] {{item00, item01, item02}, {item10, item11, item12}, {item20, item21, item22}};

        server = HolderConnection.get();

        server.setListener(answer -> {
            layout[answer / 10][answer % 10].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

            field[answer / 10][answer % 10] = 2;
            checkPosition("You lose!");
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int k = i;
                final int l = j;

                field[i][j] = i + j + 5;
                layout[i][j].setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                layout[i][j].setOnMouseClicked(event -> {
                    if (server.isReadyForAction) {
                        layout[k][l].setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

                        System.out.println(k + " " + l);
                        server.flushAnswer(10 * k + l);

                        field[k][l] = 1;
                        checkPosition("You win!");

                        SwingUtilities.invokeLater(() -> server.waitForAnswer());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Please, wait");
                        alert.setHeaderText(null);
                        alert.setContentText("It's not your turn, please, wait until the other player makes his");
                        alert.show();
                    }
                });
            }
        }

        SwingUtilities.invokeLater(() -> server.secondTurner());
    }

    private void checkPosition(String winner) {
        if (((field[0][0] == field[0][1]) && (field[0][0] == field[0][2])) ||
                ((field[1][0] == field[1][1]) && (field[1][0] == field[1][2])) ||
                ((field[2][0] == field[2][1]) && (field[2][0] == field[2][2])) ||
                ((field[0][0] == field[1][0]) && (field[0][0] == field[2][0])) ||
                ((field[0][1] == field[1][1]) && (field[0][1] == field[2][1])) ||
                ((field[0][2] == field[1][2]) && (field[0][2] == field[2][2])) ||
                ((field[0][0] == field[1][1]) && (field[0][0] == field[2][2])) ||
                ((field[0][2] == field[1][1]) && (field[0][2] == field[2][0]))) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Congratulations");
            alert.setHeaderText(null);
            alert.setContentText(winner);
            alert.setOnCloseRequest(new javafx.event.EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent event) {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
                        ((Stage) item00.getScene().getWindow()).setScene(new Scene(root, 600, 600));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            alert.show();
        }
    }
}
