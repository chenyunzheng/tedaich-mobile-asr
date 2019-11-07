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

import java.util.ArrayList;
import java.util.List;

public class AudioWaveView extends View {

    private Resources resources;

    private Paint centerLinePaint;
    private Paint wavePaint;
    private Paint ordinaryLinePaint;
    private int centerLineHalfOffSet;
    private int waveHalfOffSet;
    private int waveSpace;

    private List<Short> buf;

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
        this.resources = getResources();
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AudioWaveView);
        centerLineHalfOffSet = typedArray.getInt(R.styleable.AudioWaveView_center_line_half_offset, 0);
        waveHalfOffSet = typedArray.getInt(R.styleable.AudioWaveView_wave_half_offset, 0);
        typedArray.recycle();

        centerLinePaint = new Paint();
        centerLinePaint.setStrokeWidth(this.resources.getDimension(R.dimen.audio_wave_center_line_width));
        centerLinePaint.setColor(this.resources.getColor(R.color.colorAccent));

        wavePaint = new Paint();
        wavePaint.setStrokeWidth(this.resources.getDimension(R.dimen.audio_wave_wave_width));
        wavePaint.setStrokeJoin(Paint.Join.ROUND);
        wavePaint.setAntiAlias(true);
        wavePaint.setColor(this.resources.getColor(R.color.colorPrimary));

        ordinaryLinePaint = new Paint();
        ordinaryLinePaint.setStrokeWidth(this.resources.getDimension(R.dimen.audio_wave_ordinary_line_width));
        ordinaryLinePaint.setColor(this.resources.getColor(R.color.colorPrimaryDark));

        buf = new ArrayList<>();
        buf.add((short) 1000);
        buf.add((short) 1000);
        buf.add((short) 500);
        buf.add((short) 500);
        buf.add((short) 500);
        buf.add((short) 500);
        buf.add((short) 500);
        buf.add((short) 500);
        buf.add((short) 500);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        float centerLinePos = viewHeight/2.0f;

        //center line
        canvas.drawLine(waveHalfOffSet, centerLinePos, viewWidth-waveHalfOffSet, centerLinePos, centerLinePaint);
        //top line
        canvas.drawLine(waveHalfOffSet, centerLineHalfOffSet, viewWidth-waveHalfOffSet, centerLineHalfOffSet, ordinaryLinePaint);
        //bottom line
        canvas.drawLine(waveHalfOffSet, viewHeight - centerLineHalfOffSet, viewWidth-waveHalfOffSet, viewHeight - centerLineHalfOffSet, ordinaryLinePaint);
        //

        int rateY = 1;
        float divider = 10f;
        int marginRight=30;
        float rate = 5.0f;
        for (int i = 0; i < buf.size(); i++) {
            float y = centerLinePos - buf.get(i)/rate;// 调节缩小比例，调节基准线
            float x = (i) * divider;
            if(getWidth() - (i-1) * divider <= marginRight){
                x = getWidth()-marginRight;
            }
            //画线的方式很多，你可以根据自己要求去画。这里只是为了简单
            float y1 = centerLinePos + buf.get(i)/rate;
            canvas.drawLine(x, y,  x,y1, ordinaryLinePaint);//中间出波形
        }

    }

    public void setBuf(List<Short> buf) {
        this.buf = buf;
    }
}
