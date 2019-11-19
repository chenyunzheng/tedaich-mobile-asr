package com.tedaich.mobile.asr.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.widget.Button;

import androidx.core.graphics.drawable.DrawableCompat;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}
