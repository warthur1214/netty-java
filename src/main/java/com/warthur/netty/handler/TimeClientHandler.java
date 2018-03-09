package com.warthur.netty.handler;

import com.warthur.netty.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by warth on 2018/3/9.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		UnixTime time = (UnixTime) msg;
		System.out.println("接收到服务端消息：" + time);
		final ByteBuf date = ctx.alloc().buffer(time.toString().length());
		date.writeBytes(time.toString().getBytes());

		ctx.writeAndFlush(date).addListener(ChannelFutureListener.CLOSE);
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}
