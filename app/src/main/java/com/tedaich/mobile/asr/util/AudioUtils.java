package com.tedaich.mobile.asr.util;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

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

    /**
     * convert pcm audio to wav format
     * @param pcmPath
     * @param wavPath
     * @param deletePcm
     * @throws Exception
     */
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
        if (deletePcm){
            File pcmFile = new File(pcmPath);
            pcmFile.delete();
        }
    }

    /**
     * get recorder timer show text
     * @param milliSec
     * @return
     */
    public static String getTimerValue(long milliSec) {
        int totalSec = (int)(milliSec/1000);
        int sec = totalSec%60;
        int min = totalSec/60%60;
        int hour = totalSec/60/60%60;
        return String.format(Locale.getDefault(),"%02d:%02d:%02d", hour, min, sec);
    }

    /**
     * Read audio file duration.
     * @param file audio file
     * @return Duration in microseconds.
     */
    public static long readAudioDuration(File file) {
        try {
            MediaExtractor extractor = new MediaExtractor();
            MediaFormat format = null;
            int i;
            extractor.setDataSource(file.getPath());
            int numTracks = extractor.getTrackCount();
            // find and select the first audio track present in the file.
            for (i = 0; i < numTracks; i++) {
                format = extractor.getTrackFormat(i);
                if (format.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                    extractor.selectTrack(i);
                    break;
                }
            }
            if (i == numTracks) {
                throw new IOException("No audio track found in " + file.toString());
            }
            if (format != null) {
                return format.getLong(MediaFormat.KEY_DURATION);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "error in readAudioDuration()", e);
        }
        return -1;
    }
}
