package org.stary.webterminal.server.pcsc;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.stary.webterminal.server.SmartCardTerminalServer;

import java.util.*;
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
        SmartCardTerminalServer.pcscData.put(event.getChannel().getId(), new ArrayList<PcscMessage>());

        logger.info("new client connected with channel id: " + event.getChannel().getId());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) {
        ChannelBuffer buffer = (ChannelBuffer) event.getMessage();
        List<Long> response = new ArrayList<>();
        for (Byte b: buffer.array()) {
            response.add(b.longValue());
        }
        SmartCardTerminalServer.pcscData.get(event.getChannel().getId()).add(new PcscMessage("reader", response));

        logger.info("received message from channel '" + event.getChannel().getId() + "' : " +
                Arrays.toString(buffer.array()));
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
        SmartCardTerminalServer.pcscData.remove(event.getChannel().getId());
        logger.info("channel was closed (id: " + event.getChannel().getId() + ")");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent event) {
        // Close the connection when an exception is raised.
        logger.log(Level.WARNING, "Unexpected exception from downstream.", event.getCause());
        event.getChannel().close();
    }
}
