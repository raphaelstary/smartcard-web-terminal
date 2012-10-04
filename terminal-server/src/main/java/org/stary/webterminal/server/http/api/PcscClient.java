package org.stary.webterminal.server.http.api;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.stary.webterminal.server.SmartCardTerminalServer;
import org.stary.webterminal.server.http.HttpUtils;
import org.stary.webterminal.server.http.JsonMapper;
import org.stary.webterminal.server.pcsc.PcscMessage;

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

        if (SmartCardTerminalServer.pcscData.containsKey(requestedId)) {
            JSONArray totalResponse = new JSONArray();

            for (PcscMessage msg: SmartCardTerminalServer.pcscData.get(requestedId)) {
                JSONArray content = new JSONArray();
                content.addAll(msg.content);

                JSONObject response = new JSONObject();
                response.put("content", content);
                response.put("sender", msg.sender);

                totalResponse.add(response);
            }
            HttpUtils.sendJson(event.getChannel(), totalResponse.toJSONString());

        } else {
            JSONObject response = new JSONObject();
            response.put("status", "error");
            response.put("cause", "no active channel found for given id");
            HttpUtils.sendJson(event.getChannel(), response.toJSONString());
        }
    }
}
