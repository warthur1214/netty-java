package com.warthur.netty.channel;

import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Created by warth on 2018/3/9.
 */
@Data
@AllArgsConstructor
public class LiveChannelCache {

	private Channel channel;
	private ScheduledFuture scheduledFuture;

}
