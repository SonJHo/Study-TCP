package chatprogramV1;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils.MyUtils.*;

public class Server {

    public static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());
    public static volatile boolean running = true;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        final int PORT = 8888;
        try {

            serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("0.0.0.0"));
            System.out.println(log("server open..."));
            System.out.println("===================================\n");
        } catch (IOException e) {
            System.out.println(log("server open error!!"));
        }


        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new ServerCommandListenTask(serverSocket));

        while (running) {
            try {
                Socket socket = null; // 서버생성, client  접속 대기
                if (serverSocket != null) {
                    socket = serverSocket.accept();
                }
                es.execute(new ClientHandler(socket));
            } catch (SocketException e) {
                System.out.println(log("server "+ e.getMessage()));
                break;
            }
        }

        if (serverSocket != null) {
            serverSocket.close();
        }
        es.close();

    }
    static void broadcastMessage(String sender, String line) {
        for (ClientHandler clientHandler : Server.clientHandlers) {
            clientHandler.pw.println(sender + " " + line);
        }
    }

}

class ServerCommandListenTask implements Runnable {

    private BufferedReader br;
    private final ServerSocket serverSocket;

    public ServerCommandListenTask(ServerSocket serverSocket) {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {

                StringTokenizer st = new StringTokenizer(br.readLine(), " ");
                String command = st.nextToken();
                if (command.equals("shutdown")) { // 강제종료
                    System.out.println(log("server shutdown"));
                    Server.running = false;
                    serverSocket.close();
                    br.close();
                    break;
                } else if (command.equals("all")){ //전체 참여자 알림
                    String text = st.nextToken();
                    Server.broadcastMessage("<system>", text);
                    System.out.println(log("server all " + text));
                }else{

                    // TODO: 2024-11-27 관리자 커맨드 추가
                }
            } catch (IOException e) {
                System.out.println(log("command error!!"));
            } catch (NoSuchElementException e){
                System.out.println("command input error");
            }
        }
    }
}