package classes;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javax.swing.*;

public class ControllerGame {

    private Pane [][] layout;
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
            layout[answer / 10][answer % 10].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));;
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int k = i;
                final int l = j;

                layout[i][j].setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                layout[i][j].setOnMouseClicked(event -> {
                    layout[k][l].setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

                    System.out.println(k + " " + l);
                    server.flushAnswer(10*k + l);

                    SwingUtilities.invokeLater(() -> server.waitForAnswer());
                });
            }
        }

        SwingUtilities.invokeLater(() -> server.secondTurner());
    }
}
