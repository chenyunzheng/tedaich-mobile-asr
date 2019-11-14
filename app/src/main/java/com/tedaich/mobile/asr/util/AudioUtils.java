package com.tedaich.mobile.asr.util;

import java.io.File;

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

    public static String getAudioDirectory(){
        return AudioUtils.audioDirectory;
    }
}
