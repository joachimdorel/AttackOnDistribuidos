package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainClient {

    public static void main(String[] args) {
        System.out.println("Hello Client!");

        connectionUnicast();

    }

    public static void connectionUnicast() {
        Socket socket;

        try {
            System.out.println("Client - Connection Opened");
            socket = new Socket(InetAddress.getLocalHost(),2009);
            socket.close();
            System.out.println("Client - Connection Closed");
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}