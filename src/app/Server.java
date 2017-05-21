package app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
                executeCommand(command);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void executeCommand(Command command) {

        try {
            Calc calc = (Calc) Class.forName("app." + command.getClassName()).newInstance();
            returnToClient(calc.test());

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void returnToClient(String response) {

        try {
            ObjectOutputStream outToServer = new ObjectOutputStream(socketConnection.getOutputStream());
            //Send command to server

            outToServer.writeUTF(response);
            outToServer.flush();
            outToServer.close();
            socketConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Port number: ");
        int port = reader.nextInt();

        Server server = new Server(port);
        server.order();
    }



}
