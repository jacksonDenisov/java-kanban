import KV_Client_Server.KVServer;
import Server.HttpTaskServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            new KVServer().start();
            new HttpTaskServer().start();
        } catch (IOException e) {
            System.out.println("Ошибка при запуске серверов");
        }
    }
}
