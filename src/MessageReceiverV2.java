import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class MessageReceiverV2 implements Runnable{
    private final Socket socket;
    private  JTextArea ta;

    public MessageReceiverV2(Socket socket, JTextArea ta) {
        this.socket = socket;
        this.ta = ta;
    }

    @Override
    public void run() {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String msg = br.readLine();
                if(msg != null){
                    ta.append(msg + "\n");
                }
            }
        } catch (IOException e) {
        }

    }
}

