package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TODO when registrate a district : verify that it exists and add it in the list

public class Central {

    private static final String SERVER_CENTRAL = "[SERVER CENTRAL] ";
    private static final int PORT_SERVER = 2009;
    private List<District> districts;

    public Central(){
        districts= new ArrayList<District>();
    }

    public static void main(String[] args) {
        System.out.println(SERVER_CENTRAL);
        Central c1 = new Central();

        //Ajout d'un seul district
        //c1.agregarDistrict();

        c1.waitAuthorization();
    }

    public void agregarDistrict(){
        Scanner scan = new Scanner(System.in);  // Reading from System.in

        System.out.println("ADD DISTRICT");
        System.out.println(SERVER_CENTRAL+"District Name:");
        String name = scan.next();
        System.out.println(SERVER_CENTRAL+"Multicast IP:");
        String multicastIp = scan.next();
        System.out.println(SERVER_CENTRAL+"Multicast Port:");
        String multicastPort = scan.next();
        System.out.println(SERVER_CENTRAL+"Request IP:");
        String requestIp = scan.next();
        System.out.println(SERVER_CENTRAL+"Request Port:");
        String requestPort = scan.next();

        scan.close();

        this.districts.add(new District(name, multicastIp, multicastPort, requestIp, requestPort));
    }

    public void waitAuthorization(){
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
            if(giveAuthorization(String.valueOf(socket.getRemoteSocketAddress()), ClientDistrict)){
                objectOutputStream.writeBoolean(true);
                System.out.println(SERVER_CENTRAL+"Response to " + socket.getRemoteSocketAddress() + " to "+ ClientDistrict);
                System.out.println(SERVER_CENTRAL+ClientDistrict.toString());
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

    private Boolean giveAuthorization(String ipClient, String ClientDistrict) {
        Scanner scan = new Scanner(System.in);  // Reading from System.in

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
}
