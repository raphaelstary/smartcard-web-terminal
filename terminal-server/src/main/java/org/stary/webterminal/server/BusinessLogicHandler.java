package org.stary.webterminal.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;

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
