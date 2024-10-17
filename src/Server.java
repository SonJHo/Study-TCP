
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    public static List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        final int PORT = 8080;
        final String IP = "192.168.45.69";
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("server open..");
            System.out.println("==============================");
        } catch (IOException e) {
            System.out.println("포트 설정중 오류 발생");
        }


        while( true){
            Socket socket = serverSocket.accept(); // 서버생성, client  접속 대기
            new Thread(new ClientHandler(socket)).start();
        }

    }
}
