package com.hlibrary.image.fresco;

import android.content.Context;

/**
 * Created by danni on 2016/7/9.
 */
public class ImageManager {
    private static FrescoAction _imp;

    public static FrescoAction getFrescoAction(Context context) {
        if (_imp == null)
            synchronized (ImageManager.class) {
                if (_imp == null)
                    _imp = new FrescoImp(context);
            }
        return _imp;
    }

}
