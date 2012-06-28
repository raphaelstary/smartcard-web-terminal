package org.stary.webterminal.connector;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.stary.webterminal.common.ByteHeaderFrameDecoder;

/**
 * @author raphael
 */
public class PcscClientPipelineFactory implements ChannelPipelineFactory {
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("framer", new ByteHeaderFrameDecoder());

        // and then business logic.
        pipeline.addLast("handler", new PcscClientHandler());

        return pipeline;
    }
}
