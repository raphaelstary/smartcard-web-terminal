package org.stary.webterminal.server;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.stary.webterminal.server.HttpUtils.jsonList;
import static org.stary.webterminal.server.HttpUtils.jsonObject;
import static org.stary.webterminal.server.HttpUtils.sendJson;

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
