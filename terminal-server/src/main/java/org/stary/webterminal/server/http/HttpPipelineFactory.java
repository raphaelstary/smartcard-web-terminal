package org.stary.webterminal.server.http;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author raphael
 */
public class HttpPipelineFactory implements ChannelPipelineFactory {
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

        Map<BusinessLogicHandler.Handler, BusinessLogicHandler> businessHandlers = new HashMap<>();
        businessHandlers.put(BusinessLogicHandler.Handler.REST_API, new RestApiHandler());
        businessHandlers.put(BusinessLogicHandler.Handler.HTTP_FILE, new HttpFileHandler());

        pipeline.addLast("handler", new HttpHandler(businessHandlers));

        return pipeline;
    }
}
