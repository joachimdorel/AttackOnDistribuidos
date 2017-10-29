package Server;

import Creature.Titans;
import Util.*;

public class District {
    private String name;
    private String multicastIP;
    private int multicastPort;
    private String requestIP;
    private int requestPort;

    public District(String name, String ipMulticast, int puertoMulticast, String requestIP, int requestPort) {
        this.name = name;
        this.multicastIP = ipMulticast;
        this.multicastPort = puertoMulticast;
        this.requestIP = requestIP;
        this.requestPort = requestPort;
    }

    public String getName() {
        return name;
    }

    public int getMulticastPort() {
        return multicastPort;
    }

    public String getMulticastIP() {
        return multicastIP;
    }

    public int getRequestPort() {
        return requestPort;
    }

    public String getRequestIP() {
        return requestIP;
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
