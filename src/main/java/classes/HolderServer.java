package classes;

import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HolderServer extends HolderConnection {

    ServerSocket ss;
    Socket socket;
    int port;

    @Override
    public void connect() {
        port = 5555; // случайный порт (может быть любое число от 1025 до 65535)
        try {
            ss = new ServerSocket(port); // создаем сокет сервера и привязываем его к вышеуказанному порту
            System.out.println("Waiting for a client...");

            Dialog<Boolean> dialog = new Dialog<>();
            dialog.initStyle(StageStyle.UNIFIED);
            dialog.setTitle("Connecting...");
            dialog.setContentText("Waiting for connection...");
            dialog.show();

            socket = ss.accept(); // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
            System.out.println("Got a client :) ... Finally, someone saw me through all the cover!");
            System.out.println();

            dialog.setContentText("Connected!");
            dialog.setResult(Boolean.TRUE);
            dialog.close();

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            in = new DataInputStream(sin);
            out = new DataOutputStream(sout);
        } catch(Exception x) {
            x.printStackTrace();
        }
    }
}
