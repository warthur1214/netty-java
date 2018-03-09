package com.warthur.netty.handler;

import com.warthur.netty.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by warth on 2018/3/9.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		final ChannelFuture f = ctx.writeAndFlush(new UnixTime());

		f.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf b = (ByteBuf) msg;
		System.out.println(b.toString(CharsetUtil.UTF_8));
		b.release();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
