package com.warthur.netty.server;

import com.warthur.netty.decoder.LiveDecoder;
import com.warthur.netty.decoder.LiveEncoder;
import com.warthur.netty.handler.LiveServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import lombok.extern.log4j.Log4j;

/**
 * Created by warth on 2018/3/9.
 */
@Log4j
public class LiveServer {
	private int port;

	public LiveServer(int port) {
		this.port = port;
	}

	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap discard = new ServerBootstrap();
			discard.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) {
							socketChannel.pipeline()
									.addLast("decoder", new LiveDecoder())
									.addLast("encoder", new LiveEncoder())
									.addLast("handler", new LiveServerHandler());
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = discard.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.getDefaultFactory());
		int port = 8080;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}

		new LiveServer(port).run();
	}
}
