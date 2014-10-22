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
    private String requestUri;

    public ResponseFactory(HttpRequest request){
        httpRequest = request;
        requestUri = request.getUri();
        queryStringDecoder = new QueryStringDecoder(requestUri);
        action = parseAction();
    }

    private String parseAction(){
        String[] segments = queryStringDecoder.path().split("/");
        if(segments.length>0){
            return segments[segments.length-1];
        }

        return "";
    }

    private AResponse determineResponse(String row){
        switch (row){
            case HELLO_RESPONSE:
                return new HelloWorldResponse(httpRequest);
            case STATISTIC_RESPONSE:
                return new StatusResponse(httpRequest);
            case REDIRECT_RESPONSE:
                return  new RedirectResponse(httpRequest);

        }
        return new DefaultResponse(httpRequest);
    }

    public AResponse getResponse(){
        return determineResponse(action);
    }

    public String getRequestUri() {
        return requestUri;
    }
}
