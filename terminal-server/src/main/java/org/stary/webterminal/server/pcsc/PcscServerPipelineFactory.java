package org.stary.webterminal.server.pcsc;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.stary.webterminal.common.ByteHeaderFrameDecoder;

/**
 * @author raphael
 */
public class PcscServerPipelineFactory implements ChannelPipelineFactory {
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("framer", new ByteHeaderFrameDecoder());

        pipeline.addLast("handler", new PcscServerHandler());

        return pipeline;
    }
}
