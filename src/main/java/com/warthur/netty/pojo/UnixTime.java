package com.warthur.netty.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by warth on 2018/3/9.
 */
@AllArgsConstructor
@Data
public class UnixTime {

	private final long value;

	public UnixTime() {
		this(System.currentTimeMillis() / 1000L + 2208988800L);
	}

	@Override
	public String toString() {
		Date date = new Date((getValue() - 2208988800L) * 1000L);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
}
