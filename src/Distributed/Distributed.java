package Distributed;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

/**
 * A distributed file system is a client/server-based application that allows clients
 * to access and process data stored on the server as if it were on their own computer.
 * When a user accesses a file on the server, the server sends the user a copy of the file,
 * which is cached on the user's computer while the data is being processed and is then returned to the server.
 */


//TODO figure out how to launch threads when creating a new district 
//TODO events : notify to all when titan captured/killed (thread always activ listens to the messages sent by clients, other one treats demands)


public class Distributed {

    /**
     * Parameters to change dynamically after
     */
    private static int port=7000;
    //The group address must be in the range 224.0.0.0 to 239.255.255.255
    static String group = new String("226.1.0.2");



    public Distributed() {
        String port;
        String groupAddress;
    }

    /*
    public static void main(String[] args) {
        System.out.println("Hello Distributed!");

        InetAddress groupAddress;
        MulticastSocket socketMulticast;

        try {
            socketMulticast = new MulticastSocket(port);
            groupAddress = InetAddress.getByName(group);

            socketMulticast.joinGroup(groupAddress); // unirse a grupo de multicast

            // Listening to a message
            while (true) {
                byte[] buffer = new byte[100];
                DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
                socketMulticast.receive(datagram);
                String message = new String(datagram.getData());
                System.out.println("Message received : "+message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    private void selectPortAddress(){
        Scanner scan = new Scanner(System.in);  // Reading from System.in

        System.out.println("Select a port : ");
        String port = scan.next();

        System.out.println("Select a group address : ");
        String address = scan.next();

    }
}
