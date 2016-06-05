package com.desmond.squarecamera;

import android.graphics.Bitmap;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

/**
 * Created by WIN10 on 2016/6/3.
 */
public class AsyncTessBase {
    private volatile TessBaseAPI baseApi = null;

    public void init(){
        baseApi = new TessBaseAPI();
        File appDir = new File(Environment.getExternalStorageDirectory().getPath(), "Boohee");
        baseApi.init(appDir.toString(), "eng");
    }

    public synchronized void end(){
        if (baseApi != null){
            baseApi.end();
            baseApi = null;
        }
    }

    public synchronized String parser(Bitmap bitmap){
        if (baseApi != null) {
            baseApi.setImage(bitmap);
            return baseApi.getUTF8Text();
        }

        return null;
    }
}
