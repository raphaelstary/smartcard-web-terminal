package org.stary.webterminal.connector;

import org.jboss.netty.channel.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author raphael
 */
public class PcscClientHandler extends SimpleChannelUpstreamHandler {
    private static final Logger logger = Logger.getLogger(PcscClientHandler.class.getName());

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent) {
            logger.info(e.toString());
        }
        super.handleUpstream(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        logger.info(e.getMessage().toString());

        // todo implement
        e.getChannel().write(e.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());

        e.getChannel().close();
    }
}
