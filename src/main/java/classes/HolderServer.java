package classes;

import classes.HolderConnection;

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

            socket = ss.accept(); // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
            System.out.println("Got a client :) ... Finally, someone saw me through all the cover!");
            System.out.println();

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

    /*public void setListener(AnswerListener answer) {
        this.listener = answer;
    }

    public interface AnswerListener {
        void OnAnswerGot(int answer);
    }*/
}
