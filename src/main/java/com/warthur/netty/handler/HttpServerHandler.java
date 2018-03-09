package com.warthur.netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

/**
 * Created by warth on 2018/3/9.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
		System.out.println("class: " + fullHttpRequest.getClass().getName());
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK,
				Unpooled.wrappedBuffer("test".getBytes()));
		HttpHeaders headers = response.headers();
		headers.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charst=UTF-8");
		headers.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

		channelHandlerContext.write(response);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelReadComplete");
		super.channelReadComplete(ctx);
		ctx.flush(); // 4
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.out.println("exceptionCaught");
		if(null != cause) cause.printStackTrace();
		if(null != ctx) ctx.close();
	}
}
