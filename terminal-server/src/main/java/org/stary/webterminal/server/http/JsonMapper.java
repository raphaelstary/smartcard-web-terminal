package org.stary.webterminal.server.http;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.charset.Charset;

/**
 * JsonMapper
 */
public class JsonMapper {
    private static final String UTF_8 = "UTF-8";
    private final JSONParser parser;

    public JsonMapper() {
        parser = new JSONParser();
    }

    public JSONObject newInstance(MessageEvent event) {
        String json = ((HttpRequest) event.getMessage()).getContent().toString(Charset.forName(UTF_8));

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
