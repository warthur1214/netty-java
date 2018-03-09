package com.warthur.netty.decoder;

import com.warthur.netty.pojo.LiveMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.StringUtil;

/**
 * Created by warth on 2018/3/9.
 */
public class LiveEncoder extends MessageToByteEncoder<LiveMessage> {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, LiveMessage liveMessage, ByteBuf byteBuf) {
		byteBuf.writeByte(liveMessage.getType());
		byteBuf.writeInt(liveMessage.getLength());
		if (!StringUtil.isNullOrEmpty(liveMessage.getContent())) {
			byteBuf.writeBytes(liveMessage.getContent().getBytes());
		}
	}
}
