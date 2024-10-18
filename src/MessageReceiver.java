import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class MessageReceiver implements Runnable{
    private final Socket socket;

    public MessageReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String msg = br.readLine();
                if(msg != null){
                    System.out.println(msg);
                }
            }
        } catch (IOException e) {
        }

    }
}

