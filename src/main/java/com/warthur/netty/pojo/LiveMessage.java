package com.warthur.netty.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by warth on 2018/3/9.
 */

@NoArgsConstructor
@Data
@ToString
public class LiveMessage {

	public static final byte TYPE_HEART = 1;
	public static final byte TYPE_MESSAGE = 2;

	private byte type;
	private int length;
	private String content;
}
