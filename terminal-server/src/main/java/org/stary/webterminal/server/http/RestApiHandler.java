package org.stary.webterminal.server.http;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.json.simple.JSONObject;
import org.stary.webterminal.server.http.api.RestApiAction;

import java.util.List;

/**
 * RestApiHandler
 */
public class RestApiHandler implements BusinessLogicHandler {

    private static final String ACTION = "action";
    private final List<RestApiAction> actions;
    private final JsonMapper mapper;

    public RestApiHandler(List<RestApiAction> actions, JsonMapper mapper) {
        this.actions = actions;
        this.mapper = mapper;
    }

    @Override
    public void handleRequest(ChannelHandlerContext ctx, MessageEvent event) {
        JSONObject jsonObject = mapper.newInstance(event);

        assert jsonObject != null;
        String actionType = (String) jsonObject.get(ACTION);

        boolean actionNotFound = true;
        for (RestApiAction action: actions) {

            if (action.getAction().equalsIgnoreCase(actionType)) {
                action.process(ctx, event);
                actionNotFound = false;
            }
        }

        if (actionNotFound)
            // couldn't decide which status to take :)
            HttpUtils.sendError(ctx, HttpResponseStatus.NOT_IMPLEMENTED);
    }
}
