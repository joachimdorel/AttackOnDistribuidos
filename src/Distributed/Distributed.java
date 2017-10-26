package Distributed;

import Creature.Titans;
import Util.Const;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A distributed file system is a client/server-based application that allows clients
 * to access and process data stored on the server as if it were on their own computer.
 * When a user accesses a file on the server, the server sends the user a copy of the file,
 * which is cached on the user's computer while the data is being processed and is then returned to the server.
 */


//TODO figure out how to launch threads when creating a new district
    //TODO creation of a new distributed district server
//TODO events : notify to all when titan captured/killed (thread always activ listens to the messages sent by clients, other one treats demands)
//TODO: check all id are different

public class Distributed {
    private static final String DISTRIBUTED = " DISTRICT ";
    private String name = "";
    //The group address must be in the range 224.0.0.0 to 239.255.255.255
    private String multicastIP; //it is the group address
    private int multicastPort;
    private String requestIP;
    private int requestPort;
    private ArrayList<Titans> titansList = new ArrayList<Titans>();


    private Distributed(String name){
        this.name = name;
    }

    public static void main(String[] args) {
        System.out.println("Hello Distributed!");
        Scanner scan = new Scanner(System.in);  // Reading from System.in
        System.out.println("[" + DISTRIBUTED + "] " + "Server name : ");
        String name = scan.next();
        Distributed d = new Distributed(name);

        //TODO gestion des threads
        System.out.println("Name main thread : " + Thread.currentThread().getName());
        d.initialize(scan);
        d.connexionToMulticast();
        scan.close();
    }

    private void initialize(Scanner scan){
        System.out.println("[" + DISTRIBUTED + name + " ] " + "Multicast IP : ");
        multicastIP = scan.next();
        System.out.println("[" + DISTRIBUTED + name + " ] " + "Multicast port : ");
        while (!scan.hasNextInt()) {
            System.out.println("You have badly written the port, do it again (it has to be an integer)");
            scan.next();
        }
        multicastPort = scan.nextInt();
        System.out.println("[" + DISTRIBUTED + name + " ] " + "Request IP: ");
        requestIP = scan.next();
        System.out.println("[" + DISTRIBUTED + name + " ] " + "Request port : ");
        while (!scan.hasNextInt()) {
            System.out.println("You have badly written the port, do it again (it has to be an integer)");
            scan.next();
        }
        requestPort = scan.nextInt();
    }

    private void connexionToMulticast(){
        System.out.println("JE RENTRE ICI");
        InetAddress groupAddress;
        MulticastSocket socketMulticast;

        //TODO : to change, only a test --> send message when occur a change
        // Open a new DatagramSocket, which will be used to send the data.
        try {
            socketMulticast = new MulticastSocket(multicastPort);
            groupAddress = InetAddress.getByName(multicastIP);

            DatagramSocket serverSocket = new DatagramSocket();
            for (int i = 0; i < 5; i++) {
                String msg = "Sent message no " + i;

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                        msg.getBytes().length, groupAddress, multicastPort);
                serverSocket.send(msgPacket);

                System.out.println("Server sent packet with msg: " + msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //TODO to change, only the client listen in the mutlicast
        /*
        try {
            socketMulticast = new MulticastSocket(multicastPort);
            groupAddress = InetAddress.getByName(multicastIP);

            socketMulticast.joinGroup(groupAddress); //join the multicast group
            // Listening to a message
            while (true) {
                System.out.println("test");
                byte[] buffer = new byte[100];
                DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
                System.out.println(datagram);
                socketMulticast.receive(datagram);
                String message = new String(datagram.getData());
                System.out.println("Message received : "+message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }


    private void TitanPublication(Scanner scan){
        System.out.println("[" + DISTRIBUTED + name + " ] " + "Publish titan");
        System.out.println("[" + DISTRIBUTED + name + " ] " + "Enter a name : ");
        String titanName = scan.next();
        System.out.println("[ " + DISTRIBUTED + name + " ] " + "Select a type : ");
        System.out.println("1.-" + Const.TYPE_TITAN_NORMAL);
        System.out.println("2.-" + Const.TYPE_TITAN_ECCENTRIC);
        System.out.println("3.-" + Const.TYPE_TITAN_INCONSTANT);
        int i = scan.nextInt();
        String type = null;
        switch(i){
            case(1):type=Const.TYPE_TITAN_NORMAL; break;
            case(2):type=Const.TYPE_TITAN_ECCENTRIC; break;
            case(3):type=Const.TYPE_TITAN_INCONSTANT; break;
        }
        //TODO : envoyer un message au serveur central pour demander un id
        Titans newTitan = new Titans(titanName, type, name);
        titansList.add(newTitan);
        System.out.println("[" + DISTRIBUTED + name + " ] " + "A titan has been published : ");
        System.out.println("************");
        System.out.println("ID: " + newTitan.getID());
        System.out.println("Name: " + newTitan.getName());
        System.out.println("Type: " + newTitan.getType());
        System.out.println("************");

    }
}
