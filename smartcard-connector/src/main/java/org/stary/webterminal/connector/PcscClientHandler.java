package org.stary.webterminal.connector;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
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
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) {
        logger.info(event.getMessage().toString());
        ChannelBuffer buffer = (ChannelBuffer) event.getMessage();
        try {
            // todo send back response apdu (frame it write it back with buffer
            ViewModel.cardChannel.transmit(new CommandAPDU(buffer.array())).getBytes();

        } catch (CardException e) {
            e.printStackTrace();
        }


        event.getChannel().write(event.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());

        e.getChannel().close();
    }
}
