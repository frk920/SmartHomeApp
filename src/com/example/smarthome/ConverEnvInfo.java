package com.example.smarthome;

import java.nio.ByteOrder;

import javolution.io.Struct;
import javolution.io.Struct.Float32;
import javolution.io.Struct.Signed32;
import javolution.io.Struct.Signed8;

public class ConverEnvInfo extends Struct{
	public final Signed32 snum = new Signed32();
	public final Float32 temperature = new Float32();
	public final Float32 humidity = new Float32();
	public final Float32 ill = new Float32();
	public final Float32 bet = new Float32();
	public final Float32 adc = new Float32();
	public final Signed8 x = new Signed8();
	public final Signed8 y = new Signed8();
	public final Signed8 z = new Signed8();
	//处理对齐问题
	@Override
	public boolean isPacked() {
		
		return true;
	}
	//设为小端数据
	@Override
	public ByteOrder byteOrder() {
		
		return ByteOrder.LITTLE_ENDIAN;
	}
}
