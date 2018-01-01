package classes;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class HolderClient extends HolderConnection {

    String answer;

    @Override
    public void connect() {
        int serverPort = 5555; // здесь обязательно нужно указать порт к которому привязывается сервер.
        String address = seekServer(); // это IP-адрес компьютера, где исполняется наша серверная программа.
        // Здесь указан адрес того самого компьютера где будет исполняться и клиент.

        try {
            InetAddress ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
            System.out.println("Any of you heard of a socket with IP address " + address + " and port " + serverPort + "?");
            Socket socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
            System.out.println("Yes! I just got hold of the program.");

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            in = new DataInputStream(sin);
            out = new DataOutputStream(sout);

            isReadyForAction = false;
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private String seekServer() {
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Choose your server");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(1, 1, 1, 1));
        vbox.setSpacing(10);

        CountDownLatch latch = new CountDownLatch(1);

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> root = (HashMap<String, String>) dataSnapshot.getValue();
                System.out.println(root);

                String myIp = "null";
                try {
                    myIp = InetAddress.getLocalHost().toString().substring(InetAddress.getLocalHost().toString().indexOf("/") + 1);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                for (Map.Entry<String, String> entry : root.entrySet()) {
                    if (!entry.getValue().equals(myIp)) {
                        String value = entry.getValue();

                        StackPane pane = new StackPane(new Label(entry.getKey()));
                        pane.setAlignment(Pos.CENTER_LEFT);
                        pane.setPadding(new Insets(5, 5, 5, 5));
                        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                for (Node node : vbox.getChildren()) {
                                    ((Pane) node).setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                                }
                                pane.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                                answer = value;
                            }
                        });
                        vbox.getChildren().add(pane);
                    }
                    latch.countDown();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dialog.getDialogPane().setContent(vbox);


        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return (answer);
            } else {
                return ("null");
            }
        });


        Optional<String> result = dialog.showAndWait();

        return result.get();
    }

    @Override
    public void secondTurner() {
        this.waitForAnswer();
    }
}
