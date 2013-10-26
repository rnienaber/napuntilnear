package com.therandomist.nap.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

public class FontSetter {

    private static Typeface curlyFont = null;

    public static void setFont(TextView textView, AssetManager assetManager){
        if(textView != null){
            textView.setTypeface(getCurlyFont(assetManager));
        }
    }

    public static void setFont(Button button, AssetManager assetManager){
        if(button != null){
            button.setTypeface(getCurlyFont(assetManager));
        }
    }

    public static Typeface getCurlyFont(AssetManager assetManager) {
        if(curlyFont == null){
            curlyFont = Typeface.createFromAsset(assetManager, "homozio.ttf");
        }
        return curlyFont;
    }
}