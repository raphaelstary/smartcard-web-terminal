package org.stary.webterminal.server.http;

import org.jboss.netty.channel.*;
import org.stary.webterminal.server.SmartCardTerminalServer;
import org.stary.webterminal.server.http.BusinessLogicHandler;
import org.stary.webterminal.server.http.KeyValuePair;

import java.util.ArrayList;
import java.util.List;

import static org.stary.webterminal.server.http.HttpUtils.jsonList;
import static org.stary.webterminal.server.http.HttpUtils.jsonObject;
import static org.stary.webterminal.server.http.HttpUtils.sendJson;

/**
 * RestApiHandler
 */
public class RestApiHandler implements BusinessLogicHandler {
    @Override
    public void handleRequest(ChannelHandlerContext ctx, MessageEvent event) {
        List<String> jsonChannels = new ArrayList<>();

        for (Channel channel: SmartCardTerminalServer.allChannels) {
            jsonChannels.add(
                    jsonObject(new KeyValuePair("id", channel.getId().toString()))
                            .toString()
            );
        }

        sendJson(
                event.getChannel(),
                jsonList("channels", jsonChannels.toArray(new String[jsonChannels.size()])).toString()
        );
    }
}
