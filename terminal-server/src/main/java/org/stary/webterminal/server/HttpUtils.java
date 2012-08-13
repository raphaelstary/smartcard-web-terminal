package org.stary.webterminal.server;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * HttpUtils
 */
public final class HttpUtils {

    static final String APPLET_JAR = "smartcard-connector-0.1-SNAPSHOT-jar-with-dependencies.jar";

    static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setContent((ChannelBuffers.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8)));

        ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
    }

    static String sanitizeUri(String uri) {
        String sanitizedUri = null;
        try {
            sanitizedUri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                sanitizedUri = URLDecoder.decode(sanitizedUri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }
        return sanitizedUri;
    }

    static void setContentTypeHeader(HttpResponse response, File file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(file.getPath());
        if (type == null)
            type = "application/x-java-applet;version=1.7";
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, type);
    }

    static void setDateHeaders(HttpResponse response) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        Calendar time = new GregorianCalendar();
        response.setHeader(HttpHeaders.Names.DATE, formatter.format(time.getTime()));
    }
}
