package WipServer;

import java.io.*;
import java.net.Socket;

public class ServerHandler implements Runnable {
    private String serverHandle = ">";
    private Socket server;
    private BufferedReader in;

    public ServerHandler(Socket socket) throws IOException {
        server = socket;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }

    @Override
    public void run() { //thread
        String serverResponds = null;
        try {
            while (true) {
                serverResponds = in.readLine();
                if (serverResponds == null) break;
                System.out.println(serverHandle + " " + serverResponds); //message bounce back
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
