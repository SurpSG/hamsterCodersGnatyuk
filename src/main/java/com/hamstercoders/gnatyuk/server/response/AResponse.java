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
 *
 *  Class that generates HttpResponse object and do response.
 *  Specific HttpResponse should be created in child classes.
 */
public abstract class AResponse {

    protected HttpResponse response;
    protected HttpRequest request;

    public AResponse(HttpRequest request) {
        this.request = request;
        response = createHttpResponseObject();
    }

    protected abstract HttpResponse createHttpResponseObject();

    public void response(ChannelHandlerContext ctx) {
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    }
}