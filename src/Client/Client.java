package Client;

import Creature.Titans;
import Util.Const;
import Util.MessageBroker;
import java.io.*;
import java.net.*;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Client {

    private static final String CLIENT = "[CLIENT] ";

    private  String ipServer;
    private  int portServer;
	private  Socket socketCentral;

    private  String ipDistrict;
    private  int portDistrict;
	private  Socket socketDistrict;

	private  int portMulticast;
	private  String IPMulticast;
	private  Socket socketMulti;

    private  ArrayList<Titans> tabDistrictTitans = new ArrayList<Titans>();
    private  ArrayList<Titans> tabCapturedTitans = new ArrayList<Titans>();
    private  ArrayList<Titans> tabKilledTitans = new ArrayList<Titans>();

	//TODO create the 2 threads (thread principal = send thread ; listening thread will be overwrited when changing of district)
	//TODO lists of District Titans : synchronization methods
	//TODO menu : switch between options : write each method

    public static void main(String[] args) throws Exception {
        System.out.println(CLIENT);
        Client c=new Client();
		Scanner scan = new Scanner(System.in);  // Reading from System.in

        c.connectionServer(scan);
		c.connectDistrict(scan);
		c.openMenu(scan);

        scan.close();
		c.socketCentral.close();
		c.socketMulti.close();
		c.socketDistrict.close();
    }


    /**
     * Fonction asking to the Client the informations to the Server
     * @param scan
     */
    private  void connectionServer(Scanner scan) throws Exception {
        System.out.println(CLIENT+"Enter IP Server Central:");
        ipServer = scan.next();
        System.out.println(CLIENT+"Enter Port Server Central:");
        while (!scan.hasNextInt()) {
            System.out.println("You have badly written the port, do it again (it has to be an integer)");
            scan.next();
        }
		portServer = scan.nextInt();
		try{
			socketCentral=new Socket(ipServer,portServer);
			System.out.println("Socket between the client and the central server has been established. They can now communicate.");
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
    }


    /**
     * Function asks the client which district he wants to connect to,
     * and try to connect the client to the district he will enter the name of
     * @param scan
     */
	private  void connectDistrict(Scanner scan) throws Exception {
		System.out.println(CLIENT+"Enter the name of the District you want to Connect to:");
		String districtName = scan.next();
		//TODO check connection
		if(askServerCentral(districtName)) {
			//TODO connect to the multicast of the District
			InetAddress groupIP=InetAddress.getByName(IPMulticast);
			int port= portMulticast;
			Thread threadMessageMulti = new Thread(new Receptor(groupIP, port, districtName, tabDistrictTitans));
			threadMessageMulti.start();
			System.out.println("You are now connected to " + districtName + " !");
			firstConnectionToDistrict();
		    openMenu(scan);
		}else{
		    System.out.println("Authorization refused from the server central.");
		}
	}

	//TODO
	private void disconnectionDistrict (){

	}


	//TODO what is our way to identify a district ?
    /**
     * Function that take the port, ip address of the central server, and name of the wanted district,
     * and ask the connection authorization to the central server. Return true if success, false if fail.
     * @param districtName
     * @return
     */
    private boolean askServerCentral(String districtName) {

        String responseFromServer = null;

        try {
            //TODO update when deployed
            //socketCentral = new Socket(InetAddress.getLocalHost(), portServer); // Attention ne marche que parce que les deux programmes sont sur la même machine!

			MessageBroker mbClient = new MessageBroker();
			mbClient.put(Const.REQ_TYPE, Const.REQ_CHOSE_DISTRICT);
			mbClient.put(Const.REQ_CONTENT, districtName);

            OutputStream outputStream = socketCentral.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(mbClient.toJson());
            System.out.println("Message sent to the server");

            InputStream inputStream = socketCentral.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			responseFromServer = String.valueOf(objectInputStream.readObject());
			System.out.println("Réponse du serveur : "+ responseFromServer);

			MessageBroker mbReceive = new MessageBroker(responseFromServer);
			if (mbReceive.getStringValue(Const.REQ_CONTENT).equals(Const.VALUE_ACCESS_REFUSED)){
				System.out.println(CLIENT+"The access of the district had been refused by the Central");
				return false;
			}
			if (mbReceive.getStringValue(Const.REQ_CONTENT).equals(Const.VALUE_ACCESS_IMPOSSIBLE)){
				System.out.println(CLIENT+"The access of this district is impossible, the district maybe not exists !");
				return false;
			}
			portDistrict = mbReceive.getIntegerValue(Const.KEY_DISTRICT_REQUEST_PORT);
			ipDistrict = mbReceive.getStringValue(Const.KEY_DISTRICT_REQUEST_IP);
			IPMulticast = mbReceive.getStringValue(Const.KEY_DISTRICT_MULTICAST_IP);
			portMulticast=mbReceive.getIntegerValue(Const.KEY_DISTRICT_MULTICAST_PORT);

            inputStream.close();
            objectOutputStream.close();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return(true);
    }

    private void firstConnectionToDistrict(){
		MessageBroker mbRequest = new MessageBroker();
		mbRequest.put(Const.REQ_TYPE, Const.REQ_TITAN_LIST);
		System.out.println("FIRST TEST : "+ tabDistrictTitans);
		if (!requestToDistrict(mbRequest.toJson())){
			System.out.println(CLIENT+"There were a problem, we couldn't synchronize your data with the district data");
		} else {
			System.out.println("TTEEEESST : "+ tabDistrictTitans);
		}
	}



    /**
     * Function displaying the menu and switching between the modes
     * @param scan
     */
    private void openMenu(Scanner scan) throws Exception {

		int choice;
		System.out.println("    -    ");
        System.out.println(CLIENT+"Console");
        System.out.println(CLIENT+"(1) List the Titans");
        System.out.println(CLIENT+"(2) Change District");
        System.out.println(CLIENT+"(3) Capture Titan");
        System.out.println(CLIENT+"(4) Kill Titan");
        System.out.println(CLIENT+"(5) List Captured Titans");
        System.out.println(CLIENT+"(6) List Killed Titans");
		System.out.println("    -    ");

		choice=Integer.parseInt(scan.next());

		switch(choice){
			case 1:
				//list titans
				//TODO create the table of titans (and actualize it)!
				System.out.println("");
				System.out.println("List of the District's Titans :");
				showListTitansFromCurrentDistrict(tabDistrictTitans);
				openMenu(scan);
				break;

			case 2:
				//change District
				//TODO : deconnect from the multicastsocket of the current district
				portDistrict = -1;
				ipDistrict = null;
				//TODO : close the current thread and open a new thread!!
				connectDistrict(scan);
				openMenu(scan);
				break;

			case 3:
				//capture titan
				System.out.println("");
				System.out.println("Enter the ID of the titan you want to capture :");
				System.out.println("CAUTION: the titan has to be normal or inconstant ");
				int wishToCapture=scan.nextInt();
				boolean captured=captureTitan(wishToCapture);
				if(captured){
					System.out.println("The titan " + wishToCapture + " has been successfully captured!");
				}else{
					System.out.println("You failed to capture the titan " + wishToCapture + "...");
				}
				openMenu(scan);
				break;

			case 4:
				//kill titan
				System.out.println("");
				System.out.println("Enter the ID of the titan you want to kill :");
				System.out.println("CAUTION: the titan has to be normal or eccentric ");
				int wishToKill=scan.nextInt();
				boolean killed=killTitan(wishToKill);
				if(killed){
					System.out.println("The titan " + wishToKill + " has been successfully killed!");
				}else{
					System.out.println("You failed to kill the titan " + wishToKill + "...");
				}
				openMenu(scan);
				break;

			case 5:
				//list captured titans
				//TODO create the table of captured titans (and actualize it)!
				System.out.println("");
				System.out.println("List of the captured Titans :");
				showListTitansFromAllDistrict(tabCapturedTitans);
				openMenu(scan);
				break;

			case 6:
				//list killed titans
				//TODO create the table of killed titans (and actualize it)!
				System.out.println("");
				System.out.println("List of the killed Titans :");
				showListTitansFromAllDistrict(tabKilledTitans);
				openMenu(scan);
		
				break;
			default:
				//default
				System.out.println("Please enter a correct number (between 1 and 6)");
				openMenu(scan);
				break;
		}

		
    }


    /**
     * Function void used in the menu, to show a list of titans of the table entered in parameter
     * @param tab a tab of Titans
     */
	private void showListTitansFromCurrentDistrict(ArrayList<Titans> tab){
		System.out.println("-------------------------------");
		for (Titans titan : tab)
			System.out.println(titan.getName() + ", ID : " + titan.getID() + ", type: " + titan.getType());
	}

	/**
	 * Function void used in the menu, to show a list of titans of the table entered in parameter
	 * This function prints the origin district of the Titans
	 * @param tab a tab of Titans
	 */
	private void showListTitansFromAllDistrict(ArrayList<Titans> tab){
		System.out.println("-------------------------------");
		for (Titans titan : tab)
			System.out.println(titan.getName()
					+ ", ID : " + titan.getID()
					+ ", type: " + titan.getType()
					+ ", district: " + titan.getDistrict());
	}

    /**
     * boolean function used in the menu to ask the district (port and ip address in parameter) to capture
     * a titan (id en parameter)
     * @param idTitan id of the Titan you want to capture
     * @return true is the titan is captured, false otherwise
     */
    private boolean captureTitan(int idTitan){
		//TODO send message to the district to ask to capture the titan, and get the response back. return true if success, false if fail.
		//CAUTION: the titan has to be normal or inconstant
		Titans titanCaptured = null;
		Boolean find = false;
		Iterator<Titans> titanIterator = tabDistrictTitans.iterator();
		while(!find && titanIterator.hasNext()){
			if (titanIterator.next().getID().equals(idTitan)) {
				find = true;
				titanCaptured = titanIterator.next();
			}
		}
		if(!find){
			System.out.println(CLIENT+"The titan you want to capture isn't in the list of available titans");
			return false;
		}
		MessageBroker mbRequest = new MessageBroker();
		mbRequest.put(Const.REQ_TYPE, Const.REQ_CAPTURE_TITAN);
		mbRequest.put(Const.REQ_CONTENT, idTitan);
		if (!requestToDistrict(mbRequest.toJson())){
			return false;
		}
		titanCaptured.setStatus(Const.STATE_TITAN_CAPTURED);
		tabCapturedTitans.add(titanCaptured);
		return true;
	}


    /**
     * 	// boolean function used in the menu to ask the district (port and ip address in parameter) to kill a titan
     * 	(id en parameter)
     * @param idTitan the id of the Titan to kill
     * @return true is the titan is killed, false otherwise
     */
	private boolean killTitan(int idTitan){
		//TODO write the code of the function, that send message to the district to ask to kill the titan, and get the response back. return true if success, false if fail.
		//CAUTION: the titan has to be normal or eccentric
		Titans titanKilled = null;
		Boolean find = false;
		Iterator<Titans> titanIterator = tabDistrictTitans.iterator();
		while(!find && titanIterator.hasNext()){
			if (titanIterator.next().getID().equals(idTitan)) {
				find = true;
				titanKilled = titanIterator.next();
			}
		}
		if(!find){
			System.out.println(CLIENT+"The titan you want to capture isn't in the list of available titans");
			return false;
		}
		MessageBroker mbRequest = new MessageBroker();
		mbRequest.put(Const.REQ_TYPE, Const.REQ_KILL_TITAN);
		mbRequest.put(Const.REQ_CONTENT, idTitan);
		if (!requestToDistrict(mbRequest.toJson())){
			return false;
		}
		titanKilled.setStatus(Const.STATE_TITAN_DEAD);
		tabKilledTitans.add(titanKilled);
		return true;
	}


	private Boolean requestToDistrict(String message){
		//TODO: make the UDP
		Boolean requestAccepted = false;
		try{
			final DatagramSocket socket = new DatagramSocket();
			final byte[] receiveData = new byte[1024];
			final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			byte[] sendRequest;
			sendRequest = message.getBytes();
			System.out.println("---- ready to send data");
			InetAddress IPCentralAddress = InetAddress.getByName(ipDistrict);
			final DatagramPacket sendPacket = new DatagramPacket(
					sendRequest, sendRequest.length,IPCentralAddress, portDistrict);
			socket.send(sendPacket);
			socket.setSoTimeout(10000);
			try{
				socket.receive(receivePacket);
				MessageBroker dataReceived = new MessageBroker(new String(receivePacket.getData()));
				if(dataReceived.getStringValue(Const.REQ_CONTENT).equals(Const.VALUE_REQUEST_ACCEPTED)){
					requestAccepted = true;
				}
				if (dataReceived.getStringValue(Const.REQ_TYPE).equals(Const.REQ_TITAN_LIST)){
					//Case of the response of a first connection
					tabDistrictTitans = dataReceived.getListTitansValue(Const.REQ_CONTENT);
					requestAccepted = true;
				}

				//TODO to remove
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

		return requestAccepted;
	}

}

class Receptor extends Thread {
	private InetAddress groupIP;
	private int port;
	private String nom;
	private MulticastSocket socketReception;
	private ArrayList<Titans> tabDistrictTitans;

	Receptor(InetAddress groupIP, int port, String nom, ArrayList<Titans> tabDistrictTitans)  throws Exception {
		this.groupIP = groupIP;
		this.port = port;
		this.nom = nom;
		this.tabDistrictTitans = tabDistrictTitans;
		socketReception = new MulticastSocket(port);
		socketReception.joinGroup(groupIP);
		start();
	}

	public void run() {
		DatagramPacket message;
		byte[] contenuMessage;
		String texte;
		ByteArrayInputStream lecteur;

		while(true) {
			contenuMessage = new byte[1024];
			message = new DatagramPacket(contenuMessage, contenuMessage.length);
			try {
				socketReception.receive(message);
				texte =(String) (new DataInputStream(new ByteArrayInputStream(contenuMessage))).readUTF();
				MessageBroker messageListe = new MessageBroker(texte);
				tabDistrictTitans = messageListe.getListTitansValue(Const.REQ_TITAN_LIST);
				System.out.println(texte);
			}
			catch(Exception exc) {
				exc.printStackTrace();
			}
		}
	}
}

