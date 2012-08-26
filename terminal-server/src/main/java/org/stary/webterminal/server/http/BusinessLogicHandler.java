package org.stary.webterminal.server.http;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

/**
 * BusinessLogicHandler
 */
public interface BusinessLogicHandler {
    void handleRequest(ChannelHandlerContext ctx, MessageEvent event);

    enum Handler {
        REST_API,
        HTTP_FILE
    }
}
