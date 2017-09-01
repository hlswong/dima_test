package com.sahk.earlyliteracy.font;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.LruCache;

public class TypefaceManager {

    private static final String ROMAN_MEDIUM = "MYoungHK-Medium.otf";
    private static final String ROMAN_BOLD = "MYoungHK-bold.otf";

    private final LruCache<String, Typeface> lruCache;
    private final AssetManager assetManager;

    public TypefaceManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        lruCache = new LruCache<>(3);
    }

    private Typeface getTypeface(String fileName) {
        Typeface typeface = lruCache.get(fileName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(assetManager, "fonts/" + fileName);
            lruCache.put(fileName, typeface);
        }
        return typeface;
    }

    public Typeface getYoungBold() {
        return getTypeface(ROMAN_BOLD);
    }

    public Typeface getYoungMedium() {
        return getTypeface(ROMAN_MEDIUM);
    }
}