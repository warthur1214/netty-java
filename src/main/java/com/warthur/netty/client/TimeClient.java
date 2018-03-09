package com.warthur.netty.client;

import com.warthur.netty.decoder.TimeDecoder;
import com.warthur.netty.handler.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by warth on 2018/3/9.
 */
public class TimeClient {

	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 8080;

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) {
							socketChannel.pipeline()
									.addLast("decoder", new TimeDecoder())
									.addLast("handler", new TimeClientHandler());
						}
					});
			b.connect(host, port).sync().channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}
