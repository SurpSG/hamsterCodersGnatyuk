package com.hamstercoders.gnatyuk.server.statistic;

import java.util.Date;

/**
 * Created by Sergiy on 20-Oct-14.
 */
public class ConnectionInfo {
    private String ip;
    private String uri;
    private Date timestamp;
    private int sentBytes;
    private int receivedBytes;
    private double speed;// bytes/sec


    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setReceivedBytes(int receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setSentBytes(int sentBytes) {
        this.sentBytes = sentBytes;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
