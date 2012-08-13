package org.stary.webterminal.server;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

import static org.stary.webterminal.server.HttpUtils.setContentTypeHeader;
import static org.stary.webterminal.server.HttpUtils.setDateHeaders;

/**
 * RestApiHandler
 */
public class RestApiHandler implements BusinessLogicHandler {
    @Override
    public void handleRequest(ChannelHandlerContext ctx, MessageEvent event) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=UTF-8");
        setDateHeaders(response);

        StringBuilder json = new StringBuilder();
        json.append("{ 'channels' : [");
        for (Channel channel : SmartCardTerminalServer.allChannels) {
            json.append("{'channel' : '").append(channel.toString()).append("'},");
        }
        json.append("]}");

        response.setContent(ChannelBuffers.copiedBuffer(json.toString(), CharsetUtil.UTF_8));


        Channel channel = event.getChannel();

        channel.write(response);
    }
}
