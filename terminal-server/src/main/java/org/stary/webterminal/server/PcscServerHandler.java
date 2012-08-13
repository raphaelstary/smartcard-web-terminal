package org.stary.webterminal.server;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author raphael
 */
public class PcscServerHandler extends SimpleChannelUpstreamHandler {

    private static final Logger logger = Logger.getLogger(PcscServerHandler.class.getName());

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent event) {
        SmartCardTerminalServer.allChannels.add(event.getChannel());
        SmartCardTerminalServer.pcscData.put(event.getChannel().getId(), new ArrayList<String>());

        logger.info("new client connected with channel id: " + event.getChannel().getId());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) {
        ChannelBuffer buffer = (ChannelBuffer) event.getMessage();
        SmartCardTerminalServer.pcscData.get(event.getChannel().getId()).add(Arrays.toString(buffer.array()));

        logger.info(Arrays.toString(buffer.array()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent event) {
        // Close the connection when an exception is raised.
        logger.log(Level.WARNING, "Unexpected exception from downstream.", event.getCause());
        event.getChannel().close();
    }
}
