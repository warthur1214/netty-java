package com.warthur.netty.decoder;

import com.warthur.netty.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * Created by warth on 2018/3/9.
 */
public class TimeDecoder extends ReplayingDecoder {
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
		list.add(new UnixTime(byteBuf.readUnsignedInt()));
	}
}
