package WipServer;

import quiztastic.core.Player;
import quiztastic.ui.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

public class ClientHandler implements Runnable {
    private Set<String> players; //temp player info
    private Player clientUserName; //clients - players username
    private final BufferedReader in;
    private final PrintWriter out;
    private final ArrayList<ClientHandler> clients; //todo


    public ClientHandler(Socket ClientSocklet, ArrayList<ClientHandler> clients, Set<String> players) throws IOException { //consructor
        this.players = players;
        this.clients = clients;
        in = new BufferedReader((new InputStreamReader(ClientSocklet.getInputStream())));
        out = new PrintWriter(ClientSocklet.getOutputStream(), true);
    }

    @Override
    public void run() { //thread start for client
        try {
            out.println("what is your username?:");
            out.flush();
            String username = in.readLine(); //check if username is allowed / enabled < - >
            try {
                if (JeopardyServer.addPlayers(username) || username.isEmpty()) {
                    out.println(username + " Is already in-game / not allowed");
                    out.println("Reconnect and try again.");
                    in.close();
                    out.close(); //dc client if username not allowed  - available todo fix change so it doesnt dc the person lol
                } else {
                    serverBroadCast(username + " added to the game");
                    serverBroadCast("Current users in lobby: " + getPlayersInLobby());
                    players.add(username);
                    clientUserName = new Player(username);
                    out.println("use 'say' to chat else use ! help for more infomation");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            while (true) {
                String userInput = in.readLine();
                int space = userInput.indexOf(" ");
                if (userInput.startsWith("say")) { //player broadcast msg
                    if (space != -1) {
                        out.println(userInput.substring(space + 1));
                        playerBroadCast(userInput.substring(space + 1));
                    }
                } else if (userInput.startsWith("!")) { //server 'commands'
                    if (space != -1) {
                        switch (userInput.substring(space + 1)) {
                            case "help":
                                out.println("this is the help command it helps the player");
                                break;
                            case "play":
                               Protocol p = new Protocol(in,out, clientUserName, "en");
                               serverBroadCast(clientUserName.toString() + " has started a game ");
                                p.run();
                                break;
                            case "players":
                                out.println("hot gamer babes:" + getPlayersInLobby());
                                break;
                            case "spil":
                                out.println("work in progress");
                                Protocol po = new Protocol(in,out, clientUserName, "da");
                                serverBroadCast(clientUserName.toString() + " has started a game ");
                                po.run();
                                break;
                            default:

                        }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (JeopardyServer.removePlayer(clientUserName.toString())) {
                    removePlayer(clientUserName.toString());
                    serverBroadCast(clientUserName + " has left");
                    getPlayersInLobby();
                    clientUserName = null;
                }
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void serverBroadCast(String substring) { //server broadcast
        for (ClientHandler clientHandler : clients) {
            clientHandler.out.println("[SERVER] " + substring);
        }
    }

    private void playerBroadCast(String substring) { //player - client boradcast
        for (ClientHandler clientHandler : clients) {
            if (clientUserName == null) {
            } else {
                clientHandler.out.println(clientUserName + " Said: " + substring);
            }
        }

    }

    public Set<String> getPlayersInLobby() { //gets players on server
        players = JeopardyServer.getPlayers();
        return players;
    }

    public void removePlayer(String player) { //remove players
        players.remove(player);
        JeopardyServer.removePlayer(player);
    }
}
