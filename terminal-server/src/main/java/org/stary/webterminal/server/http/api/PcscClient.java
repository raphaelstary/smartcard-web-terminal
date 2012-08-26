package org.stary.webterminal.server.http.api;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.stary.webterminal.server.SmartCardTerminalServer;
import org.stary.webterminal.server.http.HttpUtils;
import org.stary.webterminal.server.http.JsonMapper;

/**
 * PcscClientAction
 */
public class PcscClient extends Action implements RestApiAction {
    public PcscClient(JsonMapper mapper) {
        super(mapper);
    }

    @Override
    public String getAction() {
        return "getPcscClient";
    }

    @Override
    public void process(ChannelHandlerContext ctx, MessageEvent event) {
        JSONObject request = mapper.newInstance(event);

        if (!request.containsKey("id")) {
            HttpUtils.sendError(ctx, HttpResponseStatus.NOT_ACCEPTABLE);
            return;
        }

        Integer requestedId = ((Long) request.get("id")).intValue();
        if (requestedId == null) {
            HttpUtils.sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        JSONArray response = new JSONArray();
        if (SmartCardTerminalServer.pcscData.containsKey(requestedId))
            response.addAll(SmartCardTerminalServer.pcscData.get(requestedId));
        else
            response.add("no active channel found for given id");

        HttpUtils.sendJson(event.getChannel(), response.toJSONString());
    }
}
