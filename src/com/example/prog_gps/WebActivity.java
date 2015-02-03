package com.example.prog_gps;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity{

	@Override public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    setContentView(R.layout.activity_web);

	    WebView webview = (WebView)this.findViewById(R.id.webView1);
	    webview.setWebViewClient( new WebViewClient() );
	    webview.loadUrl("http://j12006.sangi01.net/safety/safety_informations");
	}

}
