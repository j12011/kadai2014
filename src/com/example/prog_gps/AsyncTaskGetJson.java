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

	/**
	 * API URL
	 */
	//private final static String API_URL = "http://192.168.1.87/json_select.php";
	private static final String API_URL = "http://10.230.0.89/json_select.php";

	/**
	 * 呼び出し元のMainActivity
	 */
	private ListActivity activity;

	/**
	 * Constructor
	 *
	 * @param _activity: 呼び出し元のアクティビティ
	 */
	public AsyncTaskGetJson(ListActivity _activity) {
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

				JSONArray userArray = rootObject.getJSONArray("User");

				for (int n = 0; n < userArray.length(); n++) {
					// User data
					JSONObject userObject = userArray.getJSONObject(n);

					// JSON data の確認
					Log.d(TAG+" user data", userObject.toString());

					String userId = userObject.getString("id");
					String userName = userObject.getString("name");
					String userStatus = userObject.getString("status");
					String userComment = userObject.getString("comment");
					String userLastModified = userObject.getString("last_modified");
					//String userLatlng =  userObject.getString("lat") + "," +userObject.getString("lng") ;
					result += "ID"+ userId + "\r\n" + "User: "+userName+" \r\n" +"状態: " +userStatus+"\r\n" +"コメント: " +userComment +"\r\n"
							  + "最終更新："+userLastModified+"\r\n";
					//+"http://maps.google.com/maps?q="+userLatlng+"\r\n";


					// output の途中経過の確認
					Log.d(TAG+" result", result);

					// User Item data
					//JSONArray userItemArray = userObject.getJSONArray("UserItem");
					//for (int i = 0; i < userItemArray.length(); i++) {
					//	JSONObject userItemObject = userItemArray.getJSONObject(i);

						// JSON data の確認
						//Log.d(TAG+" user item data", userItemObject.toString());

						//String item = userItemObject.getString("item_name");
						//result += "Item: "+item+"\r\n";
					//}
					result += "\r\n";
				}
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

		activity.textView.setText(_result);
		return;
	}

}
