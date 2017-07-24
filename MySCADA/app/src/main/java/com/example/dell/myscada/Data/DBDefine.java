package com.example.dell.myscada.Data;

/**
 * Created by Dell on 2017/5/12.
 */

public class DBDefine {
    public final static String JTDS_NAME = "net.sourceforge.jtds.jdbc.Driver";
    private String username;
    private String password;
    private String server;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
