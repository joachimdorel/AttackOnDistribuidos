package Client;

import Creature.Titans;
import Util.Const;
import Util.MessageBroker;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    private static final String CLIENT = "[CLIENT] ";
    private String ipServer;
    private int portServer;
    private String ipDistrict;
    private int portDistrict;
    //TODO : change structure ?? les tableaux ne sont pas dynamique --> use LinkedList ?!
    private ArrayList<Titans> tabDistrictTitans = new ArrayList<Titans>();
    private ArrayList<Titans> tabCapturedTitans = new ArrayList<Titans>();
    private ArrayList<Titans> tabKilledTitans = new ArrayList<Titans>();

	//TODO create the 2 threads (thread principal = send thread ; listening thread will be overwrited when changing of district)
	//TODO tab of District Titans synchronize methods
	//TODO menu : switch between options : write each method

    public static void main(String[] args) {
        System.out.println(CLIENT);
        Client c=new Client();
		Scanner scan = new Scanner(System.in);  // Reading from System.in
        c.connectionServer(scan);
        scan.close();
    }

    private void connectionServer(Scanner scan){

		//TODO TO REMOVE
		//TEEEEESTT TO REMOVE !!!! Je l'ai laissé pour vous ;)

		/*

		System.out.println("m2  ");
		MessageBroker m2 = new MessageBroker();
		m2.put("teeest0", "coucou");
		String s = m2.toJson(); //utiliser pour envoyer en message
		System.out.println(s);
		m2.put("other", "hihi");
		m2.put("n", 1);
		s = m2.toJson();
		MessageBroker m = new MessageBroker(s); //récupérer le message recut et récupe les attributs
		System.out.println(m.getStringValue("other"));
		System.out.println(m.getIntegerValue("n"));


		System.out.println("------------");

		//test avec titan et list de titan
		MessageBroker m4 = new MessageBroker();
		Titans newTitan = new Titans("Hero", 1, Const.TYPE_TITAN_NORMAL, "TROST");
		Titans newTitan2 = new Titans("Hero2", 2, Const.TYPE_TITAN_NORMAL, "TROST2");
		ArrayList<Titans> listTitan = new ArrayList<Titans>();
		listTitan.add(newTitan);
		listTitan.add(newTitan2);
		m4.put("un titan", (Serializable) newTitan);
		m4.put(Const.REQ_TITAN_LIST, (Serializable) listTitan);
		String s4 = m4.toJson();
		System.out.println(s4);
		MessageBroker m5 = new MessageBroker(s4);
		ArrayList<Titans> list = m5.getListTitansValue(Const.REQ_TITAN_LIST);
		System.out.println("NAME  : " + list.get(1).getName());
		Titans titanTest = m5.getTitansValue("un titan");
		System.out.println("NAME 2 : " + titanTest.getName());


*/


        System.out.println(CLIENT+"Enter IP Server Central:");
        //TODO Remove when it will matter
        System.out.println(CLIENT+"Currently does not matter");
        ipServer = scan.next();
        System.out.println(CLIENT+"Enter Port Server Central:");
        //TODO Remove when it will matter
        System.out.println(CLIENT+"Currently on 2009:");
        while (!scan.hasNextInt()) {
            System.out.println("You have badly written the port, do it again (it has to be an integer)");
            scan.next();
        }
		portServer = scan.nextInt();
		connectDistrict(scan);
    }


	// function asks the client which district he wants to connect to,
    // and try to connect the client to the district he will enter the name of
	private void connectDistrict(Scanner scan){
        System.out.println(CLIENT+"Enter the name of the District you want to Connect to:");
        //TODO Remove when it will matter + print a list of existing District
        System.out.println(CLIENT+"Currently does not matter");
        String districtName = scan.next();

        if(this.askServerCentral(districtName)) {
			System.out.println("You are now connected to " + districtName + " !");
            openMenu(scan);
        }else{
            System.out.println("Authorization refused from the server central.");
        }
	}


	// function that take the port, ip address of the central server, and name of the wanted district,
	// and ask the connection authorization to the central server. Return true if success, false if fail.
    private boolean askServerCentral(String districtName) {
        Socket socket;
        Boolean responseFromServer = null;

        try {
            //TODO update when deployed
            socket = new Socket(InetAddress.getLocalHost(), portServer);
            //socket = new Socket(ipServer, portServer);

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(String.valueOf(districtName));
            System.out.println("Message sent to the server");

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			responseFromServer = objectInputStream.readBoolean();
            if (responseFromServer){
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String messageReceived = bufferedReader.readLine();
				MessageBroker mb = new MessageBroker(messageReceived);
				portDistrict = mb.getIntegerValue("portDistrict");
				ipDistrict = mb.getStringValue("ipDistrict");
				//TODO connect to the multicast of the District
			}

            inputStream.close();
            objectOutputStream.close();
            socket.close();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return(responseFromServer);
    }



    private void openMenu(Scanner scan){
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
				showListTitans(tabDistrictTitans);
				openMenu(scan);
				break;

			case 2:
				//change District
				//TODO : deconnect from the multicastsocket of the current district
				portDistrict = -1;
				ipDistrict = null;
				connectDistrict(scan);
				openMenu(scan);
				break;

			case 3:
				//capture titan
				System.out.println("");
				System.out.println("Enter the ID of the titan you want to capture :");
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
				showListTitans(tabCapturedTitans);
				openMenu(scan);
				break;

			case 6:
				//list killed titans
				//TODO create the table of killed titans (and actualize it)!
				System.out.println("");
				System.out.println("List of the killed Titans :");
				showListTitans(tabKilledTitans);
				openMenu(scan);
		
				break;
			default:
				//default
				System.out.println("Please enter a correct number (between 1 and 6)");
				openMenu(scan);
				break;
		}

		
    }

	//function void used in the menu, to show a list of titans of the table entered in parameter
	private void showListTitans(ArrayList<Titans> tab){
		System.out.println("-------------------------------");
		for (Titans titan : tab)
			System.out.println(titan.getName() + ", ID : " + titan.getID() + ", type: " + titan.getType());
	}

	// boolean function used in the menu to ask the district (port and ip address in parameter) to capture a titan (id en parameter). return true if success, false if fail.
	private boolean captureTitan(int idTitan){
		boolean captured=false;
		//TODO write the code of the function, that send message to the district to ask to capture the titan, and get the response back. return true if success, false if fail. +++ actualize the table from here
		//CAUTION: the titan has to be normal or inconstant
		return captured;
	}

	// boolean function used in the menu to ask the district (port and ip address in parameter) to kill a titan (id en parameter). return true if success, false if fail.
	private boolean killTitan(int idTitan){
		boolean killed=false;
		//TODO write the code of the function, that send message to the district to ask to kill the titan, and get the response back. return true if success, false if fail. +++ actualize the table from here
		//CAUTION: the titan has to be normal or eccentric
		return killed;
	}

}
