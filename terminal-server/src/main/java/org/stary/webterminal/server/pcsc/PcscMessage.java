package org.stary.webterminal.server.pcsc;

import java.util.List;

/**
 * Message
 */
public class PcscMessage {
    public final String sender;
    public final List<Long> content;

    public PcscMessage(String sender, List<Long> content) {
        this.sender = sender;
        this.content = content;
    }
}
