package Distributed;

import Creature.Titans;
import Util.Const;
import Util.ID_generator;

import java.io.IOException;
import java.net.DatagramPacket;
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


public class Distributed {
    private static final String DISTRIBUTED = " DISTRICT ";
    private String name = "";
    private ArrayList<Titans> titansList = new ArrayList<Titans>();
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

    private Distributed(String name){
        String port;
        String groupAddress;
        this.name = name;
    }


    public static void main(String[] args) {
        System.out.println("Hello Distributed!");
        Scanner scan = new Scanner(System.in);  // Reading from System.in

        InetAddress groupAddress;
        MulticastSocket socketMulticast;

            //TODO : initialization du district
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
        scan.close();
    }


    private void selectPortAddress(Scanner scan){
        System.out.println("Select a port : ");
        String port = scan.next();

        System.out.println("Select a group address : ");
        String address = scan.next();

    }

        //TODO call this function only in a specific district
    private void TitanPublication(Scanner scan){
        System.out.println("[ " + DISTRIBUTED + name + "] " + "Publish titan : ");
        System.out.println("[ " + DISTRIBUTED + name + "] " + "Enter a name : ");
        String titanName = scan.next();
        System.out.println("[ " + DISTRIBUTED + name + "] " + "Select a type : ");
        System.out.println("1.-" + Const.TYPE_TITAN_NORMAL);
        System.out.println("2.-" + Const.TYPE_TITAN_ECCENTRIC);
        System.out.println("3.-" + Const.TYPE_TITAN_INCONSTANT);
        int i = scan.nextInt();
        String type = null;
        switch(i){
            case(1):type=Const.TYPE_TITAN_NORMAL;
            case(2):type=Const.TYPE_TITAN_ECCENTRIC;
            case(3):type=Const.TYPE_TITAN_INCONSTANT;
        }
        //TODO : envoyer un message au serveur central pour demander un id
        ID_generator id_g = new ID_generator();
        Titans newTitan = new Titans(titanName, id_g.newID(), type, name);
        titansList.add(newTitan);
        System.out.println("[ " + DISTRIBUTED + name + "] " + "A titan has been published : ");
        System.out.println("************");
        System.out.println("ID: " + newTitan.getID());
        System.out.println("Name: " + newTitan.getName());
        System.out.println("Type: " + newTitan.getType());
        System.out.println("************");

    }
}
