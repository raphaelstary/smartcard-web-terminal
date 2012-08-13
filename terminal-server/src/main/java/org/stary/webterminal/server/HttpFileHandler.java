package org.stary.webterminal.server;

import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.stary.webterminal.server.HttpUtils.*;

/**
 * HttpFileHandler
 */
public class HttpFileHandler implements BusinessLogicHandler {

    @Override
    public void handleRequest(ChannelHandlerContext ctx, MessageEvent event) {
        HttpRequest request = (HttpRequest) event.getMessage();

        if (request.getMethod() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        final String path = mapUrlToPath(sanitizeUri(request.getUri()));

        if (path == null) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }

        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        if (!file.isFile()) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }

        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        long fileLength = 0;
        try {
            fileLength = raf.length();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpHeaders.setContentLength(response, fileLength);
        setContentTypeHeader(response, file);
        setDateHeaders(response);

        Channel channel = event.getChannel();

        channel.write(response);

        final FileRegion region = new DefaultFileRegion(raf.getChannel(), 0, fileLength);
        ChannelFuture writeFuture = channel.write(region);

        writeFuture.addListener(new ChannelFutureProgressListener() {
            @Override
            public void operationProgressed(ChannelFuture future, long amount, long current, long total) {
                System.out.printf("%s: %d / %d (+%d)%n", path, current, total, amount);
            }

            @Override
            public void operationComplete(ChannelFuture future) {
                region.releaseExternalResources();
            }
        });

        if (!HttpHeaders.isKeepAlive(request))
            writeFuture.addListener(ChannelFutureListener.CLOSE);
    }

    private String mapUrlToPath(String url) {
        if ("/".equals(url))
            url = "/connect.html";

        if ("/applet.jar".equals(url)) {
            return System.getProperty("user.dir") + File.separator + "smartcard-connector" + File.separator + "target" + File.separator + APPLET_JAR;

        } else {
            url = url.replace('/', File.separatorChar);
            return System.getProperty("user.dir") + File.separator + "resources" + url;
        }
    }
}
