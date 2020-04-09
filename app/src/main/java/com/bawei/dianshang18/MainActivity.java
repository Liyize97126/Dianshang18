package com.bawei.dianshang18;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //定义
    private WebView webv;
    private ActionBar actionBar;
    private static Context context;
    public static Context getContext() {
        return context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取Context
        context = this;
        //实例化ActionBar
        actionBar = getSupportActionBar();
        //获取ID
        webv = findViewById(R.id.webv);
        //加载网页（assets目录下的网页）
        webv.loadUrl("file:///android_asset/webshow.html");
        //应用内打开
        webv.setWebViewClient(new WebViewClient());
        webv.setWebChromeClient(new WebChromeClient(){
            //标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                actionBar.setTitle(title);
            }
            //处理JSAlert消息
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                result.cancel();//每次取消掉，下次才可以继续提示
                return true;
            }
        });
        //启用JS
        WebSettings settings = webv.getSettings();
        settings.setJavaScriptEnabled(true);
        //webView中JS按钮关联Java对象
        webv.addJavascriptInterface(this,"android_js");
        webv.addJavascriptInterface(new Eating(),"eating");
    }
    //加载JS
    public void showJs(View view) {
        webv.loadUrl("javascript:toast()");
    }
    //showToast()
    //需要加一个注解，这个注解加了之后，html中js方法才能调用到java里面的方法
    @JavascriptInterface
    public void showToast(){
        Toast.makeText(context, "Js调用了Java方法", Toast.LENGTH_LONG).show();
    }
    //加载带返回值的JS
    public void showJsFan(View view) {
        //版本大于4.4才能调用带返回值的方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //调用网页中的js方法，有返回值
            webv.evaluateJavascript("javascript:fanshow(5)", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //获取返回值
                    Log.i("Tag","得到的返回值为：" + value);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("得到的返回值为：" + value);
                    builder.create();
                    builder.show();
                }
            });
        } else {
            Toast.makeText(context, "当前系统版本不支持返回值方法回调！", Toast.LENGTH_LONG).show();
        }
    }
}
