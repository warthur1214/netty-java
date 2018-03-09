package com.warthur.netty.decoder;

import com.warthur.netty.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by warth on 2018/3/9.
 */
public class TimeEncoder extends MessageToByteEncoder<UnixTime> {

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, UnixTime o, ByteBuf byteBuf) {
		byteBuf.writeInt((int) o.getValue());
	}
}
