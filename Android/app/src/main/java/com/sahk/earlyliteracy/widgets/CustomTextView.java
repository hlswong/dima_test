package com.sahk.earlyliteracy.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sahk.earlyliteracy.R;

public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFontStyle(context, attributeSet);
    }

    public CustomTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        setFontStyle(context, attributeSet);
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
                        typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/MYoungHK-Medium.otf");
                        break;
                    case "bold":
                        typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/MYoungHK-Bold.otf");
                        break;
                    default:
                        typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/MYoungHK-Medium.otf");
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