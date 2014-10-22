package com.hamstercoders.gnatyuk.server.statistic;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sergiy on 20-Oct-14.
 */
public class UserInfo {

    private String ip;

    private int totalRequests;
    private Date lastRequestTime;

    private Set<String> uniqueRequests;

    public UserInfo(String ip){
        uniqueRequests = new HashSet<>();
        lastRequestTime = new Date();
        this.ip = ip;
    }

    public void setLastRequestTime(long lastRequestTime) {
        this.lastRequestTime = new Date(lastRequestTime) ;
    }

    public void addRequest(String request){
        totalRequests++;
        uniqueRequests.add(request);
    }

    public String getIp() {
        return ip;
    }

    public Date getLastRequestTime() {
        return lastRequestTime;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public Set<String> getUniqueRequests() {
        return uniqueRequests;
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return ((UserInfo)obj).ip == this.ip;
    }
}
