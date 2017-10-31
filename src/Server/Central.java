package Server;

import Util.Const;
import Util.MessageBroker;

import java.io.*;
import java.net.*;
import java.util.*;

import static Server.Central.scanGlobal;

//TODO when registrate a district : verify that it exists and add it in the list
//TODO update the clients list when a client change his district or when other client appears

public class Central {

    private static final String SERVER_CENTRAL = "[SERVER CENTRAL] ";
    //TODO rentrer en dynamique par dans la console ??
    private static final String IP_SERVER = "192.168.1.11";
    private static final int PORT_SERVER = 9000;
    private ArrayList<District> districts;
    private ArrayList<Client> clients;

    /**
     * Constructor
     */
    public Central(){
        districts= new ArrayList<District>();
        clients= new ArrayList<Client>();
    }

    public static void main(String[] args){
        System.out.println(SERVER_CENTRAL);
        Central c1 = new Central();

        Thread connectionClient = new Thread(new AcceptClient(c1.districts, c1.clients));
        connectionClient.start();

        System.out.println("Thread connectionClient launched!");

        Thread generatorID = new Thread(new GeneratorID());
        generatorID.start();
        System.out.println("Thread generatorID launched!");

        c1.openMenu();

        scanGlobal("close", null);
    }


    public static String scanGlobal(String threadName, ArrayList<District> districtsScan){
        Scanner scan = new Scanner(System.in);  // Reading from System.in
        int returnedValue = 0;

        if(threadName.equals("giveAuthorization")) {
            System.out.println("1. - YES");
            System.out.println("2. - NO");
            returnedValue = Integer.parseInt(scan.next());
        }else if(threadName.equals("openMenu")) {
            returnedValue = Integer.parseInt(scan.next());
        }else if(threadName.equals("addDistrict")) {
            System.out.println("Add district called.");
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
            if(!doesDistrictExists(name, districtsScan)) {
                districtsScan.add(new District(name, multicastIp, multicastPort, requestIp, requestPort));
            }
        }else if(threadName.equals("closeScan")) {
            scan.close();
        }

        return String.valueOf(returnedValue);
    }

    /**
     * Function to know is a district exist yet
     * @param name
     * @param districts
     * @return
     */
    private static Boolean doesDistrictExists(String name, ArrayList<District> districts){
        for(District d: districts){
            if(d.getName().equals(name)){
                System.out.println("The district "+name+" already exists ! ");
                return true;
            }
        }
        return false;
    }

    /**
     * Function displaying the menu and switching between the modes
     */
    private void openMenu() {
        System.out.println("    -    ");
        System.out.println(SERVER_CENTRAL + "Console");
        System.out.println(SERVER_CENTRAL + "(1) List of the Districts");
        System.out.println(SERVER_CENTRAL + "(2) List of the Clients");
        System.out.println(SERVER_CENTRAL + "(3) Add a district");
        System.out.println("    -    ");

        int choice = Integer.parseInt(scanGlobal("openMenu", null));
        switch (choice) {
            case 1:
                //list districts
                System.out.println("-------------------------------");
                for (District d : districts)
                    System.out.println(d.toString());
                openMenu();
                break;
            case 2:
                //list clients
                System.out.println("-------------------------------");
                for (Client c : clients)
                    System.out.println(c.toString());
                openMenu();
                break;
            case 3:
                //add a district
                scanGlobal("addDistrict", districts);
                openMenu();
            default:
                //default
                System.out.println("Please enter a correct number (between 1 and 3)");
                openMenu();
                break;
        }
    }

    /**
     * To add a new district in the Central
     * @param scan scanner can't be reopen
     */
    private void addDistrict(Scanner scan){
        System.out.println(SERVER_CENTRAL+"ADD DISTRICT");
        System.out.println(SERVER_CENTRAL+"District Name:");
        String name = scan.next();
        System.out.println(SERVER_CENTRAL+"Multicast IP:");
        System.out.println("CAUTION : The multicast ip must be in the range 224.0.0.0 to 239.255.255.255");
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
        if(!doesDistrictExists(name)) {
            this.districts.add(new District(name, multicastIp, multicastPort, requestIp, requestPort));
        }
    }

    private Boolean doesDistrictExists(String name){
        for(District d: districts){
            if(d.getName().equals(name)){
                System.out.println("The district "+name+" already exists ! ");
                return true;
            }
        }
        return false;
    }
}

//-------------------------------------------------------------------------------------
//----------------------ACCEPT CLIENT CONNECT TO DISTRICT THREAD-----------------------
//-------------------------------------------------------------------------------------

class AcceptClient extends Thread {
    private static final String SERVER_CENTRAL = "[SERVER CENTRAL] ";
    private List<District> districts;
    private List<Client> clients;
    private static final String IP_SERVER = "192.168.1.11";
    private static final int PORT_SERVER = 9000;

