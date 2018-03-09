package com.warthur.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j;

/**
 * Created by warth on 2018/3/8.
 */
@Log4j
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		// 丢弃接收到的数据
		ByteBuf in = (ByteBuf) msg;
		System.out.println(in.toString(CharsetUtil.UTF_8));
		in.release();

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// 出现异常关闭连接
		log.error(cause);
		ctx.close();
	}
}
