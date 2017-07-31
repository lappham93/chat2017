//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.messaging.simp.stomp;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.*;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class StompHeaderAccessor extends SimpMessageHeaderAccessor {
    private static final AtomicLong messageIdCounter = new AtomicLong();
    private static final long[] DEFAULT_HEARTBEAT = new long[]{0L, 0L};
    public static final String STOMP_ID_HEADER = "id";
    public static final String STOMP_HOST_HEADER = "host";
    public static final String STOMP_ACCEPT_VERSION_HEADER = "accept-version";
    public static final String STOMP_MESSAGE_ID_HEADER = "message-id";
    public static final String STOMP_RECEIPT_HEADER = "receipt";
    public static final String STOMP_RECEIPT_ID_HEADER = "receipt-id";
    public static final String STOMP_SUBSCRIPTION_HEADER = "subscription";
    public static final String STOMP_VERSION_HEADER = "version";
    public static final String STOMP_MESSAGE_HEADER = "message";
    public static final String STOMP_ACK_HEADER = "ack";
    public static final String STOMP_NACK_HEADER = "nack";
    public static final String STOMP_LOGIN_HEADER = "login";
    public static final String STOMP_PASSCODE_HEADER = "passcode";
    public static final String STOMP_DESTINATION_HEADER = "destination";
    public static final String STOMP_CONTENT_TYPE_HEADER = "content-type";
    public static final String STOMP_CONTENT_LENGTH_HEADER = "content-length";
    public static final String STOMP_HEARTBEAT_HEADER = "heart-beat";
    private static final String COMMAND_HEADER = "stompCommand";
    private static final String CREDENTIALS_HEADER = "stompCredentials";

    StompHeaderAccessor(StompCommand command, Map<String, List<String>> externalSourceHeaders) {
        super(command.getMessageType(), externalSourceHeaders);
        this.setHeader("stompCommand", command);
        this.updateSimpMessageHeadersFromStompHeaders();
    }

    StompHeaderAccessor(Message<?> message) {
        super(message);
        this.updateStompHeadersFromSimpMessageHeaders();
    }

    StompHeaderAccessor() {
        super(SimpMessageType.HEARTBEAT, (Map)null);
    }

    void updateSimpMessageHeadersFromStompHeaders() {
        if(this.getNativeHeaders() != null) {
            String value = this.getFirstNativeHeader("destination");
            if(value != null) {
                super.setDestination(value);
            }

            value = this.getFirstNativeHeader("content-type");
            if(value != null) {
                super.setContentType(MimeTypeUtils.parseMimeType(value));
            }

            StompCommand command = this.getCommand();
            if(StompCommand.MESSAGE.equals(command)) {
                value = this.getFirstNativeHeader("subscription");
                if(value != null) {
                    super.setSubscriptionId(value);
                }
            } else if(!StompCommand.SUBSCRIBE.equals(command) && !StompCommand.UNSUBSCRIBE.equals(command)) {
                if(StompCommand.CONNECT.equals(command)) {
                    this.protectPasscode();
                }
            } else {
                value = this.getFirstNativeHeader("id");
                if(value != null) {
                    super.setSubscriptionId(value);
                }
            }

        }
    }

    void updateStompHeadersFromSimpMessageHeaders() {
        if(this.getDestination() != null) {
            this.setNativeHeader("destination", this.getDestination());
        }

        if(this.getContentType() != null) {
            this.setNativeHeader("content-type", this.getContentType().toString());
        }

        this.trySetStompHeaderForSubscriptionId();
    }

    protected MessageHeaderAccessor createAccessor(Message<?> message) {
        return wrap(message);
    }

    Map<String, List<String>> getNativeHeaders() {
        Map map = (Map)this.getHeader("nativeHeaders");
        return map != null?map:Collections.emptyMap();
    }

    public StompCommand updateStompCommandAsClientMessage() {
        if(this.getMessageType() != SimpMessageType.MESSAGE) {
            throw new IllegalStateException("Unexpected message type " + this.getMessageType());
        } else {
            if(this.getCommand() == null) {
                this.setHeader("stompCommand", StompCommand.SEND);
            } else if(!this.getCommand().equals(StompCommand.SEND)) {
                throw new IllegalStateException("Unexpected STOMP command " + this.getCommand());
            }

            return this.getCommand();
        }
    }

    public void updateStompCommandAsServerMessage() {
        if(this.getMessageType() != SimpMessageType.MESSAGE) {
            throw new IllegalStateException("Unexpected message type " + this.getMessageType());
        } else {
            StompCommand command = this.getCommand();
            if(command != null && !StompCommand.SEND.equals(command)) {
                if(!StompCommand.MESSAGE.equals(command)) {
                    throw new IllegalStateException("Unexpected STOMP command " + command);
                }
            } else {
                this.setHeader("stompCommand", StompCommand.MESSAGE);
            }

            this.trySetStompHeaderForSubscriptionId();
            if(this.getMessageId() == null) {
                String messageId = this.getSessionId() + '-' + messageIdCounter.getAndIncrement();
                this.setNativeHeader("message-id", messageId);
            }

        }
    }

    public StompCommand getCommand() {
        return (StompCommand)this.getHeader("stompCommand");
    }

    public boolean isHeartbeat() {
        return SimpMessageType.HEARTBEAT == this.getMessageType();
    }

    public long[] getHeartbeat() {
        String rawValue = this.getFirstNativeHeader("heart-beat");
        String[] rawValues = StringUtils.split(rawValue, ",");
        return rawValues == null?Arrays.copyOf(DEFAULT_HEARTBEAT, 2):new long[]{Long.valueOf(rawValues[0]).longValue(), Long.valueOf(rawValues[1]).longValue()};
    }

    public void setAcceptVersion(String acceptVersion) {
        this.setNativeHeader("accept-version", acceptVersion);
    }

    public Set<String> getAcceptVersion() {
        String rawValue = this.getFirstNativeHeader("accept-version");
        return rawValue != null?StringUtils.commaDelimitedListToSet(rawValue):Collections.emptySet();
    }

    public void setHost(String host) {
        this.setNativeHeader("host", host);
    }

    public String getHost() {
        return this.getFirstNativeHeader("host");
    }

    public void setDestination(String destination) {
        super.setDestination(destination);
        this.setNativeHeader("destination", destination);
    }

    public void setContentType(MimeType contentType) {
        super.setContentType(contentType);
        this.setNativeHeader("content-type", contentType.toString());
    }

    public void setSubscriptionId(String subscriptionId) {
        super.setSubscriptionId(subscriptionId);
        this.trySetStompHeaderForSubscriptionId();
    }

    private void trySetStompHeaderForSubscriptionId() {
        String subscriptionId = this.getSubscriptionId();
        if(subscriptionId != null) {
            if(this.getCommand() != null && StompCommand.MESSAGE.equals(this.getCommand())) {
                this.setNativeHeader("subscription", subscriptionId);
            } else {
                SimpMessageType messageType = this.getMessageType();
                if(SimpMessageType.SUBSCRIBE.equals(messageType) || SimpMessageType.UNSUBSCRIBE.equals(messageType)) {
                    this.setNativeHeader("id", subscriptionId);
                }
            }
        }

    }

    public Integer getContentLength() {
        return this.containsNativeHeader("content-length")?Integer.valueOf(this.getFirstNativeHeader("content-length")):null;
    }

    public void setContentLength(int contentLength) {
        this.setNativeHeader("content-length", String.valueOf(contentLength));
    }

    public void setHeartbeat(long cx, long cy) {
        this.setNativeHeader("heart-beat", cx + "," + cy);
    }

    public void setAck(String ack) {
        this.setNativeHeader("ack", ack);
    }

    public String getAck() {
        return this.getFirstNativeHeader("ack");
    }

    public void setNack(String nack) {
        this.setNativeHeader("nack", nack);
    }

    public String getNack() {
        return this.getFirstNativeHeader("nack");
    }

    public void setLogin(String login) {
        this.setNativeHeader("login", login);
    }

    public String getLogin() {
        return this.getFirstNativeHeader("login");
    }

    public void setPasscode(String passcode) {
        this.setNativeHeader("passcode", passcode);
        this.protectPasscode();
    }

    private void protectPasscode() {
        String value = this.getFirstNativeHeader("passcode");
        if(value != null && !"PROTECTED".equals(value)) {
            this.setHeader("stompCredentials", new StompHeaderAccessor.StompPasscode(value));
            this.setNativeHeader("passcode", "PROTECTED");
        }

    }

    public String getPasscode() {
        StompHeaderAccessor.StompPasscode credentials = (StompHeaderAccessor.StompPasscode)this.getHeader("stompCredentials");
        return credentials != null?credentials.passcode:null;
    }

    public void setReceiptId(String receiptId) {
        this.setNativeHeader("receipt-id", receiptId);
    }

    public String getReceiptId() {
        return this.getFirstNativeHeader("receipt-id");
    }

    public void setReceipt(String receiptId) {
        this.setNativeHeader("receipt", receiptId);
    }

    public String getReceipt() {
        return this.getFirstNativeHeader("receipt");
    }

    public String getMessage() {
        return this.getFirstNativeHeader("message");
    }

    public void setMessage(String content) {
        this.setNativeHeader("message", content);
    }

    public String getMessageId() {
        return this.getFirstNativeHeader("message-id");
    }

    public void setMessageId(String id) {
        this.setNativeHeader("message-id", id);
    }

    public String getVersion() {
        return this.getFirstNativeHeader("version");
    }

    public void setVersion(String version) {
        this.setNativeHeader("version", version);
    }

    public String getShortLogMessage(Object payload) {
        return StompCommand.SUBSCRIBE.equals(this.getCommand())?"SUBSCRIBE " + this.getDestination() + " id=" + this.getSubscriptionId() + this.appendSession():(StompCommand.UNSUBSCRIBE.equals(this.getCommand())?"UNSUBSCRIBE id=" + this.getSubscriptionId() + this.appendSession():(StompCommand.SEND.equals(this.getCommand())?"SEND " + this.getDestination() + this.appendSession() + this.appendPayload(payload):(StompCommand.CONNECT.equals(this.getCommand())?"CONNECT" + (this.getUser() != null?" user=" + this.getUser().getName():"") + this.appendSession():(StompCommand.CONNECTED.equals(this.getCommand())?"CONNECTED heart-beat=" + Arrays.toString(this.getHeartbeat()) + this.appendSession():(StompCommand.DISCONNECT.equals(this.getCommand())?"DISCONNECT" + (this.getReceipt() != null?" receipt=" + this.getReceipt():"") + this.appendSession():this.getDetailedLogMessage(payload))))));
    }

    public String getDetailedLogMessage(Object payload) {
        if(this.isHeartbeat()) {
            return "heart-beat in session " + this.getSessionId();
        } else {
            StompCommand command = this.getCommand();
            if(command == null) {
                return super.getDetailedLogMessage(payload);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(command.name()).append(" ").append(this.getNativeHeaders()).append(this.appendSession());
                if(this.getUser() != null) {
                    sb.append(", user=").append(this.getUser().getName());
                }

                if(command.isBodyAllowed()) {
                    sb.append(this.appendPayload(payload));
                }

                return sb.toString();
            }
        }
    }

    private String appendSession() {
        return " session=" + this.getSessionId();
    }

    private String appendPayload(Object payload) {
        if(payload.getClass() != byte[].class) {
            throw new IllegalStateException("Expected byte array payload but got: " + ClassUtils.getQualifiedName(payload.getClass()));
        } else {
            byte[] bytes = (byte[])((byte[])payload);
            String contentType = this.getContentType() != null?" " + this.getContentType().toString():"";
            if(bytes.length != 0 && this.getContentType() != null && this.isReadableContentType()) {
                Charset charset = this.getContentType().getCharset();
                charset = charset != null?charset:StompDecoder.UTF8_CHARSET;
                return contentType + " payload=" + new String(bytes, charset);
            } else {
                return contentType;
            }
        }
    }

    public static StompHeaderAccessor create(StompCommand command) {
        return new StompHeaderAccessor(command, (Map)null);
    }

    public static StompHeaderAccessor create(StompCommand command, Map<String, List<String>> headers) {
        return new StompHeaderAccessor(command, headers);
    }

    public static StompHeaderAccessor createForHeartbeat() {
        return new StompHeaderAccessor();
    }

    public static StompHeaderAccessor wrap(Message<?> message) {
        return new StompHeaderAccessor(message);
    }

    public static StompCommand getCommand(Map<String, Object> headers) {
        return (StompCommand)headers.get("stompCommand");
    }

    public static String getPasscode(Map<String, Object> headers) {
        StompHeaderAccessor.StompPasscode credentials = (StompHeaderAccessor.StompPasscode)headers.get("stompCredentials");
        return credentials != null?credentials.passcode:null;
    }

    public static Integer getContentLength(Map<String, List<String>> nativeHeaders) {
        List values = (List)nativeHeaders.get("content-length");
        return !CollectionUtils.isEmpty(values)?Integer.valueOf((String)values.get(0)):null;
    }

    private static class StompPasscode {
        private final String passcode;

        public StompPasscode(String passcode) {
            this.passcode = passcode;
        }

        public String toString() {
            return "[PROTECTED]";
        }
    }
}
