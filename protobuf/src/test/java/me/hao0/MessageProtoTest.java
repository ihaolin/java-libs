package me.hao0;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Author: haolin
 * Date  : 8/26/15.
 * Email : haolin.h0@gmail.com
 */
public class MessageProtoTest {

    @Test
    public void testWriteRead() throws IOException {
        MessageProto.Message.Builder builder = MessageProto.Message.newBuilder();
        builder.setId(1);
        builder.setSender("haolin");
        builder.setReceiver("linhao");
        builder.setContent("fuck you");

        MessageProto.Message msg = builder.build();

        // write msg
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        msg.writeTo(output);

        // read msg
        byte[] data = output.toByteArray();
        System.out.println("encoded bytes length: " + data.length);
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        msg = MessageProto.Message.parseFrom(input);

        System.out.println("id = " + msg.getId());
        System.out.println("sender = " + msg.getSender());
        System.out.println("receiver = " + msg.getReceiver());
        System.out.println("content = " + msg.getContent());
    }

    @Test
    public void testSimpleMessage() throws IOException {
        SimpleMessageProto.SimpleMessage.Builder builder = SimpleMessageProto.SimpleMessage.newBuilder();
        builder.setA(150);
        SimpleMessageProto.SimpleMessage msg = builder.build();
        System.out.println("serialized size: " + msg.getSerializedSize());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        msg.writeTo(output);
        byte[] bytes = output.toByteArray();
        System.out.println(bytesToHexString(bytes));
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
