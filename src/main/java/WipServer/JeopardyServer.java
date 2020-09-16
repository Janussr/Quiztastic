package WipServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JeopardyServer {
    private static int PORT = 3400;
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); //client threads
    private static Set<String> players = new HashSet<>(); //players / clients on server
    private List<Thread> listOfThreads; //todo list of threads
    private static ExecutorService pool = Executors.newFixedThreadPool(6); // pool of threads / clients / players

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("[SERVER] listeing on port: " + PORT); // starting server
        while (true) {
            Socket client = listener.accept(); //accept client - todo if max treads started add to que
            InetAddress info;
            info = client.getInetAddress(); //delete for later just to get some useless ifno
            System.out.println("[SERVER] Client has connected " + client.getInetAddress());
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println("Welcome to Team JUMBO SNEGL SERVER");
            out.println(info.getCanonicalHostName() + " // " + info.getHostName() + " // " + info.getHostAddress() + " // " + PORT + "]"); //useless info delte
            ClientHandler clientThread = new ClientHandler(client, clientHandlers,players);
            clientHandlers.add(clientThread); //threading add client to thread
            pool.execute(clientThread); //start thread
        }
    }

    public static boolean addPlayers(String player) {
        if (players.contains(player))
        {
            System.out.println("[SERVER] User already in party");
            return true;
        }
        else {
            players.add(player);
            System.out.println("[SERVER] User ADDED to party");
            return false;
        }
    }
    public static boolean removePlayer(String player){
        if (players.contains(player)){
            players.remove(player);
            System.out.println("  [SERVER] removed: "+player);
            return true;
        } else {
            return false;
        }
    }
    public static Set<String> getPlayers() {
        return players;
    }
}
