package com.warthur.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j;

/**
 * Created by warth on 2018/3/9.
 */
@Log4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf b = (ByteBuf) msg;

		// 判断不等于回车
		if (!b.toString().equals("\n")) {
			System.out.println("Server received: " + b.toString(CharsetUtil.UTF_8));
		}

		ctx.writeAndFlush(msg);
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
