package com.flyue.xiaomy.protocol;

import java.util.Arrays;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/17 21:36
 * @Description:
 */
public class ProtocolMessage {


    private final String head;
    private byte[] body;

    public ProtocolMessage(String head, byte[] body) {
        this.body = body == null ? "Null Body".getBytes() : body;
        this.head = head;
    }


    public byte[] getHeadByte() {
        return head.getBytes();
    }

    public int getHeadLength() {
        return this.getHeadByte().length;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProtocolMessage message = (ProtocolMessage) o;

        if (head != null ? !head.equals(message.head) : message.head != null) return false;
        return Arrays.equals(body, message.body);
    }

    @Override
    public String toString() {
        return "ProtocolMessage{" +
                "head='" + head + '\'' +
                ", body=" + Arrays.toString(body) +
                '}';
    }

    @Override
    public int hashCode() {
        int result = head != null ? head.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(body);
        return result;
    }
}
