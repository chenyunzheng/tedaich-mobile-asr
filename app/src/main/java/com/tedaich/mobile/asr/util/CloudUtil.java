package com.tedaich.mobile.asr.util;

import java.net.HttpURLConnection;

public class CloudUtil {

    private static final String LOG_TAG = "CloudUtil";

    public static void renameFile(long gUserId, long audioId, String rename){
        HttpURLConnection connection = null;
    }

    public static void deleteFile(long gUserId, long audioId, boolean logicDel) {

    }

    public static void addUserInputTransferText(long gUserId, long audioId, String text) {

    }

    public static String getLatestTransferText(long gUserId, long audioId) {
        return "hello world";
    }

    public static boolean uploadFile(long gUserId, String metadata, String filePath) {
        return false;
    }
}
