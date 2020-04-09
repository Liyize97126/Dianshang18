package com.bawei.dianshang18;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class Eating {
    @JavascriptInterface
    public void eat(String food){
        Toast.makeText(MainActivity.getContext(),"请到控制台中查看我到底吃了什么",Toast.LENGTH_LONG).show();
        Log.i("Tag","我正在吃" + food);
    }
}
