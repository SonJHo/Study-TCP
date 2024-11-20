import java.io.*;
import java.net.Socket;

import static utils.MyUtils.log;

public class ClientHandler implements Runnable {
    private final Socket socket;

    private String userName;

    PrintWriter pw;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            Server.clientHandlers.add(this);

            userName = br.readLine();
            System.out.println(log("** " + userName + " is join **"));
            broadcastMessage(userName + "님이 입장 하셨습니다.");
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(log(userName + ":" + line));
                broadcastMessage(line);
                if (line.equals("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
        } finally {
            Server.clientHandlers.remove(this);
            System.out.println(log("** " + userName + " is quit **"));
            broadcastMessage(userName + "님이 퇴장 하셨습니다.");

            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(log("close error!!"));
            }
        }
    }

    private void broadcastMessage(String line) {
        for (ClientHandler clientHandler : Server.clientHandlers) {
            clientHandler.pw.println(userName + ":" + line);
        }
    }
}
