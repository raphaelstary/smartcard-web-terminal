package org.stary.webterminal.server.http.api;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

/**
 * RestApiAction
 */
public interface RestApiAction {

    String getAction();

    void process(ChannelHandlerContext ctx, MessageEvent event);
}
