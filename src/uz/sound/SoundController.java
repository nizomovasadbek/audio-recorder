package uz.sound;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SoundController {

    private final int BUFFER_SIZE = 4096;
    private ByteArrayOutputStream recordBytes;
    private TargetDataLine audioLine;
    private AudioFormat audioFormat;

    private boolean isRunning;

    public AudioFormat getAudioFormat(){
        int rate = 16000;
        int sizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigIndian = true;
        return new AudioFormat(rate, sizeInBits, channels, signed, bigIndian);
    }

    public void start() throws LineUnavailableException {
        audioFormat = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

        if(!AudioSystem.isLineSupported(info)){
            throw new LineUnavailableException("Microphone doesn't exist");
        }

        audioLine = AudioSystem.getTargetDataLine(audioFormat);

        audioLine.open(audioFormat);
        audioLine.start();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;

        recordBytes = new ByteArrayOutputStream();
        isRunning = true;

        while(isRunning){
            bytesRead = audioLine.read(buffer, 0, buffer.length);
            recordBytes.write(buffer, 0, bytesRead);
        }
    }

    public void stop() throws IOException {
        isRunning = false;

        if(audioLine != null){
            audioLine.drain();
            audioLine.close();
        }
    }

    public void save(File wavFile) throws IOException {
        byte[] audioData = recordBytes.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream audioInputStream = new AudioInputStream(bais, audioFormat,
                audioData.length / audioFormat.getFrameSize());

        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);

        audioInputStream.close();
        recordBytes.close();
    }

}
