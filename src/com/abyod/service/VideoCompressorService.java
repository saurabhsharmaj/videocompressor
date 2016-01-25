package com.abyod.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.abyod.utils.Utils;
import com.abyod.videocompressor.AudioAttributes;
import com.abyod.videocompressor.Encoder;
import com.abyod.videocompressor.EncoderException;
import com.abyod.videocompressor.EncoderProgressListener;
import com.abyod.videocompressor.EncodingAttributes;
import com.abyod.videocompressor.InputFormatException;
import com.abyod.videocompressor.MultimediaInfo;
import com.abyod.videocompressor.VideoAttributes;
import com.abyod.videocompressor.VideoSize;

public class VideoCompressorService {

	private volatile static double progressStatus;

	private volatile static String timeElapsed = "";
	
	EncoderProgressListener progListener = new EncoderProgressListener() {

		@Override
		public void sourceInfo(MultimediaInfo info) {
			System.out.println("info>>"+info.getFormat());

		}

		@Override
		public void progress(int permil) {
			setProgressStatus(permil);
			System.out.println("progress>>"+permil);

		}

		@Override
		public void message(String message) {
			System.out.println("message>>"+message);

		}
	};

	public File encodeTOFlv(String sourceFileName, String outputDirectory) throws IllegalArgumentException, InputFormatException, EncoderException{
		File source = new File(sourceFileName);
		File target = new File(outputDirectory + File.separator + Utils.getBaseName(source.getName())+"_converted.flv");
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
		setProgressStatus(0);
		processing(source, target, attrs, encoder);
		return target;

	}

	private void processing(File source, File target, EncodingAttributes attrs, Encoder encoder)
			throws InputFormatException, EncoderException {
		DateTime start = new DateTime(new Date());		
		encoder.encode(source, target, attrs,progListener);
		DateTime end = new DateTime(new Date());
		setTimeElapsed(target, new VideoCompressorService().caluateTimeTaken(start,end));
		System.out.println("===================> Time Elapsed : "+getTimeElapsed());
	}

	public File encodeTOWav(String sourceFileName, String outputDirectory ) throws IllegalArgumentException, InputFormatException, EncoderException{	
		File source = new File(sourceFileName);
		File target = new File(outputDirectory + File.separator+ Utils.getBaseName(source.getName())+"_converted.wav");		
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("pcm_s16le");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("wav");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		setProgressStatus(0);
		processing(source, target, attrs, encoder);
		return target;
	}

	public File encodeToMp3(String sourceFileName, String outputDirectory) throws IllegalArgumentException, InputFormatException, EncoderException{
		File source = new File(sourceFileName);
		File target = new File(outputDirectory + File.separator+Utils.getBaseName(source.getName())+".mp3");
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(128000));
		audio.setChannels(new Integer(2));
		audio.setSamplingRate(new Integer(44100));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		setProgressStatus(0);
		processing(source, target, attrs, encoder);
		return target;
	}

	public File encodeTOAvi(String sourceFileName, String outputDirectory) throws IllegalArgumentException, InputFormatException, EncoderException{
		File source = new File(sourceFileName);
		File target = new File(outputDirectory + File.separator+Utils.getBaseName(source.getName())+"_converted.avi");
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(56000));
		audio.setChannels(new Integer(1));
		audio.setSamplingRate(new Integer(22050));
		VideoAttributes video = new VideoAttributes();
		video.setCodec(VideoAttributes.DIRECT_STREAM_COPY);
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("avi");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		setProgressStatus(0);
		processing(source, target, attrs, encoder);
		return target;
	}

	public File encodeToMpeg4(String sourceFileName, String outputDirectory) throws IllegalArgumentException, InputFormatException, EncoderException{
		File source = new File(sourceFileName);
		File target = new File(outputDirectory + File.separator+Utils.getBaseName(source.getName())+"_converted.avi");
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
		setProgressStatus(0);
		processing(source, target, attrs, encoder);
		return target;
	}

	public File encodeTo3GP(String sourceFileName, String outputDirectory) throws IllegalArgumentException, InputFormatException, EncoderException{
		File source = new File(sourceFileName);
		File target = new File(outputDirectory + File.separator+Utils.getBaseName(source.getName())+"_converted.3gp");
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libfaac");
		audio.setBitRate(new Integer(128000));
		audio.setSamplingRate(new Integer(44100));
		audio.setChannels(new Integer(2));
		VideoAttributes video = new VideoAttributes();
		video.setCodec("mpeg4");
		video.setBitRate(new Integer(160000));
		video.setFrameRate(new Integer(15));
		video.setSize(new VideoSize(176, 144));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("3gp");
		attrs.setAudioAttributes(audio);
		attrs.setVideoAttributes(video);
		Encoder encoder = new Encoder();
		setProgressStatus(0);
		processing(source, target, attrs, encoder);
		return target;
	}	

	public static String[] getAvailableFormats(){
		return new String[]{"FLV","MP3","AVI","Mpeg4","3GP"};
	}

	public static Boolean deleteFile(String directory, String fileName) {
		return new File(directory, fileName).delete();

	}


	public static void setProgressStatus(long progressStatus) {
		VideoCompressorService.progressStatus = progressStatus;
	}

	public static Double getProgressStatus() {
		Double total = 1000d;
		return (progressStatus / total ) * 100;
	}

	
	public static String getTimeElapsed() {
		return timeElapsed;
	}

	public static void setTimeElapsed(File target, String timeElapsed) {
		VideoCompressorService.timeElapsed = timeElapsed;
		try {			

			File file = new File(target.getParentFile().getParentFile(), Utils.getBaseName(target.getName())+".txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(timeElapsed);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String caluateTimeTaken(DateTime startDateTime, DateTime endDateTime){

		Period period = new Period(startDateTime, endDateTime);

		PeriodFormatter formatter = new PeriodFormatterBuilder()
				.appendYears().appendSuffix(" year, ", " years, ")
				.appendMonths().appendSuffix(" month, ", " months, ")
				.appendWeeks().appendSuffix(" week, ", " weeks, ")
				.appendDays().appendSuffix(" day, ", " days, ")
				.appendHours().appendSuffix(" hour, ", " hours, ")
				.appendMinutes().appendSuffix(" minute, ", " minutes, ")
				.appendSeconds().appendSuffix(" second", " seconds")
				.printZeroNever()
				.toFormatter();		
		return formatter.print(period);

	}
}
