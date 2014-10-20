package com.hamstercoders.gnatyuk.server.response;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Sergiy on 18-Oct-14.
 */
public class RedirectResponse extends DefaultResponse {



    public static final String GET_REQUEST_URL_KEY = "url";

    public RedirectResponse(HttpRequest request) {
        super(request);
    }

    @Override
    public void response(ChannelHandlerContext ctx) {

        Map<String, List<String>> params = new QueryStringDecoder(request.getUri()).parameters();
        for (String key:params.keySet()){
            if(key.equalsIgnoreCase(GET_REQUEST_URL_KEY)){
                String url = getUrlValueForRedirectFromUrlParams(params.get(key));
                if(url != null){
                    sendRedirect(ctx, url);
                }else{
                    String errorMessage = String.format("Incorrect url %s for redirect", params.get(key));
                    super.response(ctx,errorMessage);
                }
                return;
            }
        }
    }

    private String getUrlValueForRedirectFromUrlParams(List<String> values){
        for (String value:values){
            if(isValidUrl(value)){
                return value;
            }
        }
        return null;
    }



    private boolean isValidUrl(String url){
        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        return patt.matcher(url).matches();
    }

    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(LOCATION, newUri);

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
