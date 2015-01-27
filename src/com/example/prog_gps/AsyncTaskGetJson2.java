package com.example.prog_gps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class AsyncTaskGetJson2 extends AsyncTask<Void, Void, String> {

	private final static String TAG = "AsyncTaskGetJson2";

	/**
	 * API URL
	 */
	//private final static String API_URL = "http://192.168.1.87/geter.php";
	private static final String API_URL = "http://10.230.0.89/geter.php";
	/**
	 * 呼び出し元のMainActivity
	 */
	private LoginPageActivity activity;

	/**
	 * Constructor
	 *
	 * @param _activity: 呼び出し元のアクティビティ
	 */
	public AsyncTaskGetJson2(LoginPageActivity _activity) {
		Log.d(TAG+" constructor", "start");

		activity = _activity;
	}

	/**
	 * バックグラウンドで実行する処理
	 *
	 * @param _params: Activityから受け渡されるデータ
	 * @return onPostExecute method へ受け渡すデータ
	 */
	@Override
	protected String doInBackground(Void... _params) {
		Log.d(TAG+" doInBackground", "start");

		String result2 = new String();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(API_URL);
		//-----[POST送信するデータを格納]
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			String user_id =LoginPageActivity.getID();
			nameValuePair.add(new BasicNameValuePair("user_id", user_id));
			Log.d("user_id:", user_id);
			try	{
				//-----[POST送信]
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,"utf-8"));
				HttpResponse response = httpClient.execute(httpPost);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				response.getEntity().writeTo(byteArrayOutputStream);
				Log.d("送信::::::","ok");
				//-----[サーバーからの応答を取得]
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					//TextView tv = (TextView) findViewById(R.id.textView3);
					//tv.setText(byteArrayOutputStream.toString());
					Log.d("応答::::::","ok");
				} else {
					//TextView tv = (TextView) findViewById(R.id.textView3);
					//tv.setText(byteArrayOutputStream.toString());
//				Toast.makeText(this, "[error]: "+response.getStatusLine(), Toast.LENGTH_LONG).show();
				}
				}catch (UnsupportedEncodingException e){
					e.printStackTrace();
				}catch (IOException e){
					e.printStackTrace();
				}

		ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();

		try {

			httpPost.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();

			if (responseEntity != null) {
				String data = EntityUtils.toString(responseEntity);
				Log.d(TAG+" json data", data);

				JSONObject rootObject = new JSONObject(data);

				JSONArray userArray = rootObject.getJSONArray("User");

				for (int n = 0; n < userArray.length(); n++) {
					// User data
					JSONObject userObject = userArray.getJSONObject(n);

					// JSON data の確認
					Log.d(TAG+" user data", userObject.toString());
					//String userid = userObject.getString("id");
					String userPass = userObject.getString("pass");
					String userName = userObject.getString("name");
					result2 = userPass;
					// output の途中経過の確認
					Log.d(TAG+" result", result2);

				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result2;
	}

	/**
	 * doInbackground method 後に実行する処理
	 *
	 * @param _result: doInBackground method から受け渡されるデータ
	 */
	@Override
	protected void onPostExecute(String _result2) {
		Log.d(TAG+" onPostExecute", "start");

		activity.PASS_temp = _result2;
		return;
	}

}
