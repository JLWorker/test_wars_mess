package org.itmo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SpyMessenger {
    class Message {
        public String sender;
        public String receiver;
        public String message;
        public String passcode;
        public LocalDateTime sendTime;
        public boolean forceDelete;
    };

    Map<String, List<Message>> inbox; // index from receiver -> [Message]

    void sendMessage(String sender, String receiver, String message, String passcode) {
        Message m = new Message();
        m.sender = sender;
        m.receiver = receiver;
        m.message = message;
        m.passcode = passcode;
        m.forceDelete = false;
        m.sendTime = LocalDateTime.now();

        List<Message> ms = inbox.get(receiver);
        if (ms == null) {
            List<Message> newQueue = new ArrayList<>();
            newQueue.add(m);
            inbox.put(receiver, newQueue);
        }
        else {
            if (ms.size() == 5) {
                ms.remove(0);
            }
            ms.add(m);
        }
    }

    String readMessage(String user, String passcode) {
        List<Message> ms = inbox.get(user);
        if (ms == null) {
            return "";
        }

        for (int i = 0; i < ms.size(); i++) {
            Message m = ms.get(i);
            if (m.passcode.equals(passcode)) {
                if (m.forceDelete || ChronoUnit.MILLIS.between(m.sendTime, LocalDateTime.now()) > 1500) {
                    m.forceDelete = true;
                    return "";
                }
                else {
                    m.forceDelete = true;
                    return m.message;
                }
            }
        }

        return "";
    }
}