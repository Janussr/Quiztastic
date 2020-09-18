package WipServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static String SERVER_IP;
    private static int PORT;

    public static void main(String[] args) throws IOException {
        getServerInfo();

    }

    private static void getServerInfo() throws IOException {
        String genericConnectLine = "Connecting to: " + SERVER_IP + ":" + PORT;
        int userINTput;
        Scanner scanner = new Scanner(System.in);
        System.out.println("TEAM JUMBO SNEGL - JEOPARDY CLIENT:\nMenu: \n1. Auto connect to localhost \n2. Auto connect to droplet \n3. Manual connect.");
        System.out.print(">");
        userINTput = scanner.nextInt();
        switch (userINTput){
            case 1:
                SERVER_IP = "LOCALHOST";
                PORT = 3400;
                genericConnectLine = "Connecting to: " + SERVER_IP + ":" + PORT;
                System.out.println(genericConnectLine);
                connectToJeopardy();

                break;
            case 2:
                SERVER_IP = "165.232.77.202";
                PORT = 3400;
                genericConnectLine = "Connecting to: " + SERVER_IP + ":" + PORT;
                System.out.println(genericConnectLine);
                connectToJeopardy();
                break;
            case 3:
                System.out.println("IP: ");
                SERVER_IP = scanner.nextLine();
                System.out.println("Port:");
                PORT = scanner.nextInt();
                if (!SERVER_IP.isEmpty() && PORT == 3400) {
                    System.out.println("Attempting to connect to Jeopardy server using: " + SERVER_IP + ":" + PORT);
                    connectToJeopardy();
                } else {
                    System.out.println("ERROR IN INPUT");
                    getServerInfo();
                }
                    break;
            default:
                System.out.println("ERROR");
                getServerInfo();
        }



    }

    private static void connectToJeopardy() throws IOException {
        try {
            Socket socket = new Socket(SERVER_IP, PORT);
            ServerHandler serverOut = new ServerHandler(socket);
            BufferedReader userIN = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(serverOut).start();
            System.out.println("\nconnected...");
            while (true) {
                out.flush();
                System.out.print("> ");
                String command = userIN.readLine();
                if (command.equals("dc"))break;
                out.println(command);
            }
            out.close();
            userIN.close();
            getServerInfo();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
