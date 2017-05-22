package app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
                executeCommand(command);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Execute command using reflexion
     */
    public void executeCommand(Command command) {


        try {
            Class<?> c = Class.forName(command.getClassName());
            Object cObject = c.newInstance();
            Class<?>[] paramTypes = {String.class, String.class};
            Method cMethod = cObject.getClass().getMethod(command.getMethodName(), paramTypes);
            Integer result = (Integer) cMethod.invoke(cObject,  command.getParams().get(0), command.getParams().get(1));

            returnToClient(Integer.toString(result));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void returnToClient(String response) {

        try {
            ObjectOutputStream outToServer = new ObjectOutputStream(socketConnection.getOutputStream());

            outToServer.writeUTF(response);
            outToServer.flush();
            outToServer.close();
            socketConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        order();
    }


    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Port number: ");
        int port = reader.nextInt();

        Server server = new Server(port);
        server.order();
    }
}
