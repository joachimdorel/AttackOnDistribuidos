package Server;

import Creature.Titans;
import Util.*;

public class District {
    String name;
    String ipMulticast;
    String puertoMulticast;
    String ipPeticiones;
    String puertoPeticiones;

    public District(String name, String ipMulticast, String puertoMulticast, String ipPeticiones, String puertoPeticiones) {
        this.name = name;
        this.ipMulticast = ipMulticast;
        this.puertoMulticast = puertoMulticast;
        this.ipPeticiones = ipPeticiones;
        this.puertoPeticiones = puertoPeticiones;
    }

    @Override
    public String toString() {
        return "Nombre:'" + name + ',' +
                ", IP Multicast:'" + ipMulticast + ',' +
                ", Puerto Multicast:'" + puertoMulticast + ',' +
                ", IP Peticiones:'" + ipPeticiones + ',' +
                ", Puerto Peticiones:'" + puertoPeticiones;
    }
}
