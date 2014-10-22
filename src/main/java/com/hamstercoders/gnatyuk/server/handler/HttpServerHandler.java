package com.hamstercoders.gnatyuk.server.handler;

import com.hamstercoders.gnatyuk.server.response.AResponse;
import com.hamstercoders.gnatyuk.server.response.ResponseFactory;
import com.hamstercoders.gnatyuk.server.statistic.ConnectionInfo;
import com.hamstercoders.gnatyuk.server.statistic.ServerStatistic;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    private  ConnectionInfo connectionInfo;
    public  HttpServerHandler(ConnectionInfo connectionInfo){
        this.connectionInfo = connectionInfo;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;


            if ("/favicon.ico".equals(request.getUri())) {
                FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.NOT_FOUND);
                sendHttpResponse(ctx, request, res);
                return;
            }

            if (HttpHeaders.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }

            connectionInfo.setUri(request.getUri());
            ResponseFactory responseFactory = new ResponseFactory(request);
            AResponse response = responseFactory.getResponse();
            response.response(ctx);
        }
    }

    private void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }

    private void sendHttpResponse(
            ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
