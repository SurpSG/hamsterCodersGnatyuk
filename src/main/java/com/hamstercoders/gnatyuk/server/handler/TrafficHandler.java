package com.hamstercoders.gnatyuk.server.handler;

import com.hamstercoders.gnatyuk.server.statistic.ConnectionInfo;
import com.hamstercoders.gnatyuk.server.statistic.ServerStatistic;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;

/**
 * Created by Sergiy on 22-Oct-14.
 */
public class TrafficHandler extends ChannelTrafficShapingHandler {


    private long timeStamp;

    private ConnectionInfo connectionInfo;

    public TrafficHandler(ConnectionInfo connectionInfo, long writeLimit, long readLimit) {
        super(writeLimit, readLimit);
        this.connectionInfo = connectionInfo;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ServerStatistic.getInstance().increaseActiveConnections();
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        long currentReadBytes = trafficCounter.currentReadBytes();
        long currentWriteBytes = trafficCounter.currentWrittenBytes();
        trafficCounter.stop();

        if ("/favicon.ico".equals(connectionInfo.getUri())) {
            return;
        }

        connectionInfo.setTimestamp(new Date(timeStamp));

        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        InetAddress inetaddress = socketAddress.getAddress();
        String ipAddress = inetaddress.getHostAddress(); // IP address of client
        connectionInfo.setIp(ipAddress);


        int readSpeed = (int) (trafficCounter.lastReadThroughput() >> 10);
        connectionInfo.setSpeedRead(readSpeed);
        connectionInfo.setReceivedBytes(currentReadBytes);

        int writeSpeed = (int) (trafficCounter.lastWriteThroughput() >> 10);
        connectionInfo.setSpeedWrite(writeSpeed);
        connectionInfo.setSentBytes(currentWriteBytes);

        ServerStatistic.getInstance().addConnectionInfo(connectionInfo);

        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        timeStamp = System.currentTimeMillis();
        trafficCounter.start();
        super.channelRead(ctx, msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ServerStatistic.getInstance().decreaseActiveConnections();
        super.channelInactive(ctx);
    }

}
