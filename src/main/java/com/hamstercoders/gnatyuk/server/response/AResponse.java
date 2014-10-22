package com.hamstercoders.gnatyuk.server.response;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Sergiy on 20-Oct-14.
 */
public abstract class AResponse {

    protected HttpResponse response;
    protected HttpRequest request;
    private long timeToSend;

    public AResponse(HttpRequest request) {
        this.request = request;
        response = createHttpResponseObject();
    }


    protected abstract HttpResponse createHttpResponseObject();

    public void response(ChannelHandlerContext ctx) {

        writeAndCloseConnectionIfNotKeepAlive(ctx);
    }

    protected void writeAndCloseConnectionIfNotKeepAlive(ChannelHandlerContext ctx){
        timeToSend = System.currentTimeMillis();
        writeAndFlush(ctx);

    }

    protected void  writeAndFlush(ChannelHandlerContext ctx){

        ChannelFutureListener CLOSE = new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                future.channel().close();
                timeToSend = System.currentTimeMillis() - timeToSend;
            }
        };
        ctx.write(response).addListener(CLOSE);
    }

    public long getTimeToSend() {
        return timeToSend;
    }
}