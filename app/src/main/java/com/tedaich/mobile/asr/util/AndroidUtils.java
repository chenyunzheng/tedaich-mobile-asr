package com.tedaich.mobile.asr.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.widget.Button;

import androidx.core.graphics.drawable.DrawableCompat;

public class AndroidUtils {

    private AndroidUtils() throws Throwable {
        throw new Throwable("Can't be instanced");
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
