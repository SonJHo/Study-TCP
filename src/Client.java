import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        BufferedReader br = null;
        BufferedReader br_input ;
        PrintWriter pw = null;
        final String address = "192.168.45.69";
        String userName;
        try {
            socket = new Socket(address, 8080);
            //br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            br_input = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("NickName:");
            userName = br_input.readLine();
            pw.println(userName);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("error");
            return;
        }

        new Thread(new MessageReceiver(socket)).start();

        while(true){
            System.out.println("send message:");
            String msg = br_input.readLine();
            pw.println(msg);
            if(msg.equals("quit") ){
                break;
            }
        }
        socket.close();
    }

}

