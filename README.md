**Attack on Distribuidos**

Project as part of the course of Distribuited Systems at the "Universidad Tecnica Federico Santa Maria", based in Valparaiso, Chile.

*How to launch the project*

Case of the server : <br />
Go to the virtual machine grupo-18@10.10.2.183

    javac -d . **/*.java
    java Server.Central

Case of the client : <br />
Connect to the virtual machine grupo-18@10.10.2.104

    javac -d . **/*.java
    java Client.Client
    
Case of the district server : <br />
Connect to the virtual machine grupo-18@10.10.2.79
    
    javac -d . **/*.java
    java Distributed.Distributed
    
*How to initialize each entity*

Case of the server: <br />
A menu allows you to chose the action you want to do. You have to register new districts manually. <br />

CAUTION : The scanner in the console works with a fifo (first in first out) logical. You have to response to all  previous questions to can response the actual one.

Case of the client: <br />
You firstly have to connect to the central server. <br />
You must then choose a district to connect with. This district has to be previously created and registered by the central server.
You can then choose other action to do with the menu.

Case of the district: <br />
You firstly have to register the multicast ip and port, and the ip and port reserved to listen the requests from clients.

CAUTION : The multicast ip must be in the range 224.0.0.0 to 239.255.255.255

You can then choose other action to do with the menu. 

GENERAL CAUTION : All port have to be between 5000 and 5500