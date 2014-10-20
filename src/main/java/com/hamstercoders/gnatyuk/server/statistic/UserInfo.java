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
    private Date lastRequest;

    private Set<String> uniqueRequests;

    public UserInfo(String ip){
        this.ip = ip;
        uniqueRequests = new HashSet<>();
        lastRequest = new Date();
    }

    public void addRequest(String request, long timeInMilliseconds){
        totalRequests++;
        lastRequest = new Date(timeInMilliseconds);
        uniqueRequests.add(request);
    }

    public Date getLastRequest() {
        return lastRequest;
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
