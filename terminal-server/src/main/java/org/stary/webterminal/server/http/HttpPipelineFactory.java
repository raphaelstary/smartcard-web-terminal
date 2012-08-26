package org.stary.webterminal.server.http;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;
import org.stary.webterminal.server.http.api.CommandApdu;
import org.stary.webterminal.server.http.api.PcscClient;
import org.stary.webterminal.server.http.api.PcscClients;
import org.stary.webterminal.server.http.api.RestApiAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        JsonMapper mapper = new JsonMapper();
        List<RestApiAction> actions = new ArrayList<>();
        actions.add(new CommandApdu(mapper));
        actions.add(new PcscClient(mapper));
        actions.add(new PcscClients());

        Map<BusinessLogicHandler.Handler, BusinessLogicHandler> businessHandlers = new HashMap<>();
        businessHandlers.put(BusinessLogicHandler.Handler.REST_API, new RestApiHandler(actions, mapper));
        businessHandlers.put(BusinessLogicHandler.Handler.HTTP_FILE, new HttpFileHandler());

        pipeline.addLast("handler", new HttpHandler(businessHandlers));

        return pipeline;
    }
}
