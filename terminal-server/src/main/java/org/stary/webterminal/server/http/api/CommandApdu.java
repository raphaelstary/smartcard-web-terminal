package org.stary.webterminal.server.http.api;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.json.simple.JSONObject;
import org.stary.webterminal.server.SmartCardTerminalServer;
import org.stary.webterminal.server.http.HttpUtils;
import org.stary.webterminal.server.http.JsonMapper;
import org.stary.webterminal.server.pcsc.PcscMessage;

import java.util.List;

/**
 * CommandApduAction
 */
public class CommandApdu extends Action implements RestApiAction {

    public CommandApdu(JsonMapper mapper) {
        super(mapper);
    }

    @Override
    public String getAction() {
        return "sendCommandApdu";
    }

    @Override
    public void process(ChannelHandlerContext ctx, final MessageEvent event) {
        JSONObject request = mapper.newInstance(event);

        if (!(request.containsKey("id") && request.containsKey("command"))) {
            HttpUtils.sendError(ctx, HttpResponseStatus.NOT_IMPLEMENTED);
            return;
        }

        int id = ((Long) request.get("id")).intValue();
        if (!SmartCardTerminalServer.pcscData.containsKey(id)) {
            JSONObject response = new JSONObject();
            response.put("status", "error");
            response.put("cause", "no active channel found for given id");
            HttpUtils.sendJson(event.getChannel(), response.toJSONString());
            return;
        }

        @SuppressWarnings("unchecked")
        List<Long> command = (List<Long>) request.get("command");
        SmartCardTerminalServer.pcscData.get(id).add(new PcscMessage("you", command));

        byte[] byteCommand = new byte[command.size()];
        int i = 0;
        for (Long l : command) {
            byteCommand[i] = l.byteValue();
            i++;
        }

        ChannelFuture future = SmartCardTerminalServer.send(id, byteCommand);

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                JSONObject response = new JSONObject();
                if (future.isSuccess()) {
                    response.put("status", "command sent");
                } else {
                    response.put("status", "error");
                }
                HttpUtils.sendJson(event.getChannel(), response.toJSONString());
            }
        });
    }
}
