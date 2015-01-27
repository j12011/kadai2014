package com.example.prog_gps;

import com.example.prog_gps.R.id;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ListActivity extends Activity{

	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
/*
		textView = (TextView) findViewById(id.result_text);

		// 非同期処理の実行
		AsyncTaskGetJson taskGetJson = new AsyncTaskGetJson(this);
		taskGetJson.execute();
*/
	}


}
