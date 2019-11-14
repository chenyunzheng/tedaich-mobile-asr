package com.tedaich.mobile.asr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AudioUtils {

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

    public static void convertPCMToWAV(String pcmPath, String wavPath) throws Exception {
        FileInputStream fis = new FileInputStream(pcmPath);
        FileOutputStream fos = new FileOutputStream(wavPath);
        byte[] buf = new byte[1024 * 1000];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();



    }

}
