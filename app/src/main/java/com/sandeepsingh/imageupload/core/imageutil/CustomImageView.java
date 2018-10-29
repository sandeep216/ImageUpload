package com.sandeepsingh.imageupload.core.imageutil;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.sandeepsingh.imageupload.R;

/**
 * Created by Sandeep Singh.
 */
public class CustomImageView extends android.support.v7.widget.AppCompatImageView {

    public static final int NONE = 0;
    public static final int HEIGHT = 1;
    public static final int WIDTH = 2;

    private int ratioHeight, ratioWidth, calculationType;

    public CustomImageView(Context context, int ratioWidth, int ratioHeight, int calculationType) {
        super(context);
        setDimensionRatio(ratioWidth, ratioHeight, calculationType);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDimensionRatio(context, attrs);
      //  setup();
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDimensionRatio(context, attrs);
    }

    private void setDimensionRatio(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DimensionRatio);
        int ratioWidth = typedArray.getInteger(R.styleable.DimensionRatio_ratio_width, 0);
        int ratioHeight = typedArray.getInteger(R.styleable.DimensionRatio_ratio_height, 0);
        int calculationType = typedArray.getInteger(R.styleable.DimensionRatio_calculation_type, 0);
        setDimensionRatio(ratioWidth, ratioHeight, calculationType);
        typedArray.recycle();
    }

    private void setDimensionRatio(int ratioWidth, int ratioHeight, int calculationType) {
        this.ratioWidth = ratioWidth;
        this.ratioHeight = ratioHeight;
        this.calculationType = calculationType;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        switch (calculationType) {
            case HEIGHT:
                height = (int) ((float) (((float) (ratioWidth > 0 ? ratioWidth : 1) / (float) (ratioHeight > 0 ? ratioHeight : 1)) * width));
                break;
            case WIDTH:
                width = (int) ((float) (((float) (ratioHeight > 0 ? ratioHeight : 1) / (float) (ratioWidth > 0 ? ratioWidth : 1)) * height));
                break;
        }
        setMeasuredDimension(width, height);
    }
}
