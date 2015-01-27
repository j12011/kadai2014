package com.example.prog_gps;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.example.prog_gps.R;
import com.example.prog_gps.R.id;
import com.google.android.gms.internal.ay;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class GPSActivity extends FragmentActivity implements LocationListener{

	// = 0 の部分は、適当な値に変更してください（とりあえず試すには問題ないですが）
	//音声入力用
    private static final int REQUEST_CODE = 0;

	//GPSを取得するための用意
	LocationManager locmanager;
	Location location;
	private GoogleMap mMap;
	LatLng latlng = new LatLng(35.710065, 139.8107);

	//テキストビュー、ボタン、テキストエディタ、ラジオボタン、ラジオグループの用意
	TextView lati, longi;
	EditText et_comment;
	Button b1;
	RadioGroup  rg;
	RadioButton rd, rd0, rd1, rd2;

	//DBに送るパラメータ保存用
	String lat, lng, user_ID, status, comment, nowtime;

	//DBを動かすための.phpファイルがある場所のURL
	//private static final String url = "http://10.230.0.89/gps.php";
	//private static final String url = "http://192.168.1.87/gps.php";
	private static final String url = "http://j12006.sangi01.net/gps.php";

	//カメラ用
	 private Uri fileUri;
	 private Bitmap bm;

//---------------------------------------------



//----------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		setContentView(R.layout.activity_gps);

		//それぞれ初期化
		locmanager = (LocationManager)getSystemService(LOCATION_SERVICE);
		location = locmanager.getLastKnownLocation("gps");

		lati = (TextView)findViewById(R.id.textView1);
		longi = (TextView)findViewById(R.id.textView2);
		locmanager.requestLocationUpdates(locmanager.GPS_PROVIDER, 0, 0, this);

		et_comment = (EditText) findViewById(id.editText1);
		user_ID = LoginPageActivity.getID();
		nowtime = "";

		//ラジオボックス・ラジオボタン初期化
		rg  = (RadioGroup)  findViewById(id.radioGroup1);
		rd  = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
		rd0 = (RadioButton) findViewById(id.radio0);
		rd1 = (RadioButton) findViewById(id.radio1);
		rd2 = (RadioButton) findViewById(id.radio2);

		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            // ラジオグループのチェック状態が変更された時に呼び出されます
            // チェック状態が変更されたラジオボタンのIDが渡されます
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rd = (RadioButton) findViewById(checkedId);
            }
        });

		//登録ボタンの動作
		b1 = (Button)findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateMap();
				nowtime = getNowTime();
				comment = et_comment.getText().toString();
				getStatus();


				//DBへのデータ送信
				//-----[クライアント設定]
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
				//-----[POST送信するデータを格納]
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
				nameValuePair.add(new BasicNameValuePair("user_id", user_ID));
				nameValuePair.add(new BasicNameValuePair("status", status));
				//nameValuePair.add(new BasicNameValuePair("last_modified", nowtime));
				nameValuePair.add(new BasicNameValuePair("latitude", lat));
				nameValuePair.add(new BasicNameValuePair("longitude", lng));
				nameValuePair.add(new BasicNameValuePair("comment", comment));
				try	{
					//-----[POST送信]
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,"utf-8"));
					HttpResponse response = httpclient.execute(httppost);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					response.getEntity().writeTo(byteArrayOutputStream);
					//-----[サーバーからの応答を取得]
					if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
						TextView tv = (TextView) findViewById(R.id.textView3);
						tv.setText(byteArrayOutputStream.toString());
					} else {
						TextView tv = (TextView) findViewById(R.id.textView3);
						tv.setText(byteArrayOutputStream.toString());
//					Toast.makeText(this, "[error]: "+response.getStatusLine(), Toast.LENGTH_LONG).show();
					}
				}catch (UnsupportedEncodingException e){
					e.printStackTrace();
				}catch (IOException e){
					e.printStackTrace();
				}

				//メールの自動送信
				String[] toaddresses = {"j12011@sangi.jp","sangi_050@sangi.jp"};
				String mailtext = "このメールは災害時安否確認システムからのメッセージです。\n"+
						          user_ID+"様がリストを更新しました。\n"+
						          "状態："+status+"\n"+
						          "現在地\n"+
						          "http://maps.google.com/maps?q="+lat+","+lng+"\n"+
						          "コメント\n"+comment;
				/*
				try {
					SendmailGmail.sendmail("sangi_050@sangi.jp",toaddresses,"sangi.jp",mailtext);
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				*/
			}
		});
	}
	//登録ボタンの動作ここまで

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.g, menu);
		return true;
	}

	//更新ボタンの動作
	public void onClick_button2(View view){
		//手動更新
		updateMap();
	}

	//カメラの動作
	public void onClick_button4(View view){
		// create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        startActivityForResult(intent, 1034);
	}

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = getNowTime();
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}


	//音声入力ボタンの動作
		public void onClick_button3(View view){
			try {
                // インテント作成
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // ACTION_WEB_SEARCH
                intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(
                        RecognizerIntent.EXTRA_PROMPT,
                        "Please Speech!"); // お好きな文字に変更できます

                // インテント発行
                startActivityForResult(intent, REQUEST_CODE);
            } catch (ActivityNotFoundException e) {

            }
		}

	// アクティビティ終了時に呼び出される
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 自分が投げたインテントであれば応答する
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String resultsString = "";

            // 結果文字列リスト
            ArrayList<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            /*for (int i = 0; i< results.size(); i++) {
                // ここでは、文字列が複数あった場合に結合しています
                resultsString += results.get(i);
            }*/

            resultsString += results.get(0);

            // トーストを使って結果を表示
            //Toast.makeText(this, resultsString, Toast.LENGTH_LONG).show();
            //コメント欄に出力
            Editable tmp1 = et_comment.getText();
            String tmp2 = resultsString;
            String tmp  = tmp1+tmp2;
            resultsString = tmp;
            et_comment.setText(resultsString);
        }

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1034) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                // 直前の bitmap が読み込まれていたら開放する
                if(null != bm){
                    bm.recycle();
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                // 4 を指定すると 元の 1/4 のサイズで bitmap を取得する
                options.inSampleSize = 4;
                bm = BitmapFactory.decodeFile(fileUri.getPath(), options);

                Toast.makeText(this, "一時保存完了", Toast.LENGTH_LONG).show();

                //preview.setImageBitmap(bm);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO 自動生成されたメソッド・スタブ
/*
 * 		自動更新機能
		updateMap();
*/
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

	private String getMailAddress(String id){
		String address="";
		return address;
	}

	private String getMailPassword(String id){
		String password = "";
		return password;
	}

	private void getStatus(){
		if(rd == rd0){
			status = "無事";
		} else if(rd==rd1){
			status = "負傷";
		} else if(rd==rd2){
			status = "その他";
		}
	}

	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

	private void setUpMap() {
		mMap.addMarker(new MarkerOptions().position(latlng).title("Check!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 19));
    }

	private static String getNowTime(){
		String nowtime="";
		Calendar cal1 = Calendar.getInstance();  //オブジェクトの生成
	    int year = cal1.get(Calendar.YEAR);        //現在の年を取得
	    int month = cal1.get(Calendar.MONTH) + 1;  //現在の月を取得
	    int day = cal1.get(Calendar.DATE);         //現在の日を取得
	    int hour = cal1.get(Calendar.HOUR_OF_DAY); //現在の時を取得
	    int minute = cal1.get(Calendar.MINUTE);    //現在の分を取得
	    int second = cal1.get(Calendar.SECOND);    //現在の秒を取得

	    nowtime += Integer.toString(year)   + "/";
	    nowtime += Integer.toString(month)  + "/";
	    nowtime += Integer.toString(day)    + "_";
	    nowtime += Integer.toString(hour)   + ":";
	    nowtime += Integer.toString(minute) + ":";
	    nowtime += Integer.toString(second);

		return nowtime;
	}

	private void updateMap(){
		lati.setText("緯度："+Double.toString(location.getLatitude()));
		longi.setText("経度："+Double.toString(location.getLongitude()));

		lat = Double.toString(location.getLatitude());
		lng = Double.toString(location.getLongitude());

		latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
		setUpMap();
	}

}
