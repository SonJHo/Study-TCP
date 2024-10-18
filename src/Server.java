
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import static utils.MyUtils.*;

public class Server {

    public static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        final int PORT = 8080;
        final String IP = "address";
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println(log("server open..."));
            System.out.println("===================================");
        } catch (IOException e) {
            System.out.println(log("server open error!!"));
        }


        while (true) {
            Socket socket = serverSocket.accept(); // 서버생성, client  접속 대기
            new Thread(new ClientHandler(socket)).start();
        }


    }
}
