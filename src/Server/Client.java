package Server;

public class Client {
    private String name;
    private String connectedToDistrict;

    public Client(String name){
        this.name = name;
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
}