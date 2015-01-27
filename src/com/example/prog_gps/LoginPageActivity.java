package com.example.prog_gps;

import com.example.prog_gps.R.id;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPageActivity extends Activity{

	//テキストエディタ
	EditText edID;
	EditText edPASS;
	TextView text1;
	//パラメタ保存用
	static String ID;
	String PASS="";
	String PASS_temp="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginpage);
		edID   = (EditText) findViewById(id.editText2);
		edPASS = (EditText) findViewById(id.editText1);
		text1 = (TextView) findViewById(id.textLog);

		edID.setText(loadID(this));
		edPASS.setText(loadPass(this));
	}

	//ログインボタンの動作
	public void onClick_button1(View view){

		ID   = edID.getText().toString();
		PASS = edPASS.getText().toString();

		if(loginCheck()){
			Intent intent = new Intent(LoginPageActivity.this, MenuActivity.class);
			startActivity(intent);
			saveIDPass( this, ID, PASS );
			//text1.setText(PASS_temp);
		} else {
			Toast.makeText(this, "IDまたはパスワードが違います。", Toast.LENGTH_LONG).show();
		}

		//Intent intent = new Intent(LoginPageActivity.this, MenuActivity.class);
		//startActivity(intent);
		//saveIDPass( this, ID, PASS );
	}

	public static String getID(){
		return ID;
	}

	public static String getNAME(){
		//仮実装
		String name="";
		if(ID.equals("user01")){
			name = "産技太郎";
		} else if(ID.equals("user02")) {
			name = "産技二郎";
		}
		return name;
	}
	public void getPass(){
		// 非同期処理の実行
		AsyncTaskGetJson2 taskGetJson = new AsyncTaskGetJson2(this);
		taskGetJson.execute();
	}
	private boolean loginCheck(){
		//ログイン用　ログイン成功：true　ログイン失敗:false
		//String pass = AsyncTaskGetJson.getSQL(ID);
		//getPass();
		//return true;
		/*
		if(pass.equals(PASS)){
			return true;
		} else {
			return false;
		}
		*/
		//仮実装
		if( (PASS.equals("user01") && ID.equals("user01")) || (PASS.equals("user02")&&ID.equals("user02"))){
			return true;
		} else {
			return false;
		}
	}

	public void saveIDPass( Context context, String id, String pass ){
	        // プリファレンスの準備 //
	        SharedPreferences pref =
	                context.getSharedPreferences( "id_and_pass", Context.MODE_PRIVATE );

	        // プリファレンスに書き込むためのEditorオブジェクト取得 //
	        Editor editor = pref.edit();

	        // "user_name" というキーで名前を登録
	        editor.putString( "user_id", id );
	        // "user_age" というキーで年齢を登録
	        editor.putString( "user_pass", pass );

	        // 書き込みの確定（実際にファイルに書き込む）
	        editor.commit();
	   }

	public String loadID( Context context ){
        // プリファレンスの準備 //
        SharedPreferences pref =
                context.getSharedPreferences( "id_and_pass", Context.MODE_PRIVATE );

        // "user_name" というキーで保存されている値を読み出す
        return pref.getString( "user_id", "" );
    }

	public String loadPass( Context context ){
        // プリファレンスの準備 //
        SharedPreferences pref =
                context.getSharedPreferences( "id_and_pass", Context.MODE_PRIVATE );

        // "user_name" というキーで保存されている値を読み出す
        return pref.getString( "user_pass", "" );
    }

	public synchronized void sleep(long msec)
	{	//指定ミリ秒実行を止めるメソッド
		try
		{
			wait(msec);
		}catch(InterruptedException e){}
	}
}
