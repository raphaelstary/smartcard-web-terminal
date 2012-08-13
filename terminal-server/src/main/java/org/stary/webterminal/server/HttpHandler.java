package org.stary.webterminal.server;

import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

import static org.stary.webterminal.server.HttpUtils.*;

/**
 * @author raphael
 */
public class HttpHandler extends SimpleChannelUpstreamHandler {

    private final Map<BusinessLogicHandler.Handler, BusinessLogicHandler> businessHandlers;

    public HttpHandler(Map<BusinessLogicHandler.Handler, BusinessLogicHandler> businessHandlers) {
        this.businessHandlers = businessHandlers;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws IOException {
        HttpRequest request = (HttpRequest) event.getMessage();

        if (request.getUri().startsWith("/api/")) {
            businessHandlers.get(BusinessLogicHandler.Handler.REST_API).handleRequest(ctx, event);
            return;
        }
        businessHandlers.get(BusinessLogicHandler.Handler.HTTP_FILE).handleRequest(ctx, event);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent event) {
        Channel channel = event.getChannel();
        Throwable cause = event.getCause();
        if (cause instanceof TooLongFrameException) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        cause.printStackTrace();
        if (channel.isConnected()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
