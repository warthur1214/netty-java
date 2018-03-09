package com.warthur.netty.handler;

import com.warthur.netty.channel.LiveChannelCache;
import com.warthur.netty.pojo.LiveMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by warth on 2018/3/9.
 */
public class LiveServerHandler extends SimpleChannelInboundHandler<LiveMessage> {

	private static Map<Integer, LiveChannelCache> channelCache = new HashMap<>();
	private Logger logger = LoggerFactory.getLogger(LiveServerHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("new client connected");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, LiveMessage liveMessage) {
		Channel channel = channelHandlerContext.channel();
		final int hashCode = channel.hashCode();

		System.out.println(System.currentTimeMillis() + " channel hashCode:" + hashCode + " msg:" + liveMessage + " cache:" + channelCache.size());

		if (!channelCache.containsKey(hashCode)) {
			System.out.println("channelCache.containsKey(hashCode), put key:" + hashCode);

			// 监听channel的close事件
			channel.closeFuture().addListener(future -> {
				System.out.println(System.currentTimeMillis() + " channel close, remove key:" + hashCode);
				channelCache.remove(hashCode);
			});
			// 设置延时10s任务，无消息关闭channel
			ScheduledFuture scheduledFuture = channelHandlerContext.executor().schedule(() -> {
				System.out.println(System.currentTimeMillis() + " schedule runs, close channel:" + hashCode);
				channel.close();
			}, 10, TimeUnit.SECONDS);
			channelCache.put(hashCode, new LiveChannelCache(channel, scheduledFuture));
		}

		LiveChannelCache cache = channelCache.get(hashCode);
		switch (liveMessage.getType()) {
			case LiveMessage.TYPE_HEART:

				// 心跳消息，设置5s延时任务，无心跳关闭channel
				ScheduledFuture scheduledFuture = channelHandlerContext.executor().schedule(
						(Callable<ChannelFuture>) channel::close, 5, TimeUnit.SECONDS);

				cache.getScheduledFuture().cancel(true);
				cache.setScheduledFuture(scheduledFuture);
				channelHandlerContext.channel().writeAndFlush(liveMessage);
				break;
			case LiveMessage.TYPE_MESSAGE:
				ScheduledFuture f = cache.getScheduledFuture();
				f.cancel(true);

				f = channelHandlerContext.executor().schedule(
						(Callable<ChannelFuture>) channel::close, 10, TimeUnit.SECONDS);
				cache.setScheduledFuture(f);
				channelCache.forEach((key, value) -> {
					Channel otherChannel = value.getChannel();
					otherChannel.writeAndFlush(liveMessage);
				});
				break;
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelReadComplete");
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.debug("exceptionCaught");
		if(null != cause) cause.printStackTrace();
		if(null != ctx) ctx.close();
	}
}
