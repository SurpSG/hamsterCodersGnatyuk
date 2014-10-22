package com.hamstercoders.gnatyuk.server.response;

import com.hamstercoders.gnatyuk.server.statistic.ServerStatistic;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

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
public class RedirectResponse extends AResponse {

    public static final String GET_REQUEST_URL_KEY = "url";

    public RedirectResponse(HttpRequest request) {
        super(request);
    }

    @Override
    protected HttpResponse createHttpResponseObject() {

        String urlForRedirect = "";
        Map<String, List<String>> params = new QueryStringDecoder(request.getUri()).parameters();
        for (String key:params.keySet()){
            if(key.equalsIgnoreCase(GET_REQUEST_URL_KEY)){
                urlForRedirect = getUrlValueForRedirectFromUrlParams(params.get(key));
            }
        }

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);

        response.headers().set(LOCATION, urlForRedirect);


        ServerStatistic.getInstance().addRedirectInfo(urlForRedirect);

        return response;
    }

    private String getUrlValueForRedirectFromUrlParams(List<String> values){
        for (String value:values){
            return value;
        }
        return "";
    }

}
