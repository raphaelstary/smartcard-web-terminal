package org.stary.webterminal.server.http.api;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

/**
 * PcscClientAction
 */
public class PcscClient implements RestApiAction {
    @Override
    public String getAction() {
        return "getPcscClient";
    }

    @Override
    public void process(MessageEvent event) {
    }
}
