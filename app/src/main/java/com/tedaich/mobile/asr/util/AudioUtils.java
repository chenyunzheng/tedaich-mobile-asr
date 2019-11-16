package com.tedaich.mobile.asr.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AudioUtils {

    private static final String LOG_TAG = "AudioUtils";

    private static File filesDir = null;
    private static String audioDirectory = null;

    private AudioUtils() throws Throwable {
        throw new Throwable("Can't be instanced");
    }

    /**
     * set file directory
     * @param filesDir
     */
    public static void setFilesDir(File filesDir){
        AudioUtils.filesDir = filesDir;
    }

    /**
     * create audio directory
     * @param audioDirName
     */
    public static void createAudioDirectory(String audioDirName) {
        AudioUtils.audioDirectory = filesDir.getPath() + File.separatorChar + audioDirName;
        File dirFile = new File(AudioUtils.audioDirectory);
        if (!dirFile.exists()){
            dirFile.mkdirs();
        }
    }

    /**
     * get audio directory
     * @return
     */
    public static String getAudioDirectory(){
        return AudioUtils.audioDirectory;
    }

    private static byte[] getWAVHeader(int sampleRate, int numChannels, int numSamples){
        return null;
    }

    public static void convertPCMToWAV(String pcmPath, String wavPath, boolean deletePcm) throws Exception {
        int pcmByteSize = 0;
        try(FileInputStream fis = new FileInputStream(pcmPath)){
            byte[] buffer = new byte[1024 * 5];
            int size;
            while ((size = fis.read(buffer)) != -1){
                pcmByteSize += size;
            }
        } catch (Exception e){
            Log.e(LOG_TAG, "error in reading pcm audio - " + pcmPath, e);
            throw e;
        }
        WavHeader header = new WavHeader(16000,1,2,pcmByteSize);
        try(FileInputStream fis = new FileInputStream(pcmPath);FileOutputStream fos = new FileOutputStream(wavPath)){
            //write wav header
            byte[] wavHeader = header.getWavHeader();
            fos.write(wavHeader, 0, wavHeader.length);
            fos.flush();
            byte[] buffer = new byte[1024 * 5];
            int size;
            while ((size = fis.read(buffer)) != -1){
                fos.write(buffer, 0, size);
            }
            fos.flush();
        } catch (Exception e){
            Log.e(LOG_TAG, "error in writing to wav audio - " + wavPath, e);
            throw e;
        }
        File pcmFile = new File(pcmPath);
        if (deletePcm){
            pcmFile.delete();
        }
    }

}
