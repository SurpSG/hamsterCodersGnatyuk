package com.hamstercoders.gnatyuk.server.statistic;

import java.util.Date;

/**
 * Created by Sergiy on 20-Oct-14.
 */
public class ConnectionInfo {
    private String ip;
    private String uri;
    private Date timestamp;
    private long sentBytes;
    private long receivedBytes;
    private int speedWrite;// KB/sec
    private int speedRead;// KB/sec


    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setReceivedBytes(long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setSentBytes(long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setSpeedRead(int speedRead) {
        this.speedRead = speedRead;
    }

    public void setSpeedWrite(int speedWrite) {
        this.speedWrite = speedWrite;
    }

    public String getUri() {
        return uri;
    }

    public String getIp() {
        return ip;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getSpeedRead() {
        return speedRead;
    }

    public int getSpeedWrite() {
        return speedWrite;
    }

    public long getReceivedBytes() {
        return receivedBytes;
    }

    public long getSentBytes() {
        return sentBytes;
    }

    @Override
    public String toString() {
        return "ConnectionInfo["
                +"ip='"+ip+"' "
                +"uri='"+uri+"' "
                +"timestamp='"+timestamp+"' "
                +"sentBytes='"+sentBytes+"' "
                +"receivedBytes='"+receivedBytes+"' "
                +"speedWrite='"+speedWrite+"' "
                +"speedRead='"+speedRead+"'"
                +"]";
    }
}
