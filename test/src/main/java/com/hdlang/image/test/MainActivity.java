package com.hdlang.image.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hlibrary.image.ImageManager;
import com.hlibrary.image.listener.IImageDownListener;
import com.hlibrary.util.Logger;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;


public class MainActivity extends AppCompatActivity {
    private final static String URL = "http://cms-bucket.nosdn.127.net/2018/08/06/110d3bb132804ba6b1eefd8c16801ca5.jpeg";
//    private final static String URL = "http://a.hiphotos.baidu.com/image/h%3D300/sign=4a51c9cd7e8b4710d12ffbccf3ccc3b2/b64543a98226cffceee78e5eb5014a90f703ea09.jpg";
//    private final static String URL = "http://cms-bucket.nosdn.127.net/catchpic/c/c9/c9321d0844f24b2a83303502c1fe62af.gif";


    ImageManager imageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageManager = ImageManager.Companion.getInstance(this);
        imageManager.init();
        setContentView(R.layout.activity_main);
        load2();
    }

    private void load1() {
        imageManager.load(findViewById(R.id.imgvw), URL, 0, R.drawable.ic_placeholder, R.drawable.icon_failure);
    }

    private void load2() {
        imageManager.load(URL
                , this, new IImageDownListener() {


                    @Override
                    public void onDownFinish(@NotNull File file) {
                        try {
                            ImageView imageView = findViewById(R.id.imgvw);
//                            if (file.getAbsolutePath().endsWith(".gif")) {
                            GifDrawable gifDrawable = new GifDrawable(file);
                            imageView.setImageDrawable(gifDrawable);
//                            } else {
//                                imageView.setImageBitmap(bitmap);
//                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDownFinish(@NotNull Bitmap bitmap) {
                        ImageView imageView = findViewById(R.id.imgvw);
                        imageView.setImageBitmap(bitmap);
                    }


                    @Override
                    public void onError(@NotNull String msg) {
                        Logger.Companion.getInstance().defaultTagD(msg);
                    }
                });
    }

}
