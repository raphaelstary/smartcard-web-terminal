package org.stary.webterminal.server.http.api;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

/**
 * CommandApduAction
 */
public class CommandApdu implements RestApiAction {
    @Override
    public String getAction() {
        return "sendCommandApdu";
    }

    @Override
    public void process(MessageEvent event) {
    }
}