    public AcceptClient(ArrayList<District> districts, ArrayList<Client> clients){
        this.districts = districts;
        this.clients = clients;
    }

    public void run() {
        try{
            while(true){
                 waitAuthorization();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to authorize a Client to connect to a Distributed Server
     * @param
     */
    private void waitAuthorization(){
        ServerSocket serverSocket;
        Socket socket;
        String clientDistrictName = null;
        District districtReturned = null;
        System.out.println("Into waitAuthorization Function");


        try {
            serverSocket = new ServerSocket(PORT_SERVER);
            socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            try {
                String clientRequest = String.valueOf(objectInputStream.readObject());
                MessageBroker mbReceive = new MessageBroker(clientRequest);
                if (mbReceive.getStringValue(Const.REQ_TYPE).equals(Const.REQ_CHOSE_DISTRICT)){
                    System.out.println(SERVER_CENTRAL + "A client made a request to connect to a district");
                    clientDistrictName = mbReceive.getStringValue(Const.REQ_CONTENT);
                    districtReturned = findDistrict(clientDistrictName);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            MessageBroker mbSend = new MessageBroker();
            mbSend.put(Const.REQ_TYPE, Const.REQ_CHOSE_DISTRICT);
            if(districtReturned!=null){
                if(giveAuthorization(String.valueOf(socket.getRemoteSocketAddress()), clientDistrictName)){
                    mbSend.put(Const.REQ_CONTENT, Const.VALUE_ACCESS_ACCEPTED);
                    mbSend.put(Const.KEY_DISTRICT_REQUEST_IP, districtReturned.getRequestIP());
                    mbSend.put(Const.KEY_DISTRICT_REQUEST_PORT, districtReturned.getRequestPort());
                    mbSend.put(Const.KEY_DISTRICT_MULTICAST_IP, districtReturned.getMulticastIP());
                    mbSend.put(Const.KEY_DISTRICT_MULTICAST_PORT, districtReturned.getMulticastPort());
                    updateClientList(socket.getRemoteSocketAddress().toString(), clientDistrictName);
                }else{
                    mbSend.put(Const.REQ_CONTENT, Const.VALUE_ACCESS_REFUSED);
                }
            } else {
                mbSend.put(Const.REQ_CONTENT, Const.VALUE_ACCESS_IMPOSSIBLE);
            }
            objectOutputStream.writeObject(mbSend.toJson());
            System.out.println(SERVER_CENTRAL+"Response to " + socket.getRemoteSocketAddress() + " to "+ clientDistrictName);
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

    private Boolean giveAuthorization(String ipClient, String ClientDistrict) {
        Thread.interrupted();
        System.out.println(SERVER_CENTRAL + "Give authorization to " + ipClient + " for the district " + ClientDistrict);

        //Displaying the menu
        System.out.println("    -    ");
        System.out.println(SERVER_CENTRAL + "Console");
        System.out.println(SERVER_CENTRAL + "(1) List of the Districts");
        System.out.println(SERVER_CENTRAL + "(2) List of the Clients");
        System.out.println(SERVER_CENTRAL + "(3) Add a district");
        System.out.println("    -    ");

        return scanGlobal("giveAuthorization", null).equals("1");
    }

    private void updateClientList(String clientName, String districtConnectedTo){
        Boolean update = false;
        Iterator<Client> clientIterator = clients.iterator();
        while(!update && clientIterator.hasNext()){
            if (clientIterator.next().getName().equals(clientName)) {
                update = true;
                clientIterator.next().setConnectedToDistrict(districtConnectedTo);
            }
        }
        if(!update){
            clients.add(new Client(clientName, districtConnectedTo));
        }
    }
}

//-------------------------------------------------------------------------------------
//------------------------------GENERATOR ID THREAD------------------------------------
//-------------------------------------------------------------------------------------

class GeneratorID extends Thread {
    private static Integer generator_ID = 1;
    private final int PORT_SERVER = 9000;
    //private final String IP_SERVER = "192.168.1.30"; //TODO to remove

    public void run() {
        try{
            while(true){
                generateID();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
    * Generator of unique ID
    * Socket which receive id generating request, send an unique ID in response
    * */
    private void generateID (){
        try {
            //TODO change in the virtual machine
//            InetAddress IPServer = InetAddress.getByName(IP_SERVER); // --> not works
            InetAddress IPServer = InetAddress.getLocalHost(); //works by in a thread create a new IP

            final DatagramSocket serverSocket = new DatagramSocket(PORT_SERVER, IPServer);
            byte[] receiveData;
            byte[] sendData;
            //TODO to remove
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

                //TODO to remove
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

