package com.example.prog_gps;

import com.example.prog_gps.R.id;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends Activity{

	String login_ID="";
	String login_name="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		//テキストビューにログインユーザーIDを表示

		TextView user_ID = (TextView) findViewById(id.textView1);
		login_ID = LoginPageActivity.getID();
		login_name = LoginPageActivity.getUSERNAME();
		//user_ID.setText("ようこそ！"+login_ID+"様");
		user_ID.setText("ようこそ！"+login_name+"様");

	}
	//安否確認ボタンの動作
	public void onClick_button1(View view){
		Intent intent = new Intent(MenuActivity.this, GPSActivity.class);
	    startActivity(intent);
	}
	//リスト表示ボタンの動作
	public void onClick_button2(View view){
		//Uri uri = Uri.parse("http://j12006.sangi01.net/safety/safety_informations");
		//Intent i = new Intent(Intent.ACTION_VIEW,uri);
		//startActivity(i);
		Intent intent = new Intent(MenuActivity.this, WebActivity.class);
	    startActivity(intent);
	}

}
