package classes;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class HolderConnection {

    AnswerListener listener;

    DataInputStream in;
    DataOutputStream out;

    static HolderConnection connection;

    public static void keep(HolderConnection kept) {
        connection = kept;
    }

    static public HolderConnection get() {
        return connection;
    }


    public void connect() {}

    public void waitForAnswer() {
        try {
            String line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
            System.out.println(line);
            listener.OnAnswerGot(Integer.parseInt(line));
        } catch (Exception e) {
            System.out.println("Server's down");
        }
    }

    public void flushAnswer(int ans) {
        try{
            out.writeUTF(Integer.toString(ans)); // отсылаем клиенту обратно ту самую строку текста.
            out.flush(); // заставляем поток закончить передачу данных.
        } catch (Exception e) {
            System.out.println("Server's down");
        }
    }

    public void secondTurner() {}


    public void setListener(AnswerListener answer) {
        this.listener = answer;
    }

    public interface AnswerListener {
        void OnAnswerGot(int answer);
    }
}
