package com.zimny.socialfood.view;

import android.content.Context;
import android.content.res.TypedArray;
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
    private Integer itemsCount;
    private int backgroundColorIcon;
    private int iconColor;
    private Drawable defaultIcon;
    private Drawable icon1;
    private Drawable icon2;
    private Drawable icon3;
    private int backgroundColorText;
    private int textColor;
    private String text;
    private int defaultBackgroundColor = ContextCompat.getColor(getContext(), R.color.primary_dark);
    private int defaultTextColor = ContextCompat.getColor(getContext(), R.color.primary);
    private int defaultBorderColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
    private int defaultColorIcon = ContextCompat.getColor(getContext(), R.color.primary);
    private Drawable defaultBackgroundIcon = ContextCompat.getDrawable(getContext(), R.drawable.circle_button);
    private int borderColorIcon;
    private int borderWidthIcon;
    private int borderColorText;
    private int borderWidthText;


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
            itemsCount = typedArray.getInteger(R.styleable.MultiCircleView_itemsCount, 1);
            defaultIcon = typedArray.getDrawable(R.styleable.MultiCircleView_defaultIcon);
            iconColor = typedArray.getColor(R.styleable.MultiCircleView_iconColor, defaultColorIcon);
            backgroundColorIcon = typedArray.getColor(R.styleable.MultiCircleView_backgroundColorIcon, defaultBackgroundColor);
            text = typedArray.getString(R.styleable.MultiCircleView_text);
            backgroundColorText = typedArray.getColor(R.styleable.MultiCircleView_backgroundColorText, defaultBackgroundColor);
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

    public CircleImageView getCircle1() {
        return circle1;
    }

    public void setCircle1(CircleImageView circle1) {
        this.circle1 = circle1;
    }

    @Nullable
    public CircleImageView getCircle2() {
        return circle2;
    }

    public void setCircle2(@Nullable CircleImageView circle2) {
        this.circle2 = circle2;
    }

    @Nullable
    public CircleImageView getCircle3() {
        return circle3;
    }

    public void setCircle3(@Nullable CircleImageView circle3) {
        this.circle3 = circle3;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
        init(getContext());
    }

    public int getBackgroundColorIcon() {
        return backgroundColorIcon;
    }

    public void setBackgroundColorIcon(int backgroundColorIcon) {
        this.backgroundColorIcon = backgroundColorIcon;
        init(getContext());
    }

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
        init(getContext());
    }

    public Drawable getIcon1() {
        return icon1;
    }

    public void setIcon1(Drawable icon1) {
        this.icon1 = icon1;
        init(getContext());
    }

    public Drawable getIcon2() {
        return icon2;
    }

    public void setIcon2(Drawable icon2) {
        this.icon2 = icon2;
        init(getContext());
    }

    public Drawable getIcon3() {
        return icon3;
    }

    public void setIcon3(Drawable icon3) {
        this.icon3 = icon3;
        init(getContext());
    }

    public int getBackgroundColorText() {
        return backgroundColorText;
    }

    public void setBackgroundColorText(int backgroundColorText) {
        this.backgroundColorText = backgroundColorText;
        init(getContext());
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        init(getContext());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        init(getContext());
    }

    public int getBorderColorIcon() {
        return borderColorIcon;
    }

    public void setBorderColorIcon(int borderColorIcon) {
        this.borderColorIcon = borderColorIcon;
        init(getContext());
    }

    public int getBorderWidthIcon() {
        return borderWidthIcon;
    }

    public void setBorderWidthIcon(int borderWidthIcon) {
        this.borderWidthIcon = borderWidthIcon;
        init(getContext());
    }

    public int getBorderColorText() {
        return borderColorText;
    }

    public void setBorderColorText(int borderColorText) {
        this.borderColorText = borderColorText;
        init(getContext());
    }

    public int getBorderWidthText() {
        return borderWidthText;
    }

    public void setBorderWidthText(int borderWidthText) {
        this.borderWidthText = borderWidthText;
        init(getContext());
    }

    private void init(Context context) {
        removeAllViews();
        if (itemsCount > 0) {
            switch (itemsCount) {
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

    private void oneCircle(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_circle_view, this);
        ButterKnife.bind(this, view);
        if (circle1 != null && defaultIcon != null) {
            circle1.setBorderColor(borderColorIcon);
            circle1.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            circle1.setBackgroundColor(backgroundColorIcon);
            if (icon1 != null) {
                circle1.setImageDrawable(icon1);
            } else {
                circle1.setImageDrawable(defaultIcon);
            }
        }
    }

    private void twoCircle(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.two_circle_view, this);
        ButterKnife.bind(this, view);
        if (circle1 != null && defaultIcon != null) {
            circle1.setBorderColor(borderColorIcon);
            circle1.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorIcon, PorterDuff.Mode.SRC_ATOP));
            circle2.setBackground(defaultBackgroundIcon);
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

    private void threeCircle(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.three_circle_view, this);
        ButterKnife.bind(this, view);
        if (circle1 != null && defaultIcon != null) {
            circle1.setBorderColor(borderColorIcon);
            circle1.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            circle1.setBackgroundColor(backgroundColorIcon);
            circle1.setImageDrawable(defaultIcon);
        }
        if (circle2 != null && defaultIcon != null) {
            circle2.setBorderColor(borderColorIcon);
            circle2.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            circle2.setBackgroundColor(backgroundColorIcon);
            circle2.setImageDrawable(defaultIcon);
        }
        if (circle3 != null && defaultIcon != null) {
            circle3.setBorderColor(borderColorIcon);
            circle3.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            circle3.setBackgroundColor(backgroundColorIcon);
            circle3.setImageDrawable(defaultIcon);
        }
    }

    private void fourCircle(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.multi_circle_view, this);
        ButterKnife.bind(this, view);
        if (circle1 != null && defaultIcon != null) {
            circle1.setBorderColor(borderColorIcon);
            circle1.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            circle1.setBackgroundColor(backgroundColorIcon);
            circle1.setImageDrawable(defaultIcon);
        }
        if (circle2 != null && defaultIcon != null) {
            circle2.setBorderColor(borderColorIcon);
            circle2.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            circle2.setBackgroundColor(backgroundColorIcon);
            circle2.setImageDrawable(defaultIcon);
        }
        if (circle3 != null && defaultIcon != null) {
            circle3.setBorderColor(borderColorIcon);
            circle3.setBorderWidth(borderWidthIcon);
            defaultIcon.mutate().setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP));
            circle3.setBackgroundColor(backgroundColorIcon);
            circle3.setImageDrawable(defaultIcon);
        }
        if (circle4 != null && defaultIcon != null) {
            defaultBackgroundIcon.mutate().setColorFilter(new PorterDuffColorFilter(backgroundColorText, PorterDuff.Mode.SRC_ATOP));
            circle4.setBackground(defaultBackgroundIcon);
            circle4.setTextColor(textColor);
            if (text != null) {
                circle4.setText(text);
            } else {
                circle4.setText(String.valueOf(itemsCount.intValue() - 3));
            }
        }

    }


}
