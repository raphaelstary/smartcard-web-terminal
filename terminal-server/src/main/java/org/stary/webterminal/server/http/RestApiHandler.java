package org.stary.webterminal.server.http;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.stary.webterminal.server.SmartCardTerminalServer;
import org.stary.webterminal.server.http.BusinessLogicHandler;
import org.stary.webterminal.server.http.KeyValuePair;
import org.stary.webterminal.server.http.api.RestApiAction;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.stary.webterminal.server.http.HttpUtils.jsonList;
import static org.stary.webterminal.server.http.HttpUtils.jsonObject;
import static org.stary.webterminal.server.http.HttpUtils.sendJson;

/**
 * RestApiHandler
 */
public class RestApiHandler implements BusinessLogicHandler {

    private static final String UTF_8 = "UTF-8";
    private static final String ACTION = "action";
    private final List<RestApiAction> actions;
    private final JSONParser parser;

    public RestApiHandler(List<RestApiAction> actions, JSONParser parser) {
        this.actions = actions;
        this.parser = parser;
    }

    @Override
    public void handleRequest(ChannelHandlerContext ctx, MessageEvent event) {
        String json = ((HttpRequest) event.getMessage()).getContent().toString(Charset.forName(UTF_8));

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert jsonObject != null;
        String actionType = (String) jsonObject.get(ACTION);

        boolean actionNotFound = true;
        for (RestApiAction action: actions) {

            if (action.getAction().equalsIgnoreCase(actionType)) {
                action.process(event);
                actionNotFound = false;
            }
        }

        if (actionNotFound)
            // couldn't decide which status to take :)
            HttpUtils.sendError(ctx, HttpResponseStatus.NOT_IMPLEMENTED);
    }
}
