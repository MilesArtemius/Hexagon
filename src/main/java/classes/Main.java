package classes;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main extends Application {

    private static String userName;

    private String popUpDialog() {
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Please, think of a username");

        // Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(this.getClass().getResource("/login.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                loginButton.setDisable(true);
            } else {
                loginButton.setDisable(false);
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> root = (HashMap<String, String>) dataSnapshot.getValue();
                        for (Map.Entry<String, String> entry : root.entrySet()) {
                            if (newValue.trim().equals(entry.getKey())) {
                                loginButton.setDisable(true);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
            }
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return (username.getText());
            } else {
                return ("null");
            }
        });

        Optional<String> result = dialog.showAndWait();

        return result.orElse("null");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/menu.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();

        try {
            Path temp = Files.createTempFile("resource", ".json");
            Files.copy(getClass().getResourceAsStream("/connector.json"), temp, StandardCopyOption.REPLACE_EXISTING);
            FileInputStream serviceAccount = new FileInputStream(temp.toFile());
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl("https://hexagon-the-game.firebaseio.com/")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            System.out.println("game .jar damaged");
            e.printStackTrace();
        }

        try {
            userName =popUpDialog();
            FirebaseDatabase.getInstance().getReference().child(userName)
                    .setValue(InetAddress.getLocalHost().toString()
                    .substring(InetAddress.getLocalHost().toString().indexOf("/") + 1));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
        try {
            FirebaseDatabase.getInstance().getReference().child(userName).removeValue();
        } catch (Exception e) {
            System.out.println("exit parameter lost");
            e.printStackTrace();
        }
    }
}
