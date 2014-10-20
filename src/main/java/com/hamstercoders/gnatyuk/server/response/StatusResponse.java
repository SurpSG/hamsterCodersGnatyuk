package com.hamstercoders.gnatyuk.server.response;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Created by Sergiy on 20-Oct-14.
 */
public class StatusResponse extends DefaultResponse {

    protected final StringBuilder buf = new StringBuilder();

    public StatusResponse(HttpRequest request) {
        super(request);
    }

    @Override
    public void response(ChannelHandlerContext ctx) {

        boolean writeResponseSuccess = writeResponse(buf, ctx);
        if (!writeResponseSuccess) {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
