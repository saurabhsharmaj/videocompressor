package com.abyod.videocompressor;

import java.io.File;

public class App {

	public static void main(String[] args) throws IllegalArgumentException, InputFormatException, EncoderException {
		/**/
		method1("D:\\jagdish\\Wildlife.wmv", "D:\\jagdish\\compressed_1.flv");
		System.out.println("================Done ================");

	}
	
	public static void method1(String sourceFile,String newFileName) throws IllegalArgumentException, InputFormatException, EncoderException{
		File source = new File(sourceFile);
		File target = new File(newFileName);
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(64000));
		audio.setChannels(new Integer(1));
		audio.setSamplingRate(new Integer(22050));
		VideoAttributes video = new VideoAttributes();
		video.setCodec("flv");
		video.setBitRate(new Integer(160000));
		video.setFrameRate(new Integer(15));
		video.setSize(new VideoSize(400, 300));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("flv");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		encoder.encode(source, target, attrs);
	}
	public static void aviTOMp4(String sourceFile,String newFileName) throws IllegalArgumentException, InputFormatException, EncoderException{
		File source = new File(sourceFile);
		File target = new File(newFileName);
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libvorbis");
		VideoAttributes video = new VideoAttributes();
		video.setCodec("mpeg4");
		video.setTag("DIVX");
		video.setBitRate(new Integer(160000));
		video.setFrameRate(new Integer(30));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mpegvideo");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		encoder.encode(source, target, attrs);
	}

}
