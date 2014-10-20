package com.hamstercoders.gnatyuk.server.response;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Sergiy on 19-Oct-14.
 */
public class DefaultResponse {

    protected HttpRequest request;

    private StringBuilder buf = new StringBuilder();

    public DefaultResponse(HttpRequest request) {
        this.request = request;
    }


    public void response(ChannelHandlerContext ctx) {
        prepareResponseData();
        writeAndCloseConnectionIfNotKeepAlive(ctx);
    }



    protected void response(ChannelHandlerContext ctx, String message){
        prepareResponseData();
        buf.append("\n\r"+message);
        writeAndCloseConnectionIfNotKeepAlive(ctx);
    }



    protected boolean writeResponse(StringBuilder buf, ChannelHandlerContext ctx) {
        // Decide whether to close the connection or not.
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK,
                Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        // Write the response.
        ctx.write(response);
        return keepAlive;
    }

    private void writeAndCloseConnectionIfNotKeepAlive(ChannelHandlerContext ctx){
        if (!writeResponse(buf, ctx)) {
            // If keep-alive is off, close the connection once the content is fully written.
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void prepareResponseData(){

        buf.setLength(0);
        buf.append("WELCOME TO THE WEB SERVER\r\n");
        buf.append("===================================\r\n");

        buf.append("VERSION: ").append(request.getProtocolVersion()).append("\r\n");
        buf.append("HOSTNAME: ").append(request.headers().get(HOST)).append("\r\n");
        buf.append("REQUEST_URI: ").append(request.getUri()).append("\r\n\r\n");

        HttpHeaders headers = request.headers();
        if (!headers.isEmpty()) {
            for (Map.Entry<String, String> h : headers) {
                String key = h.getKey();
                String value = h.getValue();
                buf.append("HEADER: ").append(key).append(" = ").append(value).append("\r\n");
            }
            buf.append("\r\n");
        }

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        Map<String, List<String>> params = queryStringDecoder.parameters();
        if (!params.isEmpty()) {
            for (Entry<String, List<String>> p : params.entrySet()) {
                String key = p.getKey();
                List<String> vals = p.getValue();
                for (String val : vals) {
                    buf.append("PARAM: ").append(key).append(" = ").append(val).append("\r\n");
                }
            }
            buf.append("\r\n");
        }

        appendDecoderResult(buf, request);
    }


    private void appendDecoderResult(StringBuilder buf, HttpObject o) {
        DecoderResult result = o.getDecoderResult();
        if (result.isSuccess()) {
            return;
        }

        buf.append(".. WITH DECODER FAILURE: ");
        buf.append(result.cause());
        buf.append("\r\n");
    }
}
