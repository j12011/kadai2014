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
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

public class AsyncTaskGetJson extends AsyncTask<Void, Void, String> {

	private final static String TAG = "AsyncTaskGetJson";
	String tmp_id = "";
	String tmp_username = "";
	String tmp_password = "";
	String tmp_mailaddress = "";
	String tmp_mailpass = "";

	/**
	 * API URL
	 */
	//private final static String API_URL = "http://192.168.1.87/json_select.php";
	//private static final String API_URL = "http://10.230.0.89/json_select.php";
	private static final String API_URL = "http://j12006.sangi01.net/json_select.php";

	/**
	 * 呼び出し元のMainActivity
	 */
	private LoginPageActivity activity;

	/**
	 * Constructor
	 *
	 * @param _activity: 呼び出し元のアクティビティ
	 */
	public AsyncTaskGetJson(LoginPageActivity _activity) {
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

		String result = new String();
		ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(API_URL);
			httpPost.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();

			if (responseEntity != null) {
				String data = EntityUtils.toString(responseEntity);
				Log.d(TAG+" json data", data);

				JSONObject rootObject = new JSONObject(data);

				JSONArray userArray = rootObject.getJSONArray("users");

				for (int n = 0; n < userArray.length(); n++) {
					// User data
					JSONObject userObject = userArray.getJSONObject(n);

					// JSON data の確認
					Log.d(TAG+" user data", userObject.toString());

					String userId = userObject.getString("id");
					String userName = userObject.getString("username");
					String passWord = userObject.getString("password");
					String mailAddress = userObject.getString("mailaddress");
					String mailPass = userObject.getString("mailpass");

					result += userId +"\n"+ userName+ "\n" + passWord + "\n"+ mailAddress +"\n"+mailPass+"\n";

					tmp_id += userId+",";
					tmp_username += userName+",";
					tmp_password += passWord+",";
					tmp_mailaddress += mailAddress+",";
					tmp_mailpass += mailPass+",";


					//result = userId;
					// output の途中経過の確認
					Log.d(TAG+" result", result);

				}
				tmp_id.substring(0, tmp_id.length()-1);
				tmp_username.substring(0, tmp_username.length()-1);
				tmp_password.substring(0, tmp_password.length()-1);
				tmp_mailaddress.substring(0, tmp_mailaddress.length()-1);
				tmp_mailpass.substring(0, tmp_mailpass.length()-1);

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * doInbackground method 後に実行する処理
	 *
	 * @param _result: doInBackground method から受け渡されるデータ
	 */
	@Override
	protected void onPostExecute(String _result) {
		Log.d(TAG+" onPostExecute", "start");
		activity.setResult(_result);
		activity.setID_Array(tmp_id);
		activity.setUSERNAME_Array(tmp_username);
		activity.setPASSWORD_Array(tmp_password);
		activity.setMAILADDRESS_Array(tmp_mailaddress);
		activity.setMAILPASS_Array(tmp_mailpass);
		activity.t.setText(_result);
		return;
	}

}
