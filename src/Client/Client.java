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
	//TODO create the 2 threads (thread principal = send thread ; listening thread will be overwrited when changing of district)
	//TODO menu : switch between options : write each method

    public static void main(String[] args) {
        System.out.println(CLIENT);
        Client c=new Client();
        c.connectionServer();
    }

    private void connectionServer(){
        Scanner scan = new Scanner(System.in);  // Reading from System.in

        System.out.println(CLIENT+"Enter IP Server Central:");
        //TODO Remove when it will matter
        System.out.println(CLIENT+"Currently does not matter");
        String ipServer = scan.next();
        System.out.println(CLIENT+"Enter Port Server Central:");
        //TODO Remove when it will matter
        System.out.println(CLIENT+"Currently on 2009:");
        while (!scan.hasNextInt()) {
            System.out.println("You have badly written the port, do it again (it has to be an integer)");
            scan.next();
        }
        Integer portServer = scan.nextInt();
        System.out.println(CLIENT+"Enter District to Connect to:");
        //TODO Remove when it will matter
        System.out.println(CLIENT+"Currently does not matter");
        String district = scan.next();

        scan.close();

        if(this.askServerCentral(portServer, ipServer, district)) {
            openMenu();
        }else{
            System.out.println("Authorization refused from the server central.");
        }

    }

    public boolean askServerCentral(int port, String host, String district) {
        Socket socket;
        Boolean responseFromServer = null;

        try {
            //TODO update when deployed
            socket = new Socket(InetAddress.getLocalHost(), port);
            //socket = new Socket(host, port);

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(String.valueOf(district));

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            responseFromServer = objectInputStream.readBoolean();

            inputStream.close();
            objectOutputStream.close();
            socket.close();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return(responseFromServer);
    }

    public void openMenu(){
        System.out.println(CLIENT+"Console");
        System.out.println(CLIENT+"(1) List the Titans");
        System.out.println(CLIENT+"(2) Change District");
        System.out.println(CLIENT+"(3) Capture Titan");
        System.out.println(CLIENT+"(4) Kill Titan");
        System.out.println(CLIENT+"(5) List Captured Titans");
        System.out.println(CLIENT+"(6) List Killed Titans");
    }
}
