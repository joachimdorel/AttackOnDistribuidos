package Client;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final String CLIENT = "[CLIENT] ";

    public Client() {
    }
	//TODO create the 2 threads (thread principal = send thread ; listening thread will be overwrited when changing of district)
	//TODO menu : switch between options : write each method

    public static void main(String[] args) {
        System.out.println(CLIENT);
        Client c=new Client();
        c.connectionServer();
    }

    private void connectionServer(){
        Scanner scan = new Scanner(System.in);  // Reading from System.in

        System.out.println(CLIENT+"Enter IP Server Central:");
        //TODO Remove when it will matter
        System.out.println(CLIENT+"Currently does not matter");
        String ipServer = scan.next();
        System.out.println(CLIENT+"Enter Port Server Central:");
        //TODO Remove when it will matter
        System.out.println(CLIENT+"Currently on 2009:");
        while (!scan.hasNextInt()) {
            System.out.println("You have badly written the port, do it again (it has to be an integer)");
            scan.next();
        }
		Integer portServer = scan.nextInt();
        scan.close();
		connectDistrict(portServer,ipServer);

    }


	// function that take the central server port and ip address, ask the client wich district he wants to connect to, and try to connect the client to the district he will enter the name of
	public void connectDistrict(int portServer, String ipServer){
		Scanner scan = new Scanner(System.in);
        System.out.println(CLIENT+"Enter the name of the District you want to Connect to:");
        //TODO Remove when it will matter
        System.out.println(CLIENT+"Currently does not matter");
        String district = scan.next();

        scan.close();

        if(this.askServerCentral(portServer, ipServer, district)) {
			System.out.println("You are now connected to " + district + " !");
            openMenu();
        }else{
            System.out.println("Authorization refused from the server central.");
        }
	}


	// function that take the port, ip address of the central server, and name of the wanted district, and ask the connection authorization to the central server. Return true if success, false if fail.
    public boolean askServerCentral(int port, String host, String district) {
        Socket socket;
        Boolean responseFromServer = null;

        try {
            //TODO update when deployed
            socket = new Socket(InetAddress.getLocalHost(), port);
            //socket = new Socket(host, port);

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(String.valueOf(district));

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            responseFromServer = objectInputStream.readBoolean();

            inputStream.close();
            objectOutputStream.close();
            socket.close();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return(responseFromServer);
		//TODO return portDistrict and ipDistrict of district server so the client can connect to it (in a string? and cast it to get the int and string?) !!!!!!!!!!! en fait mieux si retourne le socket avec le district non? comme ça pas besoin de le faire tout le tmeps!! à changer dans les fonctions prenant en paramètre le port et ip du districte!!!!
    }



    public void openMenu(int portServer, String ipServer, int portDistrict, String ipDistrict){
		Scanner scan = new Scanner(System.in);  // Reading from System.in what the user want to do
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
        scan.close();

		swich(choice){
			case 1:
				//list titans
				//TODO create the table of titans (and actualize it)!
				System.out.println("");
				System.out.println("List of the District's Titans :");
				showListTitans(tabTitans);
				openMenu(portServer, ipServer, portDistrict, ipDistrict);
				break;

			case 2:
				//change District
				connectDistrict(portServer,ipServer);
				//TODO get the port and ip of district from connectDistrict so that function can work!
				openMenu(portServer, ipServer, newportDistrict, newipDistrict);
				break;

			case 3:
				//capture titan
				Scanner scan = new Scanner(System.in);
				System.out.println("");
				System.out.println("Enter the ID of the titan you want to capture :");
				int wishToCapture=scan.next(); //type problem???
       			scan.close();
				if(boolean captured=captureTitan(wishToCapture, portDistrict, ipDistrict)){
					System.out.println("The titan " + wishToCapture + " has been succesfuly captured!");
				}else{
					System.out.println("You failed to capture the titan " + wishToCapture + "...");
				}
				openMenu(portServer, ipServer, portDistrict, ipDistrict);
				break;

			case 4:
				//kill titan
				Scanner scan = new Scanner(System.in);  
				System.out.println("");
				System.out.println("Enter the ID of the titan you want to kill :");
				int wishToKill=scan.next(); //type problem???
        		scan.close();
				if(boolean killed=killTitan(wishToKill, portDistrict, ipDistrict)){
					System.out.println("The titan " + wishToKill + " has been succesfuly killed!");
				}else{
					System.out.println("You failed to kill the titan " + wishToKill + "...");
				}
				openMenu(portServer, ipServer, portDistrict, ipDistrict);
				break;

			case 5:
				//list captured titans
				//TODO create the table of captured titans (and actualize it)!
				System.out.println("");
				System.out.println("List of the captured Titans :");
				showListTitans(tabCapturedTitans);
				openMenu(portServer, ipServer, portDistrict, ipDistrict);
				break;

			case 6:
				//list killed titans
				//TODO create the table of killed titans (and actualize it)!
				System.out.println("");
				System.out.println("List of the killed Titans :");
				showListTitans(tabKilledTitans);
				openMenu(portServer, ipServer, portDistrict, ipDistrict);
		
				break;
			default:
				//default
				System.out.println("Please enter a correct number (between 1 and 6)");
				openMenu(portServer, ipServer, portDistrict, ipDistrict);
				break;
		}

		
    }

	//function void used in the menu, to show a list of titans of the table entered in parameter
	public void showListTitans(Titans[] tab){
		int i;
		System.out.println("-------------------------------");
		for(int i=0;i<tab.length;i++)
			System.out.println(tab[i].name + ", ID : "+ tab[i].id + ", type: "+tab[i].type);
	}

	// boolean function used in the menu to ask the district (port and ip address in parameter) to capture a titan (id en parameter). return true if success, false if fail.
	public boolean captureTitan(int numTitan, int portDistrict, String ipDistrict){
		boolean captured=false;
		//TODO write the code of the function, that send message to the district to ask to capture the titan, and get the response back. return true if success, false if fail. +++ actualize the table from here

		return captured;
	}

	// boolean function used in the menu to ask the district (port and ip address in parameter) to kill a titan (id en parameter). return true if success, false if fail.
	public boolean killTitan(int numTitan, int portDistrict, String ipDistrict){
		boolean killed=false;
		//TODO write the code of the function, that send message to the district to ask to kill the titan, and get the response back. return true if success, false if fail. +++ actualize the table from here

		return killed;
	}


}
