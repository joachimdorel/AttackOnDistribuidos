package Server;

public class Distrito {

    String nombre;
    String ipMulticast;
    String puertoMulticast;
    String ipPeticiones;
    String puertoPeticiones;

    public Distrito(){}

    public Distrito(String nombre, String ipMulticast, String puertoMulticast, String ipPeticiones, String puertoPeticiones) {
        this.nombre = nombre;
        this.ipMulticast = ipMulticast;
        this.puertoMulticast = puertoMulticast;
        this.ipPeticiones = ipPeticiones;
        this.puertoPeticiones = puertoPeticiones;
    }
}
