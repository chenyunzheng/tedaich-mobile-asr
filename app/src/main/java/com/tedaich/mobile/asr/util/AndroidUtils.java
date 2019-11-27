package com.tedaich.mobile.asr.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.text.Html;
import android.text.Spanned;
import android.widget.Button;

import androidx.core.graphics.drawable.DrawableCompat;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class AndroidUtils {

    private static final String LOG_TAG = "AndroidUtils";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private AndroidUtils() throws Throwable {
        throw new Throwable("Can't be instanced");
    }

    /**
     * format date into yyyy-MM-dd HH:mm:ss.SSS
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        return sdf.format(date);
    }

    /**
     * format date into yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDateWithoutMillisec(Date date){
        String dateStr = formatDate(date);
        return dateStr.substring(0, dateStr.lastIndexOf("."));
    }

    /**
     * convert short to byte array
     * @param value
     * @return
     */
    public static byte[] convertShortToByteArray(short value){
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(value);
        return buffer.array();
    }

    /**
     * Convert density independent pixels value (dip) into pixels value (px).
     * @param dp Value needed to convert
     * @return Converted value in pixels.
     */
    public static float dpToPx(int dp) {
        return dpToPx((float) dp);
    }

    /**
     * Convert density independent pixels value (dip) into pixels value (px).
     * @param dp Value needed to convert
     * @return Converted value in pixels.
     */
    public static float dpToPx(float dp) {
        return (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Convert pixels value (px) into density independent pixels (dip).
     * @param px Value needed to convert
     * @return Converted value in pixels.
     */
    public static float pxToDp(int px) {
        return pxToDp((float) px);
    }

    /**
     * Convert pixels value (px) into density independent pixels (dip).
     * @param px Value needed to convert
     * @return Converted value in pixels.
     */
    public static float pxToDp(float px) {
        return (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int convertMillsToPx(long mills, float pxPerSec) {
        // 1000 is 1 second evaluated in milliseconds
        return (int) (mills * pxPerSec / 1000);
    }

    public static int convertPxToMills(long px, float pxPerSecond) {
        return (int) (1000 * px / pxPerSecond);
    }

    /**
     * get screen width
     * @return
     */
    public static int screenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * get screen height
     * @return
     */
    public static int screenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * add icon on button
     * @param button
     * @param iconDrawable
     * @param iconColor
     * @param text
     */
    public static void addButtonIcon(Button button, Integer iconDrawable, Integer iconColor, String text){
        final Resources resources = button.getResources();
        Html.ImageGetter imgGetter = source -> {
            Drawable drawable = resources.getDrawable(Integer.parseInt(source));
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            if (iconColor != null){
                final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                wrappedDrawable.mutate();
                DrawableCompat.setTint(wrappedDrawable, resources.getColor(iconColor));
            }
            return drawable;
        };
        Spanned span = Html.fromHtml("<img src=\"" + iconDrawable + "\"/>" + text, imgGetter, null);
        button.setText(span);
    }

    /**
     * see com.android.internal.util.ConcurrentUtils#newFixedThreadPool()
     */
    public static ExecutorService newFixedThreadPool(int nThreads, String poolName,
                                                     int linuxThreadPriority) {
        return Executors.newFixedThreadPool(nThreads,
                new ThreadFactory() {
                    private final AtomicInteger threadNum = new AtomicInteger(0);

                    @Override
                    public Thread newThread(final Runnable r) {
                        return new Thread(poolName + threadNum.incrementAndGet()) {
                            @Override
                            public void run() {
                                Process.setThreadPriority(linuxThreadPriority);
                                r.run();
                            }
                        };
                    }
                });
    }

    /**
     * get app version
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        String versionName;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "N/A";
        }
        return versionName;
    }

}
