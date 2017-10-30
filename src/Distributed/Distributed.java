package Distributed;

import Creature.*;
import Util.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.*;


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
	private InetAddress groupAddress; //InetAddress du multicast
	private MulticastSocket socketMulticast;
    private String requestIP;
    private int requestPort;
    private String centralServerIP;
    private int centralServerPort;
    private ArrayList<Titans> titansList = new ArrayList<Titans>();


    private Distributed(String name){
        this.name = name;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello Distributed!");
        Scanner scan = new Scanner(System.in);  // Reading from System.in
        System.out.println("[" + DISTRIBUTED + "] " + "Server name : ");
        String name = scan.next();
        Distributed d = new Distributed(name);

        //TODO gestion des threads

        System.out.println("Name main thread : " + Thread.currentThread().getName());
        d.initialize(scan);
        //d.connectionToMulticast();
        d.openMenu(scan);
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
        //We ask the central server data to can connect to it and ask him a unique id
        System.out.println("[" + DISTRIBUTED + name + " ] " + "Central server IP: ");
        centralServerIP = scan.next();
        System.out.println("[" + DISTRIBUTED + name + " ] " + "Central server port : ");
        while (!scan.hasNextInt()) {
            System.out.println("You have badly written the port, do it again (it has to be an integer)");
            scan.next();
        }
        centralServerPort = scan.nextInt();
    }


    //TODO : send a mensage to the multicast each time there is a modification
    private void connectionToMulticast(){
        InetAddress groupAddress;
        MulticastSocket socketMulticast;
        //TODO : to change, only a test --> send message when occur a change
        // Open a new DatagramSocket, which will be used to send the data.
        try {
            socketMulticast = new MulticastSocket(multicastPort);
            groupAddress = InetAddress.getByName(multicastIP);
			socketMulticast.setTimeToLive(15);

            DatagramSocket serverSocket = new DatagramSocket();
            for (int i = 0; i < 5; i++) {
                String msg = "Sent message no " + i;

                // TODO Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                msg.getBytes().length, groupAddress, multicastPort);
                serverSocket.send(msgPacket);

                System.out.println("Server sent packet with msg: " + msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Function displaying the menu and switching between the modes
     * @param scan scanner can't be reopen
     */
    private void openMenu(Scanner scan) throws IOException {

        int choice;
        System.out.println("    -    ");
        System.out.println("[" + DISTRIBUTED + name + " ] " + "Console");
        System.out.println("[" + DISTRIBUTED + name + " ] " + "(1) List of the Titans");
        System.out.println("[" + DISTRIBUTED + name + " ] " + "(2) Add a Titan");
        System.out.println("    -    ");
        choice = Integer.parseInt(scan.next());
        switch (choice) {
            case 1:
                //list titans
                System.out.println("-------------------------------");
                for (Titans t : titansList)
                    System.out.println(t.getName() + ", ID : " + t.getID() + ", type: " + t.getType());
                openMenu(scan);
                break;
            case 2:
                //add a titan
                titanPublication(scan);
                openMenu(scan);
            default:
                //default
                System.out.println("Please enter a correct number (between 1 and 2)");
                openMenu(scan);
                break;
        }
    }


    private void titanPublication(Scanner scan) throws IOException {
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
        int id = requestID();
        if(id == -1){
            System.out.println("[" + DISTRIBUTED + name + " ] " + "There were a problem : impossible to get an id for the new titan");
        } else {
            Titans newTitan = new Titans(titanName, type, id, name);
            titansList.add(newTitan);
            System.out.println("[" + DISTRIBUTED + name + " ] " + "A titan has been published : ");
            System.out.println("************");
            System.out.println("ID: " + newTitan.getID());
            System.out.println("Name: " + newTitan.getName());
            System.out.println("Type: " + newTitan.getType());
            System.out.println("************");

            //TODO remove the comment
            //sendTitansListMulticast();
        }
    }


    //TODO : think about this function
    private int requestID(){
        int newId = -1;
        MessageBroker mb = new MessageBroker();
        mb.put(Const.REQ_TYPE,Const.REQ_NEW_ID);
        String request = mb.toJson();
        try{
            final DatagramSocket socket = new DatagramSocket();
            final byte[] receiveData = new byte[100];
            final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            byte[] sendRequest;
            sendRequest = request.getBytes();
            System.out.println("---- ready to send data");
            InetAddress IPCentralAddress = InetAddress.getByName(centralServerIP);
            final DatagramPacket sendPacket = new DatagramPacket(
                    sendRequest, sendRequest.length,IPCentralAddress, centralServerPort);
            socket.send(sendPacket);
            socket.setSoTimeout(10000);
            try{
                socket.receive(receivePacket);
                final MessageBroker dataReceived = new MessageBroker(new String(receivePacket.getData()));
                newId = dataReceived.getIntegerValue(Const.REQ_CONTENT);
                final InetAddress returnIPAddress = receivePacket.getAddress();
                final int port = receivePacket.getPort();
                System.out.println("From server at: " + returnIPAddress + ":"
                        + port);
                System.out.println("Message: " + dataReceived.toJson());
            } catch (final SocketTimeoutException ste){
                System.out.println("Timeout Occurrend : Packet assumed lost");
            }
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newId;
    }

	private void sendTitansListMulticast() throws IOException {
			byte[] contenuMessage;
			DatagramPacket message;

			MessageBroker listToSend = new MessageBroker();
			String stringToSend;
			listToSend.put(Const.REQ_TITAN_LIST, (Serializable) titansList);
			stringToSend=listToSend.toJson();
			ByteArrayOutputStream sortie = 	new ByteArrayOutputStream(); 

			(new DataOutputStream(sortie)).writeUTF(stringToSend); 
			contenuMessage = sortie.toByteArray();
			message = new DatagramPacket(contenuMessage, contenuMessage.length, groupAddress, multicastPort);
			socketMulticast.send(message);
	  
		
	}
	//TODO
	/*private void captureRequest(int id){
		
	}


	private void killRequest(int id){
		
	}*/
	

}
