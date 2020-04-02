package it.polimi.ingsw.server.model;

public class User {
    private String name;
    private String ip;

    public User(String name, String ip){
        this.name=name;
        this.ip=ip;
    }

    private String getName() {
        return name;
    }

    public void setName( String name){
        this.name=name;
    }
    private String getIp(){
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String name(){
        return name;
    }

    public String ip(){
        return ip;
    }
}
