package app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by hugo on 17-05-21.
 */
public class Server {

    private int port;
    private ServerSocket serverSocket;
    private Socket socketConnection;


    public Server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void order() {
        if(serverSocket != null){
            try {
                socketConnection = serverSocket.accept();
                ObjectInputStream fromClient = new ObjectInputStream(socketConnection.getInputStream());
                Command command = (Command) fromClient.readObject();
                System.out.println(command.getClassName());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


    public void executeCommand(Command command) {
        switch (command.getClassName()) {
            case "Calc":

                break;
            default:
                returnToClient("Close server socket");
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void returnToClient(String response) {

    }


    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Port number: ");
        int port = reader.nextInt();

        Server server = new Server(port);
        server.order();
    }



}
