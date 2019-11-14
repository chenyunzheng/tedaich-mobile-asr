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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private AndroidUtils() throws Throwable {
        throw new Throwable("Can't be instanced");
    }

    public static String formatDate(Date date){
        return sdf.format(date);
    }

    public static byte[] convertShortToByteArray(short value){
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(value);
        return buffer.array();
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
