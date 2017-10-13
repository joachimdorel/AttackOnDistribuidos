package Central;

import java.io.IOException;
import java.net.*;

public class MainCentral {


    private static int port=7000;
    //The group address must be in the range 224.0.0.0 to 239.255.255.255
    static String group = new String("226.1.0.2");

    public static void main(String[] args) {
        System.out.println("Hello Central!");


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

        /*
        ServerSocket socketserver  ;
        Socket socketduserveur ;

        try {
            socketserver = new ServerSocket(2009);
            socketduserveur = socketserver.accept();
            System.out.println("Server Central - Connection Opened");
            socketserver.close();
            socketduserveur.close();
            System.out.println("Server Central- Connection Closed");
        }catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
