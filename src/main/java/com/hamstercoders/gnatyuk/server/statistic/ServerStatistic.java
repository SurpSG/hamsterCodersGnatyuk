package com.hamstercoders.gnatyuk.server.statistic;

import java.util.*;

/**
 * Created by Sergiy on 20-Oct-14.
 */
public class ServerStatistic {

    private static final int CONNECTIONS_INFO_NUMBER_TO_STORE = 16;

    private int totalRequests;
    private int activeConnections;

    private List<ConnectionInfo> connectionsInfo;

    //key - uri, value - counter
    private Map<String, Integer> redirectsInfo;

    private List<ClientInfo> clientsInfo;

    private static final ServerStatistic serverStatistic= new ServerStatistic();

    private ServerStatistic(){
        totalRequests = 0;
        activeConnections = 0;

        connectionsInfo = new ArrayList<>();
        redirectsInfo = new HashMap<>();
        clientsInfo = new ArrayList<>();
    }

    public static ServerStatistic getInstance(){
        return serverStatistic;
    }

    public synchronized void addConnectionInfo(ConnectionInfo connectionInfo){
        if(connectionsInfo.size() >= CONNECTIONS_INFO_NUMBER_TO_STORE){
            connectionsInfo.remove(0);
        }
        connectionsInfo.add(connectionInfo);
        totalRequests++;
        addRequest(connectionInfo);
    }

    private synchronized void addRequest(ConnectionInfo connectionInfo){

        String ip = connectionInfo.getIp();
        for (ClientInfo clientInfo: clientsInfo){
            if(clientInfo.getIp().equals(ip)){
                clientInfo.addRequest(connectionInfo.getUri(), connectionInfo.getTimestamp());
                return;
            }
        }

        ClientInfo clientInfo = new ClientInfo(ip);
        clientInfo.addRequest(connectionInfo.getUri(), connectionInfo.getTimestamp());
        clientsInfo.add(clientInfo);

    }

    public synchronized void addRedirectInfo(String url){
        Integer redirectsCountForUrl = redirectsInfo.get(url);
        redirectsCountForUrl = (redirectsCountForUrl != null) ? ++redirectsCountForUrl : 1;
        redirectsInfo.put(url, redirectsCountForUrl);
    }

    public synchronized void increaseTotalRequests(){
        totalRequests++;
    }

    public void increaseActiveConnections(){
        changeActiveConnectionsValue(1);
    }

    public void decreaseActiveConnections(){
        changeActiveConnectionsValue(-1);
    }

    private synchronized void changeActiveConnectionsValue(int delta){
        activeConnections +=delta;
    }

    public synchronized int getActiveConnections(){
        return activeConnections;
    }

    public synchronized List<ConnectionInfo> getConnectionsInfo() {
        return new ArrayList<>(connectionsInfo);
    }

    public synchronized Map<String, Integer> getRedirectsInfo() {
        return new HashMap<>(redirectsInfo);
    }

    public synchronized List<ClientInfo> getClientsInfo() {
        return new ArrayList<>(clientsInfo);
    }

    public synchronized int getTotalRequests() {
        return totalRequests;
    }

    @Override
    public String toString() {
        return "totalRequests = "+totalRequests+", activeConnections = "+activeConnections;
    }

    public class ClientInfo{

        private String ip;
        private int requestsCount;
        private Date lastRequestTime;

        private Set<String> uniqueRequests;

        public ClientInfo(String ip){
            this.ip = ip;
            requestsCount = 0;
            lastRequestTime = new Date(System.currentTimeMillis());
            uniqueRequests = new HashSet<>();
        }

        public void addRequest(String request, Date timestamp){
            lastRequestTime = (timestamp != null) ? timestamp :lastRequestTime;
            uniqueRequests.add(request);
            requestsCount++;
        }

        public String getIp() {
            return ip;
        }


        public Date getLastRequestTime() {
            return lastRequestTime;
        }

        public Set<String> getUniqueRequests() {
            return uniqueRequests;
        }

        public int getRequestsCount() {
            return requestsCount;
        }

        @Override
        public int hashCode() {
            return ip.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return ((ClientInfo)obj).ip.equals(ip);
        }

        @Override
        public String toString() {
            return String.format("ClientInfo[ip = %s,  requestsCount = %s, lastRequestTime = %s, uniqueRequests = %s]",
                    ip, requestsCount, lastRequestTime, uniqueRequests.toString());
        }
    }
}
