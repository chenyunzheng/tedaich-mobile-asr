package com.tedaich.mobile.asr.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.tedaich.mobile.asr.R;

public class AudioWaveView extends View {

    private static final float factor = 2.0f;

    private Paint centerLinePaint;
    private Paint wavePaint;
    private Paint ordinaryLinePaint;
    private int verticalHalfOffSet;
    private int horizontalHalfOffSet;
    private float waveCountPerPixel;
    private Short[] buf;

    private int viewWidth;
    private int viewHeight;

    public AudioWaveView(Context context) {
        super(context);
        init(context, null);
    }

    public AudioWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AudioWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setFocusable(false);
        Resources resources = getResources();
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AudioWaveView);
        verticalHalfOffSet = typedArray.getInt(R.styleable.AudioWaveView_vertical_half_offset, 0);
        horizontalHalfOffSet = typedArray.getInt(R.styleable.AudioWaveView_horizontal_half_offset, 0);
        waveCountPerPixel = typedArray.getFloat(R.styleable.AudioWaveView_wave_count_per_pixel,1.0f);
        typedArray.recycle();

        centerLinePaint = new Paint();
        centerLinePaint.setStrokeWidth(resources.getDimension(R.dimen.audio_wave_center_line_width));
        centerLinePaint.setColor(resources.getColor(R.color.app_gray_background));

        wavePaint = new Paint();
        wavePaint.setStrokeWidth(resources.getDimension(R.dimen.audio_wave_wave_width));
        wavePaint.setStrokeJoin(Paint.Join.ROUND);
        wavePaint.setAntiAlias(true);
        wavePaint.setColor(resources.getColor(R.color.colorPrimary));

        ordinaryLinePaint = new Paint();
        ordinaryLinePaint.setStrokeWidth(resources.getDimension(R.dimen.audio_wave_ordinary_line_width));
        ordinaryLinePaint.setColor(resources.getColor(R.color.app_gray_background));

        buf = new Short[0];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerLinePos = viewHeight/2.0f;
        //center line
        canvas.drawLine(horizontalHalfOffSet, centerLinePos, viewWidth - horizontalHalfOffSet, centerLinePos, centerLinePaint);
        //top line
        canvas.drawLine(horizontalHalfOffSet, verticalHalfOffSet, viewWidth - horizontalHalfOffSet, verticalHalfOffSet, ordinaryLinePaint);
        //bottom line
        canvas.drawLine(horizontalHalfOffSet, viewHeight - verticalHalfOffSet, viewWidth - horizontalHalfOffSet, viewHeight - verticalHalfOffSet, ordinaryLinePaint);

        float scaleY = (centerLinePos - verticalHalfOffSet) / Short.MAX_VALUE;
        for (int i = 0; i < buf.length; i++) {
            if (buf[i] == null){
                continue;
            }
            float distance = Math.min(Math.abs(buf[i] * scaleY * factor), centerLinePos - verticalHalfOffSet);
            float y1 = centerLinePos - distance;
            float y2 = centerLinePos + distance;
            float x = i / waveCountPerPixel + horizontalHalfOffSet;
            canvas.drawLine(x, y1, x, y2, wavePaint);
        }

    }

    public void setBuf(Short[] buf) {
        this.buf = buf;
    }

    public int getMaxWaveCount(){
        float _count = (viewWidth - 2 * horizontalHalfOffSet) * waveCountPerPixel;
        return Math.round(_count);
    }
}
