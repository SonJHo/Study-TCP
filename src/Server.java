
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import static utils.MyUtils.*;

public class Server {

    public static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());
    public static volatile boolean running = true;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        final int PORT = 8080;
        final String IP = "address";
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println(log("server open..."));
            System.out.println("===================================\n");
        } catch (IOException e) {
            System.out.println(log("server open error!!"));
        }


        Thread thread = new Thread(new ServerCommandTask(serverSocket));
        thread.start();

        while (running) {
            try {
                Socket socket = null; // 서버생성, client  접속 대기
                if (serverSocket != null) {
                    socket = serverSocket.accept();
                }
                new Thread(new ClientHandler(socket)).start();
            }catch (SocketException e){
                System.out.println(log(e.getMessage()));
                break;
            }
        }

        if (serverSocket != null) {
            serverSocket.close();
        }

    }
}

class ServerCommandTask implements Runnable {

    private BufferedReader br;
    private final ServerSocket serverSocket;

    public ServerCommandTask(ServerSocket serverSocket) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        // TODO: 2024-10-18 관리자 커맨드 추가해야함
        while (true) {
            try {
                String command = br.readLine();
                if (command.equals("shutdown")) {
                    System.out.println(log("server shutdown"));
                    Server.running = false;
                    serverSocket.close();
                    break;
                }
            } catch (IOException e) {
                System.out.println(log("command error!!"));
            }
        }
    }
}