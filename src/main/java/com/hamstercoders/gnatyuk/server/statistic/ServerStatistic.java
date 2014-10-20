package com.hamstercoders.gnatyuk.server.statistic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiy on 20-Oct-14.
 */
public class ServerStatistic {

    private static final int CONNECTIONS_INFO_NUMBER_TO_STORE = 16;

    private int totalRequests;
    private int activeConnections;

    private List<UserInfo> usersInfo;
    private List<ConnectionInfo> connectionsInfo;

    private static final ServerStatistic serverStatistic= new ServerStatistic();

    private ServerStatistic(){
        totalRequests = 0;
        activeConnections = 0;
        usersInfo = new ArrayList<>();
        connectionsInfo = new ArrayList<>();
    }

    public static ServerStatistic getInstance(){
        return serverStatistic;
    }

    public synchronized void addConnectionInfo(ConnectionInfo connectionInfo){
        if(connectionsInfo.size() >= CONNECTIONS_INFO_NUMBER_TO_STORE){
            connectionsInfo.remove(0);
            connectionsInfo.add(connectionInfo);
        }
    }

    public synchronized void addRequest(){

    }

    public synchronized void increaseActiveConnections(){
        activeConnections++;
    }

    public synchronized void decreaseActiveConnections(){
        activeConnections--;
    }

    public int getActiveConnections(){
        return activeConnections;
    }
}
