package com.tedaich.mobile.asr.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tedaich.mobile.asr.R;

public class UserItemView extends RelativeLayout {

    private Resources resources;

    public UserItemView(Context context) {
        super(context);
        init(context, null);
    }

    public UserItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public UserItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.me_item_layout, this);
        this.resources = getResources();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UserItemView);
        Drawable itemIcon = typedArray.getDrawable(R.styleable.UserItemView_left_icon);
        boolean iconShow = typedArray.getBoolean(R.styleable.UserItemView_show_left_icon, false);
        String leftText = typedArray.getString(R.styleable.UserItemView_left_text);
        String rightText = typedArray.getString(R.styleable.UserItemView_right_text);
        boolean rightArrowShow = typedArray.getBoolean(R.styleable.UserItemView_show_right_arrow, false);
        boolean bottomLineShow = typedArray.getBoolean(R.styleable.UserItemView_show_bottom_line, true);
        typedArray.recycle();

        ImageView ivItemIcon = findViewById(R.id.user_item_icon);
        TextView tvItemName = findViewById(R.id.user_item_name);
        TextView tvItemBaseInfo = findViewById(R.id.user_item_baseinfo);
        ImageView ivItemMore = findViewById(R.id.user_item_more);
        View vItemBottomLine = findViewById(R.id.user_item_bottom_line);

        ivItemIcon.setImageDrawable(itemIcon);
        ivItemIcon.setVisibility(iconShow ? VISIBLE : INVISIBLE);
        tvItemName.setText(leftText);
        tvItemBaseInfo.setText(rightText);
        ivItemMore.setVisibility(rightArrowShow ? VISIBLE : INVISIBLE);
        vItemBottomLine.setVisibility(bottomLineShow ? VISIBLE : INVISIBLE);

        tvItemName.setOnClickListener(view -> {
            String itemName = ((TextView)view).getText().toString();
            if (!itemName.equals(this.resources.getString(R.string.user_item_feedback_help_name))){
                Toast.makeText(this.getContext(), itemName, Toast.LENGTH_SHORT).show();
            }
        });
        ivItemMore.setOnClickListener(view -> {
            Toast.makeText(this.getContext(), "in developing", Toast.LENGTH_SHORT).show();
        });

    }
}
