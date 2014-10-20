package com.hamstercoders.gnatyuk.server.response;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Sergiy on 18-Oct-14.
 */
public class HelloWorldResponse extends DefaultResponse{

    public static final long RESPONSE_DELAY = 1000;//TODO 10 sec
    public static final String HELLO_WORLD_MESSAGE = "Hello World!";

    protected final StringBuilder buf = new StringBuilder();


    public HelloWorldResponse(HttpRequest request){
        super(request);
        sleep(RESPONSE_DELAY);
        createResponse();
    }

    private void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void response(ChannelHandlerContext ctx) {

        boolean writeResponseSuccess = writeResponse(buf, ctx);
        if (!writeResponseSuccess) {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void createResponse(){
        buf.append(HELLO_WORLD_MESSAGE);
    }





}
