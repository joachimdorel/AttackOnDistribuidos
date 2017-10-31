package Server;

public class Client {
    private String name;
    private String connectedToDistrict;

    public Client(String name){
        this.name = name;
    }
    public Client(String name, String connectedToDistrict) {
        this.name = name;
        this.connectedToDistrict = connectedToDistrict;
    }

    public String getConnectedToDistrict(){
        return connectedToDistrict;
    }

    public void setConnectedToDistrict(String district){
        connectedToDistrict = district;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name:'" + name +
                ", Connected to district:'" + connectedToDistrict;
    }
}
