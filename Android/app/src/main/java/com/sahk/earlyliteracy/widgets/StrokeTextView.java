package com.sahk.earlyliteracy.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sahk.earlyliteracy.R;

public class StrokeTextView extends TextView {

    private TextView outlineTextView = null;
    private int strokeWidth = 15;
    private int color = Color.parseColor("#000000");

    public StrokeTextView(Context context) {
        super(context);
        outlineTextView = new TextView(context);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        outlineTextView = new TextView(context, attributeSet);
        init();
        setFontStyle(context, attributeSet);
    }

    public StrokeTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        outlineTextView = new TextView(context, attributeSet, defStyle);
        init();
        setFontStyle(context, attributeSet);
    }

    public void init() {
        TextPaint textPaint = outlineTextView.getPaint();
        textPaint.setStrokeWidth(strokeWidth);
        textPaint.setStyle(Paint.Style.STROKE);
        outlineTextView.setTextColor(color);
        outlineTextView.setGravity(getGravity());
    }

    public void setStrokeWidth(int width) {
        this.strokeWidth = width;
    }

    public void setStrokeColor(int color) {
        this.color = color;
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        outlineTextView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        CharSequence outlineText = outlineTextView.getText();
        if (outlineText == null || !outlineText.equals(this.getText())) {
            outlineTextView.setText(getText());
            postInvalidate();
        }
        outlineTextView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        outlineTextView.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getTypeface() != null) {
            outlineTextView.setTypeface(getTypeface());
        }
        outlineTextView.draw(canvas);
        super.onDraw(canvas);
    }

    @Override
    public void setTextSize(float size) {
        outlineTextView.setTextSize(size);
        super.setTextSize(size);
    }

    private void setFontStyle(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CustomTextView);
        String style = typedArray.getString(R.styleable.CustomTextView_style);
        setFontStyle(context, style);
        typedArray.recycle();
    }

    public boolean setFontStyle(Context context, String asset) {
        Typeface typeface = null;
        try {
            if (asset != null) {
                switch (asset) {
                    case "medium":
                        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/MYoungHK-Medium.otf");
                        break;
                    case "bold":
                        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/MYoungHK-Bold.otf");
                        break;
                    default:
                        typeface = Typeface.DEFAULT;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            setTypeface(typeface);
        }
        return true;
    }
}