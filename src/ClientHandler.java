import java.io.*;
import java.net.Socket;
import java.time.LocalTime;

public class ClientHandler implements Runnable {
    private Socket socket;

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
            System.out.println("**" + userName + " is join **");
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println("[" + LocalTime.now() + "]" + userName + ":" + line);
                broadcastMessage(line);
                if (line.equals("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
        } finally {
            Server.clientHandlers.remove(this);
            System.out.println("**" + userName + " is quit **");
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("종료중 error");
            }
        }
    }

    private void broadcastMessage(String line) {
        for (ClientHandler clientHandler : Server.clientHandlers) {
            clientHandler.pw.println("[" + LocalTime.now() + "]" + userName + ":" + line);
        }
    }
}
