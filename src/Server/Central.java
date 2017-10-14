package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Central {

    private static final String SERVER_CENTRAL = "[SERVER CENTRAL] ";
    private static final int PORT_SERVER = 2009;
    private List<Distrito> distritos;

    public Central(){
        distritos= new ArrayList<Distrito>();
    }

    public static void main(String[] args) {
        Central c1 = new Central();

        //Ajout d'un seul distrito
        //c1.agregarDistrito();

        c1.waitAuthorization();
    }

    public void agregarDistrito(){
        Scanner scan = new Scanner(System.in);  // Reading from System.in

        System.out.println("AGREGAR DISTRITO");
        System.out.println(SERVER_CENTRAL+"Nombre Distrito:");
        String nombre = scan.next();
        System.out.println(SERVER_CENTRAL+"IP Multicast:");
        String ipMulticast = scan.next();
        System.out.println(SERVER_CENTRAL+"Puerto Multicast:");
        String puertoMulticast = scan.next();
        System.out.println(SERVER_CENTRAL+"IP Peticiones:");
        String ipPeticiones = scan.next();
        System.out.println(SERVER_CENTRAL+"Puerto Peticiones:");
        String puertoPeticiones = scan.next();

        scan.close();

        this.distritos.add(new Distrito(nombre, ipMulticast, puertoMulticast, ipPeticiones, puertoPeticiones));
    }

    public void waitAuthorization(){
        ServerSocket serverSocket;
        Socket socket;
        String distritoClient = null;

        try {
            serverSocket = new ServerSocket(PORT_SERVER);
            socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try {
                distritoClient = String.valueOf(objectInputStream.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            giveAuthorization(String.valueOf(socket.getRemoteSocketAddress()), distritoClient);

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject("Client Received");
            objectOutputStream.close();

            serverSocket.close();
            socket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean giveAuthorization(String ipClient, String distritoClient) {
        System.out.println(SERVER_CENTRAL + "Give authorization to " + ipClient + " for the district " + distritoClient);

        return true;
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
