package com.warthur.netty.decoder;

import com.warthur.netty.pojo.LiveMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by warth on 2018/3/9.
 */
public class LiveDecoder extends ReplayingDecoder<LiveDecoder.LiveState> {

	private Logger log = LoggerFactory.getLogger(LiveDecoder.class);

	public enum LiveState {
		TYPE,
		LENGTH,
		CONTENT
	}

	private LiveMessage message;

	public LiveDecoder() {
		super(LiveState.TYPE);
	}

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
		LiveState state = state();
		log.debug("state:" + state + " message:" + message);
		switch (state) {
			case TYPE:
				message = new LiveMessage();
				byte type = byteBuf.readByte();
				log.debug("type：" + type);
				message.setType(type);
				checkpoint(LiveState.LENGTH);
				break;
			case LENGTH:
				int length = byteBuf.readInt();
				message.setLength(length);
				if (length > 0) {
					checkpoint(LiveState.CONTENT);
				} else {
					list.add(message);
					checkpoint(LiveState.TYPE);
				}
				break;
			case CONTENT:
				byte[] bytes = new byte[message.getLength()];
				byteBuf.readBytes(bytes);
				message.setContent(new String(bytes));
				list.add(message);
				checkpoint(LiveState.TYPE);
				break;
			default:
				throw new IllegalStateException("invalid state:" + state());
		}
		log.debug("end state:" + state + " list:" + list);
	}

}
