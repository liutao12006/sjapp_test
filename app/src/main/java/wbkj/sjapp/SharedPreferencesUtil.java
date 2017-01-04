package wbkj.sjapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import wbkj.sjapp.models.User;
import wbkj.sjapp.utils.SerializeUtil;

public class SharedPreferencesUtil {


	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor sharedPreferencesEditor;

	public SharedPreferencesUtil(Context context) {
		sharedPreferences = context.getSharedPreferences("share_data",
				Context.MODE_PRIVATE);
		sharedPreferencesEditor = sharedPreferences.edit();
	}

	public void put(String key,String value){
		sharedPreferencesEditor.putString(key, value);
		sharedPreferencesEditor.commit();
	}

	public void putInt(String key,int value){
		sharedPreferencesEditor.putInt(key, value);
		sharedPreferencesEditor.commit();
	}

	public int getInt(String key,int defaultValue){
		return sharedPreferences.getInt(key, defaultValue);
	}

	public long getFloat(String key,long defaultValue){
		return sharedPreferences.getLong(key, defaultValue);
	}

	public long getLong(String key,long defaultValue){
		return sharedPreferences.getLong(key,defaultValue);
	}



	public void delete(){
		sharedPreferencesEditor.remove("history");
		sharedPreferencesEditor.commit();
	}

	public String get(String key,String defaultValue){
		return sharedPreferences.getString(key, defaultValue);
	}

	public boolean isLogin(){
		return sharedPreferences.getBoolean("loginFlag", false);
	}
	
	/**
	 * @param user
	 */
	public void onLoginSuccess(User user) {
		try {
			sharedPreferencesEditor.putString("user", SerializeUtil.serialize(user));
			sharedPreferencesEditor.putBoolean("loginFlag", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sharedPreferencesEditor.commit();
	}

	public User getUser() {
		User user = null;
		try {
			user = SerializeUtil.deSerialization(sharedPreferences.getString("user", null));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return user;
	}

	public void logout() {
		sharedPreferencesEditor.remove("user");
		sharedPreferencesEditor.putBoolean("loginFlag", false);
		sharedPreferencesEditor.commit();
	}



}
