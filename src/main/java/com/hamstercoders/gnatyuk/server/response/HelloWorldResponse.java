package com.hamstercoders.gnatyuk.server.response;

import io.netty.buffer.Unpooled;
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
public class HelloWorldResponse extends AResponse{

    public static final long RESPONSE_DELAY = 10000;//TODO 10 sec
    public static final String HELLO_WORLD_MESSAGE = "Hello World!";

    public HelloWorldResponse(HttpRequest request){
        super(request);
        sleep(RESPONSE_DELAY);
    }

    @Override
    protected HttpResponse createHttpResponseObject() {
        String buf = createResponseMessage();
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK,
                Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        return response;
    }

    private void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String createResponseMessage(){
        return HELLO_WORLD_MESSAGE;
    }





}
