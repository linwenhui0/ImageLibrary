package com.hdlang.image.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hlibrary.image.ImageManager;

public class MainActivity extends AppCompatActivity {

    ImageManager imageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageManager = ImageManager.Companion.getInstance(this);
        imageManager.init();
        setContentView(R.layout.activity_main);
        imageManager.load(findViewById(R.id.imgvw),
                "http://a.hiphotos.baidu.com/image/h%3D300/sign=4a51c9cd7e8b4710d12ffbccf3ccc3b2/b64543a98226cffceee78e5eb5014a90f703ea09.jpg",
                0, R.drawable.ic_placeholder, R.drawable.icon_failure
        );
    }
}
