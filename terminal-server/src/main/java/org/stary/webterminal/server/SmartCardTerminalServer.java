package org.stary.webterminal.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.stary.webterminal.server.http.HttpPipelineFactory;
import org.stary.webterminal.server.pcsc.PcscMessage;
import org.stary.webterminal.server.pcsc.PcscServerPipelineFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * @author raphael
 */
public class SmartCardTerminalServer {
    private final int httpPort;
    private final int tcpPort;

    private static final Logger logger = Logger.getLogger(SmartCardTerminalServer.class.getName());

    public static final ChannelGroup allChannels = new DefaultChannelGroup("pcsc-server");
    public static final ConcurrentMap<Integer, List<PcscMessage>> pcscData = new ConcurrentHashMap<>();

    public SmartCardTerminalServer(int httpPort, int tcpPort) {
        this.httpPort = httpPort;
        this.tcpPort = tcpPort;
    }

    public void run() {
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new HttpPipelineFactory());

        bootstrap.bind(new InetSocketAddress(httpPort));


        ServerBootstrap tcpBootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

        tcpBootstrap.setPipelineFactory(new PcscServerPipelineFactory());

        tcpBootstrap.bind(new InetSocketAddress(tcpPort));
        logger.info("server started");
    }

    public static ChannelFuture send(int channelId, byte[] msg) {
        ChannelBuffer buffer = ChannelBuffers.buffer(msg.length+1);
        buffer.writeByte(msg.length);
        buffer.writeBytes(msg);

        return allChannels.find(channelId).write(buffer);
    }

    public static void main(String[] args) {
        if (args.length > 1)
            new SmartCardTerminalServer(Integer.parseInt(args[0]), Integer.parseInt(args[1])).run();
        else
            new SmartCardTerminalServer(8080, 8088).run();
    }
}
