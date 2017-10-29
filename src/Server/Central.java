package Server;

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
    private static final String IP_SERVER = "192.168.1.11";
    private static final int PORT_SERVER = 9000;
    private List<District> districts;
    private List<Client> clients;
    private static Integer generator_ID = 1;

    public Central(){
        districts= new ArrayList<District>();
    }

    public static void main(String[] args) {
        System.out.println(SERVER_CENTRAL);
        Scanner scan = new Scanner(System.in);  // Reading from System.in
        Central c1 = new Central();
        c1.generateID();
        //Ajout d'un seul district
        //c1.agregarDistrict();

        //c1.waitAuthorization(scan);
        scan.close();
    }

    public void addDistrict(Scanner scan){
        System.out.println("ADD DISTRICT");
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

    private void waitAuthorization(Scanner scan){
        ServerSocket serverSocket;
        Socket socket;
        String ClientDistrict = null, authorizationAccorded = null;
        District districtReturned = null;

        try {
            serverSocket = new ServerSocket(PORT_SERVER);
            socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try {
                ClientDistrict = String.valueOf(objectInputStream.readObject());
                //TODO Search the district corresponding in List<District> districts
                //districtReturned = district.getDistrictByName ? ---> The function is not coded yet...
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            if(giveAuthorization(String.valueOf(socket.getRemoteSocketAddress()), ClientDistrict, scan)){
                objectOutputStream.writeBoolean(true);
                System.out.println(SERVER_CENTRAL+"Response to " + socket.getRemoteSocketAddress() + " to "+ ClientDistrict);
                System.out.println(SERVER_CENTRAL+ClientDistrict);
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

    private Boolean giveAuthorization(String ipClient, String ClientDistrict, Scanner scan) {
        System.out.println(SERVER_CENTRAL + "Give authorization to " + ipClient + " for the district " + ClientDistrict);
        System.out.println("1. - YES");
        System.out.println("2. - NO");

        return (scan.next().equals("1")) ? true : false;
    }



        /*

        private static int port=7000;
        //The group address must be in the range 224.0.0.0 to 239.255.255.255
        static String group = new String("226.1.0.2");

        System.out.println("Hello Server!");


        //Multicast
        InetAddress groupAddress=null;
        MulticastSocket socketMulticast=null;

        try {
            socketMulticast = new MulticastSocket(port);
            groupAddress = InetAddress.getByName(group);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String message="Message from central.";
        byte[] buf = message.getBytes();
        DatagramPacket dg = new DatagramPacket(buf, buf.length, groupAddress, port);
        try {
            socketMulticast.send(dg); // envia datagrama al grupo
        } catch (IOException ex) {
            System.out.println(ex);
        }
        */

    /*
    * Generator of unique ID
    * Socket which receive id generating request, send an unique ID in response */
    //TODO : make a specific thread to this function : has to be always listening
    //TODO : specific port and ip ??
    private void generateID (){
        try {
            //TODO chose a method, make it works
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
            //TODO change this sentence
            ex.printStackTrace();
            System.exit(1);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
