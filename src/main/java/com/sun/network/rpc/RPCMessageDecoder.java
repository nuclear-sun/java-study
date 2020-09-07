package com.sun.network.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

// V3: decode
class RPCMessageDecoder extends ByteToMessageDecoder {

    // TODO magic 97
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        while (in.readableBytes() >= 97) {

            byte[] hdrBytes = new byte[97];
            in.getBytes(in.readerIndex(), hdrBytes);

            // 1. 反序列化头
            MsgHeader header = (MsgHeader)SerDeUtil.deserialize(hdrBytes);

            long contentLength = header.getContentLength();

            if(in.readableBytes() >= 97 + contentLength) {
                // 2. 反序列化体
                byte[] body = new byte[(int)contentLength];
                in.readBytes(97); // discard head
                in.readBytes(body);

                Object bodyObject = SerDeUtil.deserialize(body);
                out.add(bodyObject);
            } else {
                break;
            }
        }
    }
}