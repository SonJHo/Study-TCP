import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client2 {
    public static void main(String[] args) throws IOException {
        final String address = "192.168.45.69";
        Socket socket;
        BufferedReader br;
        BufferedReader br_input;
        PrintWriter pw;
        String userName;
        try {
            socket = new Socket(address, 8888);

            //br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            br_input = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("NickName:");
            userName = br_input.readLine();
            pw.println(userName);

        } catch (UnknownHostException e) {
            System.out.println("error");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("connect error!!");
            return;
        }

        new Thread(new MessageReceiver(socket)).start();

        while (true) {
            System.out.println("send message:");
            String msg = br_input.readLine();
            pw.println(msg);
            if (msg.equals("quit")) {
                break;
            }
        }
        System.out.println("종료");
        socket.close();
    }

}

