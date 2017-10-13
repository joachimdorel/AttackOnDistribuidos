package Distributed;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MainDistributed {

    private static int port=7000;

    //The group address must be in the range 224.0.0.0 to 239.255.255.255
    static String group = new String("226.1.0.2");

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
}