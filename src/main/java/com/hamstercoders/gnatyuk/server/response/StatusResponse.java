package com.hamstercoders.gnatyuk.server.response;

import com.hamstercoders.gnatyuk.server.statistic.ConnectionInfo;
import com.hamstercoders.gnatyuk.server.statistic.ServerStatistic;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Sergiy on 20-Oct-14.
 */
public class StatusResponse extends AResponse {

    protected final StringBuilder buf = new StringBuilder();

    public StatusResponse(HttpRequest request) {
        super(request);
    }

    @Override
    protected HttpResponse createHttpResponseObject() {

        StringBuilder buf = generateStatistic();
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK,
                Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");

        if (keepAlive) {
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        return response;

    }

    private StringBuilder generateStatistic(){
        ServerStatistic serverStatistic = ServerStatistic.getInstance();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Total requests: "+serverStatistic.getTotalRequests()+"<br>");
        stringBuilder.append("Active connections: "+serverStatistic.getActiveConnections()+"<br>");
        stringBuilder.append(generateRequestsForEachIpTable());
        stringBuilder.append(generateRedirectsInfoTable());
        stringBuilder.append(generate16LastConnectionsTable());
        return stringBuilder;
    }

    private StringBuilder generateRequestsForEachIpTable(){
        StringBuilder stringBuilder = new StringBuilder();
        ServerStatistic serverStatistic = ServerStatistic.getInstance();

        stringBuilder.append("<table border=1><tr>"
                +"<th>ip</th>"
                +"<th>unique requests number</th>"
                +"<th>requests number</th>"
                +"<th>last request time</th></tr>");
        for (ServerStatistic.ClientInfo clientInfo: serverStatistic.getClientsInfo()){
            stringBuilder.append("<tr>"
                    +"<td>"+clientInfo.getIp()+"</td>"
                    +"<td>"+clientInfo.getUniqueRequests().size()+"</td>"
                    +"<td>"+clientInfo.getRequestsCount()+"</td>"
                    +"<td>"+clientInfo.getLastRequestTime()+"</td>"
                    +"</tr>");
        }
        stringBuilder.append("</table>");

        return stringBuilder;
    }

    private StringBuilder generateRedirectsInfoTable(){
        StringBuilder stringBuilder = new StringBuilder();
        ServerStatistic serverStatistic = ServerStatistic.getInstance();

        stringBuilder.append("<br>Redirects info:");
        stringBuilder.append("<table border=1><tr>"
                +"<th>url</th>"
                +"<th>redirects number</th></tr>");
        Map<String, Integer> redirectsInfo = serverStatistic.getRedirectsInfo();
        for (String uri: redirectsInfo.keySet()){
            stringBuilder.append("<tr>"
                    +"<td>"+uri+"</td>"
                    +"<td>"+redirectsInfo.get(uri)+"</td></tr>");
        }

        stringBuilder.append("</table>");

        return stringBuilder;
    }

    private StringBuilder generate16LastConnectionsTable(){
        StringBuilder stringBuilder = new StringBuilder();
        ServerStatistic serverStatistic = ServerStatistic.getInstance();

        stringBuilder.append("<br>Last 16 connections info:");
        stringBuilder.append("<table border=1><tr>"
                +"<th>ip</th>"
                +"<th>URI</th>"
                +"<th>timestamp</th>"
                +"<th>sent bytes</th>"
                +"<th>received bytes</th>"
                +"<th>write speed KB/s</th>"
                +"<th>read speed KB/s</th></tr>");
        for (ConnectionInfo connectionInfo: serverStatistic.getConnectionsInfo()){
            stringBuilder.append("<tr>"
                    +"<td>"+connectionInfo.getIp()+"</td>"
                    +"<td>"+connectionInfo.getUri()+"</td>"
                    +"<td>"+connectionInfo.getTimestamp()+"</td>"
                    +"<td>"+connectionInfo.getSentBytes()+"</td>"
                    +"<td>"+connectionInfo.getReceivedBytes()+"</td>"
                    +"<td>"+connectionInfo.getSpeedWrite()+"</td>"
                    +"<td>"+connectionInfo.getSpeedRead()+"</td></tr>");
        }

        stringBuilder.append("</table>");

        return stringBuilder;
    }


}
