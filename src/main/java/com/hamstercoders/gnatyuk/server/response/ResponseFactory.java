package com.hamstercoders.gnatyuk.server.response;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * Created by Sergiy on 18-Oct-14.
 */
public class ResponseFactory {

    private static final String HELLO_RESPONSE = "hello";
    private static final String STATISTIC_RESPONSE = "status";
    private static final String REDIRECT_RESPONSE = "redirect";

    private QueryStringDecoder queryStringDecoder;
    private HttpRequest httpRequest;
    private String action;

    public ResponseFactory(HttpRequest request){
        httpRequest = request;
        queryStringDecoder = new QueryStringDecoder(request.getUri());
        action = parseAction();
    }

    private String parseAction(){
        String[] segments = queryStringDecoder.path().split("/");
        if(segments.length>0){
            return segments[segments.length-1];
        }

        return "";
    }

    private DefaultResponse determineResponse(String row){
        switch (row){
            case HELLO_RESPONSE:
                return new HelloWorldResponse(httpRequest);
            case STATISTIC_RESPONSE:
//                return new StatusResponse(httpRequest);
                break;
            case REDIRECT_RESPONSE:
                return  new RedirectResponse(httpRequest);

        }
        return new DefaultResponse(httpRequest);
    }

    public DefaultResponse getResponse(){
        return determineResponse(action);
    }

}
