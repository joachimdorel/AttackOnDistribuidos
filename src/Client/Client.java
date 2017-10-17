package Client;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final String CLIENT = "[CLIENT] ";

    public Client() {
    }

    public static void main(String[] args) {
        Client c=new Client();
        c.connectionServer();
    }

    private void connectionServer(){
        Scanner scan = new Scanner(System.in);  // Reading from System.in

        System.out.println(CLIENT+"Enter IP Server Central:");
        System.out.println(CLIENT+"Currently does not matter");
        String ipServer = scan.next();
        System.out.println(CLIENT+"Enter Port Server Central:");
        System.out.println(CLIENT+"Currently on 2009:");
        while (!scan.hasNextInt()) {
            System.out.println("You have badly written the port, do it again (it have to be a integer)");
            scan.next();
        }
        Integer portServer = scan.nextInt();
        System.out.println(CLIENT+"Enter District to Connect to:");
        System.out.println(CLIENT+"Currently does not matter");
        String district = scan.next();

        scan.close();

        this.askServerCentral(portServer, ipServer, district);

    }

    public void askServerCentral(int port, String host, String district) {
        Socket socket;
        String responseFromServer = null;

        try {
            //TODO update when deployed
            socket = new Socket(InetAddress.getLocalHost(), port);
            //socket = new Socket(host, port);

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(String.valueOf(district));

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try {
                responseFromServer = String.valueOf(objectInputStream.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            inputStream.close();
            objectOutputStream.close();
            socket.close();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(responseFromServer);
    }
}