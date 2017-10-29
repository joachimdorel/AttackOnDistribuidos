package Server;

import Creature.Titans;
import Util.Const;
import Util.MessageBroker;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TODO when registrate a district : verify that it exists and add it in the list
//TODO update the clients list when a client change his district or when other client appears

public class Central {

    private static final String SERVER_CENTRAL = "[SERVER CENTRAL] ";
    //The group address must be in the range 224.0.0.0 to 239.255.255.255
    private static final String IP_SERVER = "192.168.1.11";
    private static final int PORT_SERVER = 9000;
    private List<District> districts;
    private List<Client> clients;
    private static Integer generator_ID = 1;

    /**
     * Constructor
     */
    public Central(){
        districts= new ArrayList<District>();
    }

    public static void main(String[] args) {
        System.out.println(SERVER_CENTRAL);
        Scanner scan = new Scanner(System.in);  // Reading from System.in
        Central c1 = new Central();
        c1.generateID();
        //Ajout d'un seul district
        //c1.addDistrict();

        //c1.waitAuthorization(scan);
        scan.close();
    }

    /**
     * To add a new district in the Central
     * @param scan
     */
    public void addDistrict(Scanner scan){
        System.out.println(SERVER_CENTRAL+"ADD DISTRICT");
        System.out.println(SERVER_CENTRAL+"District Name:");
        String name = scan.next();
        System.out.println(SERVER_CENTRAL+"Multicast IP:");
        String multicastIp = scan.next();
        System.out.println(SERVER_CENTRAL+"Multicast Port:");
        while (!scan.hasNextInt()) {
            System.out.println("You have badly written the port, do it again (it has to be an integer)");
            scan.next();
        }
        int multicastPort = scan.nextInt();
        System.out.println(SERVER_CENTRAL+"Request IP:");
        String requestIp = scan.next();
        System.out.println(SERVER_CENTRAL+"Request Port:");
        while (!scan.hasNextInt()) {
            System.out.println("You have badly written the port, do it again (it has to be an integer)");
            scan.next();
        }
        int requestPort = scan.nextInt();

        this.districts.add(new District(name, multicastIp, multicastPort, requestIp, requestPort));
    }

    /**
     * Method to authorize a Client to connect to a Distributed Server
     * @param scan
     */
    private void waitAuthorization(Scanner scan){
        ServerSocket serverSocket;
        Socket socket;
        String clientDistrictName = null, authorizationAccorded = null;
        District districtReturned = null;

        try {
            serverSocket = new ServerSocket(PORT_SERVER);
            socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try {
                String clientRequest = String.valueOf(objectInputStream.readObject());
                MessageBroker mbReceive = new MessageBroker(clientRequest);
                if (mbReceive.getStringValue(Const.REQ_TYPE).equals(Const.REQ_CHOSE_DISTRICT)){
                    clientDistrictName = mbReceive.getStringValue(Const.REQ_CONTENT);
                    districtReturned = findDistrict(clientDistrictName);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            if(giveAuthorization(String.valueOf(socket.getRemoteSocketAddress()), clientDistrictName, scan)){
                MessageBroker mbSend = new MessageBroker();
                mbSend.put(Const.REQ_TYPE, Const.REQ_CHOSE_DISTRICT);
                //TODO to add
//                mbSend.put("ipMulticast", );
                objectOutputStream.writeBoolean(true);
                System.out.println(SERVER_CENTRAL+"Response to " + socket.getRemoteSocketAddress() + " to "+ clientDistrictName);
                System.out.println(SERVER_CENTRAL+clientDistrictName);
            }else{
                objectOutputStream.writeBoolean(false);
            }
            objectOutputStream.close();

            serverSocket.close();
            socket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private District findDistrict(String name){
        for(District d: districts){
            if(d.getName().equals(name)){
                return d;
            }
        }
        System.out.println("The district does not exist");
        return null;
    }

    private Boolean giveAuthorization(String ipClient, String ClientDistrict, Scanner scan) {
        System.out.println(SERVER_CENTRAL + "Give authorization to " + ipClient + " for the district " + ClientDistrict);
        System.out.println("1. - YES");
        System.out.println("2. - NO");
        return scan.next().equals("1");
    }


    /*
    * Generator of unique ID
    * Socket which receive id generating request, send an unique ID in response
    * */
    //TODO : make a specific thread to this function : has to be always listening
    private void generateID (){
        try {
            //TODO change in the virtual machine
//            InetAddress IPServer = InetAddress.getByName(IP_SERVER); --> not works
            InetAddress IPServer = InetAddress.getLocalHost(); //works
            ;

            final DatagramSocket serverSocket = new DatagramSocket(PORT_SERVER, IPServer);
            byte[] receiveData;
            byte[] sendData;
            System.out.println("Local address : " + serverSocket.getLocalAddress());
            while (true) {
                receiveData = new byte[100];
                final DatagramPacket receivePacket = new DatagramPacket(receiveData,
                        receiveData.length);
                System.out.println("Waiting for datagram packet");
                serverSocket.receive(receivePacket);
                MessageBroker message = new MessageBroker(new String(receivePacket.getData()));
                final InetAddress IPAddress = receivePacket.getAddress();
                final int port = receivePacket.getPort();

                System.out.println("From: " + IPAddress + ":" + port);
                System.out.println("Message: " + message.toJson());

                if(message.getStringValue(Const.REQ_TYPE).equals(Const.REQ_NEW_ID)){
                    message.put(Const.REQ_CONTENT, generator_ID++);
                }
                sendData = message.toJson().getBytes();
                final DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }

        } catch (final SocketException ex) {
            ex.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
