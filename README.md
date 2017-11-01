**Attack on Distribuidos**

Project as part of the course of Distribuited Systems at the "Universidad Tecnica Federico Santa Maria", based in Valparaiso, Chile.



-------------------------------------------------
---------------Table of Contents :--------------- 
-------------------------------------------------
** How to launch the project
** Usage - How to initialize and run each entity
(In this README, we will also explain the difficulties we encountered, and what's not working)
-------------------------------------------------
-------------------------------------------------


/!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ 

ATTENTION : by testing on the virtual machines, we realized that the function we were using to map the messages (ObjectMapping of JSON Jackson function) was using Java 8. OR, on the virtual machines is only working Java 7! Therefore, when we try to send a message on the departement virtual machines, the programs are failing (error : Unsupported major.minor version 52.0).... We tried without success to find the 51's version, supported by Java 7, and didn't have enough time to think about a solution without this JSON mapping...
But these functions are working perfectly on our computers with Java 8

/!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ /!\ 




-------------------------------------------------
---------*How to launch the project*-------------
-------------------------------------------------

---- Case of the server : <br />
Go to the virtual machine grupo-18@10.10.2.183

    javac -d . **/*.java
    java Server.Central


---- Case of the client : <br />
Connect to the virtual machine grupo-18@10.10.2.104

    javac -d . **/*.java
    java Client.Client
    

---- Case of the district server : <br />
Connect to the virtual machine grupo-18@10.10.2.79
    
    javac -d . **/*.java
    java Distributed.Distributed



-------------------------------------------------    
-----*Usage -How to initialize each entity*------
-------------------------------------------------

---- Case of the server: <br />
A menu allows you to chose the action you want to do. You have to register new districts manually. <br />

CAUTION : The scanner in the console works with a fifo (first in first out) logic. You have to answer to all  previous questions to be able to answer the actual one.



---- Case of the client: <br />
You first have to connect to the central server. To do that, you have to enter the IP address of the server (you can see it printed in the central server console : 10.10.2.183). The central server port is 4600. <br />
You must then chose a district to connect to, by selecting it by his name (we suppose that the client know the names of the different districts). This district has to be previously created and registered by the central server.
You can then chose other action to do with the menu.

CHANGE DISTRICT PROBLEM : The 2nd choice in the menu, Change District, isn't available. We had problems with this function and not enough time to solve then. Therefore, unfortunately, a client cannot change of district. 


---- Case of the district: <br />
You first have to register the multicast ip and port, and the ip and port reserved to listen the requests from clients (here you can chose the address and port you want). 
Then, you have to enter the central server informations in order to connect to it (To do that, you have to enter the IP address of the server (you can see it printed in the central server console : 10.10.2.183). The central server port is 4600)

CAUTION : The multicast ip must be in the range 224.0.0.0 to 239.255.255.255

You can then chose other action to do with the menu. 


---- 

GENERAL CAUTION : All ports have to be between 5000 and 5500
