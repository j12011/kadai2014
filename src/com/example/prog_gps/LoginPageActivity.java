package com.example.prog_gps;

import com.example.prog_gps.R.id;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPageActivity extends Activity{

	//テキストエディタ
	EditText edUSERNAME;
	EditText edPASS;
	TextView text1;

	//パラメタ保存用
	static String USERNAME;
	String result="no data";
	String PASS="";
	//String PASS_temp="";
	static String USERID;

	String[] ID_Array;
	String[] USERNAME_Array;
	String[] PASSWORD_Array;
	static String[] MAILADDRESS_Array;
	String[] MAILPASS_Array;

	static String USERMAILADDRESS;
	static String USERMAILPASS;




	//テスト用
	TextView t;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginpage);
		edUSERNAME   = (EditText) findViewById(id.editText2);
		edPASS = (EditText) findViewById(id.editText1);
		text1 = (TextView) findViewById(id.textLog);

		edUSERNAME.setText(loadUSERNAME(this));
		edPASS.setText(loadPass(this));

		t = (TextView) findViewById(id.textView3);
		t.setVisibility(View.GONE);
		getPass();
		//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		//t.setText(result);
	}

	//ログインボタンの動作
	public void onClick_button1(View view){

		USERNAME   = edUSERNAME.getText().toString();
		PASS = edPASS.getText().toString();

		sleep(1000);

		//t.setText(result);
		//Toast.makeText(this, ID_Array, Toast.LENGTH_LONG).show();
		//Toast.makeText(this, USERNAME_Array, Toast.LENGTH_LONG).show();
		//Toast.makeText(this, PASSWORD_Array, Toast.LENGTH_LONG).show();



		if(loginCheck()){
			Intent intent = new Intent(LoginPageActivity.this, MenuActivity.class);
			startActivity(intent);
			saveIDPass( this, USERNAME, PASS );
			//text1.setText(PASS_temp);
		} else {
			Toast.makeText(this, "IDまたはパスワードが違います。", Toast.LENGTH_LONG).show();
		}

		//Intent intent = new Intent(LoginPageActivity.this, MenuActivity.class);
		//startActivity(intent);
		//saveIDPass( this, ID, PASS );
	}

	public static String getID(){
		return USERID;
	}

	public static String getUSERNAME(){
		return USERNAME;
	}

	public static String getNAME(){
		//仮実装
		String name="";
		if(USERNAME.equals("user01")){
			name = "産技太郎";
		} else if(USERNAME.equals("user02")) {
			name = "産技二郎";
		}
		else {
			name = "ゲスト";
		}
		return name;
	}
	public void getPass(){
		// 非同期処理の実行
		AsyncTaskGetJson taskGetJson = new AsyncTaskGetJson(this);
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

		for(int i=0; i < (ID_Array.length); i++){
			if(USERNAME_Array[i].equals(USERNAME)){
				if(PASSWORD_Array[i].equals(PASS)){
					USERID = ID_Array[i];
					USERMAILADDRESS = MAILADDRESS_Array[i];
					USERMAILPASS = MAILPASS_Array[i];
					return true;
				}
			}
		}
		return false;

		//仮実装
/*
		if( (PASS.equals("user01") && ID.equals("user01")) || (PASS.equals("user02")&&ID.equals("user02")) || (PASS.equals("1")&&ID.equals("1"))){
			return true;
		} else {
			return false;
		}
*/
	}

	public void saveIDPass( Context context, String name, String pass ){
	        // プリファレンスの準備 //
	        SharedPreferences pref =
	                context.getSharedPreferences( "name_and_pass", Context.MODE_PRIVATE );

	        // プリファレンスに書き込むためのEditorオブジェクト取得 //
	        Editor editor = pref.edit();

	        // "user_name" というキーで名前を登録
	        editor.putString( "user_name", name );
	        // "user_age" というキーで年齢を登録
	        editor.putString( "user_pass", pass );

	        // 書き込みの確定（実際にファイルに書き込む）
	        editor.commit();
	   }

	public String loadUSERNAME( Context context ){
        // プリファレンスの準備 //
        SharedPreferences pref =
                context.getSharedPreferences( "name_and_pass", Context.MODE_PRIVATE );

        // "user_name" というキーで保存されている値を読み出す
        return pref.getString( "user_name", "" );
    }

	public String loadPass( Context context ){
        // プリファレンスの準備 //
        SharedPreferences pref =
                context.getSharedPreferences( "name_and_pass", Context.MODE_PRIVATE );

        // "user_name" というキーで保存されている値を読み出す
        return pref.getString( "user_pass", "" );
    }

	public void setResult(String RESULT){
		result = RESULT;
	}

	public void setID_Array(String Array){
		String tmp = Array;
		ID_Array = tmp.split(",",0);
	}

	public void setUSERNAME_Array(String Array){
		String tmp = Array;
		USERNAME_Array = tmp.split(",",0);
	}

	public void setPASSWORD_Array(String Array){
		String tmp = Array;
		PASSWORD_Array = tmp.split(",",0);
	}

	public void setMAILADDRESS_Array(String Array){
		String tmp = Array;
		MAILADDRESS_Array = tmp.split(",",0);
	}

	public void setMAILPASS_Array(String Array){
		String tmp = Array;
		MAILPASS_Array = tmp.split(",",0);
	}

	public static String[] getMAILADDRES(){
		return MAILADDRESS_Array;
	}

	public static String getUSERMAILADDRES(){
		return USERMAILADDRESS;
	}

	public static String getUSERMAILPASS(){
		return USERMAILPASS;
	}

	public synchronized void sleep(long msec)
	{	//指定ミリ秒実行を止めるメソッド
		try
		{
			wait(msec);
		}catch(InterruptedException e){}
	}
}
