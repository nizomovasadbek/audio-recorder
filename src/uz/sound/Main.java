package uz.sound;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.IOException;

public class Main {

    private static final int TIME = 10000; //10 sek

    public static void main(String[] args) {
        File wavFile = new File("audio.wav");
        SoundController audioRecorder = new SoundController();

        Thread audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("yozilmoqda...");
                    audioRecorder.start();
                }catch (LineUnavailableException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        });

        audioThread.start();

        try{
            Thread.sleep(TIME);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        try{
            audioRecorder.stop();
            audioRecorder.save(wavFile);
            System.out.println("To'xtatildi");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("DONE");
    }
}
