package com.zimny.socialfood.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.zimny.socialfood.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ideo7 on 07.09.2017.
 */

public class MultiCircleView extends LinearLayout {

    private Integer items;
    private int backgroundColorIcon;
    private int iconColor;
    private Drawable defaultIcon;
    private Drawable icon1;
    private Drawable icon2;
    private Drawable icon3;
    private int backgroundColorText;
    private int textColor;
    private String text;
    private int defaultBackgroundColorIcon = Color.WHITE;
    private int defaultBackgroundColorText = ContextCompat.getColor(getContext(), R.color.primary_dark);
    private int defaultTextColor = ContextCompat.getColor(getContext(), R.color.primary);
    private int defaultBorderColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
    private int defaultColorIcon = ContextCompat.getColor(getContext(), R.color.primary);
    private Drawable defaultBackgroundIcon = ContextCompat.getDrawable(getContext(), R.drawable.circle_button);
    private int borderColorIcon;
    private int borderWidthIcon;
    private int borderColorText;
    private int borderWidthText;


    @BindView(R.id.circle1)
    CircleImageView circle1;
    @Nullable
    @BindView(R.id.circle2)
    CircleImageView circle2;
    @Nullable
    @BindView(R.id.circle3)
    CircleImageView circle3;
    @Nullable
    @BindView(R.id.circle4)
    TextView circle4;


    public MultiCircleView(Context context) {
        super(context);
        init(context);
    }

    public MultiCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);
    }

    public MultiCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MultiCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttrs(context, attrs, defStyleAttr, defStyleRes);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        parseTypedArray(context, context.getTheme().obtainStyledAttributes(attrs, R.styleable.MultiCircleView, 0, 0));
    }

    private void parseAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        parseTypedArray(context, context.getTheme().obtainStyledAttributes(attrs, R.styleable.MultiCircleView, defStyleAttr, 0));
    }

    private void parseAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        parseTypedArray(context, context.getTheme().obtainStyledAttributes(attrs, R.styleable.MultiCircleView, defStyleAttr, defStyleRes));
    }

    private void parseTypedArray(Context context, TypedArray typedArray) {
        try {
            items = typedArray.getInteger(R.styleable.MultiCircleView_items, 1);
            defaultIcon = typedArray.getDrawable(R.styleable.MultiCircleView_defaultIcon);
            iconColor = typedArray.getColor(R.styleable.MultiCircleView_iconColor, defaultColorIcon);
            backgroundColorIcon = typedArray.getColor(R.styleable.MultiCircleView_backgroundColorIcon, defaultBackgroundColorIcon);
            text = typedArray.getString(R.styleable.MultiCircleView_text);
            backgroundColorText = typedArray.getColor(R.styleable.MultiCircleView_backgroundColorText, defaultBackgroundColorText);
            textColor = typedArray.getColor(R.styleable.MultiCircleView_textColor, defaultTextColor);
            icon1 = typedArray.getDrawable(R.styleable.MultiCircleView_icon1);
            icon2 = typedArray.getDrawable(R.styleable.MultiCircleView_icon2);
            icon3 = typedArray.getDrawable(R.styleable.MultiCircleView_icon3);
            borderColorIcon = typedArray.getColor(R.styleable.MultiCircleView_borderColorIcon, defaultBorderColor);
            borderWidthIcon = typedArray.getInteger(R.styleable.MultiCircleView_borderWidthIcon, 4);
            borderColorText = typedArray.getColor(R.styleable.MultiCircleView_borderColorText, defaultTextColor);
            borderWidthText = typedArray.getInteger(R.styleable.MultiCircleView_borderWidthText, 4);
        } catch (Exception ex) {
            XLog.d(ex.getLocalizedMessage());
        } finally {
            typedArray.recycle();
            init(context);
        }
    }

    public void init(Context context) {
        if (items > 0) {
            switch (items) {
                case 1:
                    oneCircle(context);
                    break;
                case 2:
                    twoCircle(context);
                    break;
                case 3:
                    threeCircle(context);
                    break;
                default:
                    fourCircle(context);
                    break;
            }
        }

        invalidate();

    }

    public void oneCircle(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_circle_view, this);
        ButterKnife.bind(this, view);
        if (circle1 != null && defaultIcon != null) {
            circle1.setBorderColor(borderColorIcon);
            circle1.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorIcon, PorterDuff.Mode.SRC_ATOP));
            circle1.setBackground(defaultBackgroundIcon);
            circle1.setImageDrawable(defaultIcon);
        }
    }

    public void twoCircle(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.two_circle_view, this);
        ButterKnife.bind(this, view);
        if (circle1 != null && defaultIcon != null) {
            circle1.setBorderColor(borderColorIcon);
            circle1.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorIcon, PorterDuff.Mode.SRC_ATOP));
            circle1.setBackground(defaultBackgroundIcon);
            circle1.setImageDrawable(defaultIcon);
        }
        if (circle2 != null && defaultIcon != null) {
            circle2.setBorderColor(borderColorIcon);
            circle2.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorIcon, PorterDuff.Mode.SRC_ATOP));
            circle2.setBackground(defaultBackgroundIcon);
            circle2.setImageDrawable(defaultIcon);
        }
    }

    public void threeCircle(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.three_circle_view, this);
        ButterKnife.bind(this, view);
        if (circle1 != null && defaultIcon != null) {
            circle1.setBorderColor(borderColorIcon);
            circle1.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorIcon, PorterDuff.Mode.SRC_ATOP));
            circle1.setBackground(defaultBackgroundIcon);
            circle1.setImageDrawable(defaultIcon);
        }
        if (circle2 != null && defaultIcon != null) {
            circle2.setBorderColor(borderColorIcon);
            circle2.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorIcon, PorterDuff.Mode.SRC_ATOP));
            circle2.setBackground(defaultBackgroundIcon);
            circle2.setImageDrawable(defaultIcon);
        }
        if (circle3 != null && defaultIcon != null) {
            circle3.setBorderColor(borderColorIcon);
            circle3.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorIcon, PorterDuff.Mode.SRC_ATOP));
            circle3.setBackground(defaultBackgroundIcon);
            circle3.setImageDrawable(defaultIcon);
        }
    }

    public void fourCircle(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.multi_circle_view, this);
        ButterKnife.bind(this, view);
        if (circle1 != null && defaultIcon != null) {
            circle1.setBorderColor(borderColorIcon);
            circle1.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorIcon, PorterDuff.Mode.SRC_ATOP));
            circle1.setBackground(defaultBackgroundIcon);
            circle1.setImageDrawable(defaultIcon);
        }
        if (circle2 != null && defaultIcon != null) {
            circle2.setBorderColor(borderColorIcon);
            circle2.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorIcon, PorterDuff.Mode.SRC_ATOP));
            circle2.setBackground(defaultBackgroundIcon);
            circle2.setImageDrawable(defaultIcon);
        }
        if (circle3 != null && defaultIcon != null) {
            circle3.setBorderColor(borderColorIcon);
            circle3.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorIcon, PorterDuff.Mode.SRC_ATOP));
            circle3.setBackground(defaultBackgroundIcon);
            circle3.setImageDrawable(defaultIcon);
        }
        if (circle4 != null) {
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorText, PorterDuff.Mode.SRC_ATOP));
            circle4.setBackground(defaultBackgroundIcon);
            circle4.setTextColor(textColor);
            if (text != null) {
                circle4.setText(text);
            } else {
                circle4.setText(String.valueOf(items.intValue() - 3));
            }
        }

    }


}
