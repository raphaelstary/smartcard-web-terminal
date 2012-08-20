package org.stary.webterminal.server.http.api;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.json.simple.JSONObject;
import org.stary.webterminal.server.SmartCardTerminalServer;
import org.stary.webterminal.server.http.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * PcscClientsAction
 */
public class PcscClients implements RestApiAction {

    private static final String GET_PCSC_CLIENTS = "getPcscClients";

    @Override
    public String getAction() {
        return GET_PCSC_CLIENTS;
    }

    @Override
    public void process(MessageEvent event) {

        JSONObject channels = new JSONObject();
        for (Channel channel: SmartCardTerminalServer.allChannels) {
            channels.put("id", channel.getId());
        }

        JSONObject json = new JSONObject();
        json.put("channels", channels);

        HttpUtils.sendJson(event.getChannel(), json.toJSONString());
    }
}
