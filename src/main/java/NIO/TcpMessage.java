package NIO;

import java.util.Arrays;

/**
 * @className: TcpMessage.java
 * @version: 1.0.0
 * @description: tcp协议消息封装
 * @author: chen qinping
 * @date: 2021/9/26 17:48
 */
public class TcpMessage {
    public static final char MESSAGE_HEAD =0x1f1f;
    /**
     * 身份认证消息
     */
    public static final byte TYPE_ID_VALIDATE_MESSAGE=0x1;
    /**
     * 心跳消息
     */
    public static final byte TYPE_HEART_BEAT_MESSAGE=0x2;
    /**
     * 能耗数据消息
     */
    public static final byte TYPE_DATA_MESSAGE=0x3;

    /**
     * 消息类型，TYPE_ID_VALIDATE_MESSAGE/TYPE_HEART_BEAT_MESSAGE/TYPE_HEART_BEAT_MESSAGE
     */
    private byte messageType;
    private String message;
    private byte[] messageBytes;
    /**
     * 消息发出后通道是否关闭
     */
    private boolean isClosed=false;

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public byte[] getMessageBytes() {
        return messageBytes;
    }

    public void setMessageBytes(byte[] messageBytes) {
        this.messageBytes = messageBytes;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TcpMessage{" +
                "messageType=" + messageType +
                ", message='" + message + '\'' +
                ", messageBytes=" + Arrays.toString(messageBytes) +
                ", isClosed=" + isClosed +
                '}';
    }
}
