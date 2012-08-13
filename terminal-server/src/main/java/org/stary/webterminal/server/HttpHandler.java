package org.stary.webterminal.server;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author raphael
 */
public class HttpHandler extends SimpleChannelUpstreamHandler {

    private static final String APPLET_JAR = "smartcard-connector-0.1-SNAPSHOT-jar-with-dependencies.jar";

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws IOException {
        HttpRequest request = (HttpRequest) event.getMessage();

        if (request.getUri().startsWith("/api/")) {
            return;

        } else if (request.getMethod() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        final String path = sanitizeUri(request.getUri());

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

        long fileLength = raf.length();

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

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setContent((ChannelBuffers.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8)));

        ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }

        if ("/".equals(uri))
            uri = "/connect.html";

        if ("/applet.jar".equals(uri)) {
            uri = uri.replace('/', File.separatorChar);
            return System.getProperty("user.dir") + File.separator + "smartcard-connector" + File.separator + "target" + File.separator + APPLET_JAR;

        } else {
            uri = uri.replace('/', File.separatorChar);
            return System.getProperty("user.dir") + File.separator + "resources" + uri;
        }
    }

    private static void setContentTypeHeader(HttpResponse response, File file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(file.getPath());
        if (type == null)
            type = "application/x-java-applet;version=1.7";
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, type);
    }

    private static void setDateHeaders(HttpResponse response) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        Calendar time = new GregorianCalendar();
        response.setHeader(HttpHeaders.Names.DATE, formatter.format(time.getTime()));
    }
}
