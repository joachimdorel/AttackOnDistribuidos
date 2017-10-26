package Server;

import Creature.Titans;
import Util.*;

public class District {
    String name;
    String multicastIP;
    int multicastPort;
    String requestIP;
    int requestPort;

    public District(String name, String ipMulticast, int puertoMulticast, String requestIP, int requestPort) {
        this.name = name;
        this.multicastIP = ipMulticast;
        this.multicastPort = puertoMulticast;
        this.requestIP = requestIP;
        this.requestPort = requestPort;
    }

    @Override
    public String toString() {
        return "Nombre:'" + name + ',' +
                ", IP Multicast:'" + multicastIP + ',' +
                ", Puerto Multicast:'" + multicastPort + ',' +
                ", IP Peticiones:'" + requestIP + ',' +
                ", Puerto Peticiones:'" + requestPort;
    }
}
