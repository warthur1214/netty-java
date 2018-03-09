package com.warthur.netty.server;

import com.warthur.netty.handler.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by warth on 2018/3/9.
 */
public class HttpServer {

	private final int port;

	private HttpServer(int port) {
		this.port = port;
	}

	private void start() {
		ServerBootstrap b = new ServerBootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();

		try {
			b.group(group)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) {
						socketChannel.pipeline()
								.addLast("decoder", new HttpRequestDecoder())
								.addLast("encoder", new HttpResponseEncoder())
								.addLast("aggregator", new HttpObjectAggregator(512 * 1024))
								.addLast("handler", new HttpServerHandler());
					}
				})
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);

			b.bind(port).sync().channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: " + HttpServer.class.getSimpleName() + " <port>");
			return;
		}
		int port = Integer.parseInt(args[0]);
		new HttpServer(port).start();
	}
}
