package Server;

public class District {
    private String name;
    private String multicastIP;
    private int multicastPort;
    private String requestIP;
    private int requestPort;

    public District(String name, String multicastIP, int multicastPort, String requestIP, int requestPort) {
        this.name = name;
        this.multicastIP = multicastIP;
        this.multicastPort = multicastPort;
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
        return "Name:'" + name + ',' +
                ", Multicast IP:'" + multicastIP + ',' +
                ", Multicast Port:'" + multicastPort + ',' +
                ", Request IP:'" + requestIP + ',' +
                ", Request Port:'" + requestPort;
    }
}
