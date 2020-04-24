package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.socket.SocketServer;

import java.net.ServerSocket;

public class User {
    private SocketServer socket;
    private String name;
    private String ip;

    public User(String name, SocketServer socket){
        this.name = name;
        this.socket = socket;
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
