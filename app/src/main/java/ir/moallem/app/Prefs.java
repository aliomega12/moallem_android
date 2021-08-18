package ir.moallem.app;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
	private static String STRING_PREF_MSG = "msgprefs";
	private static String STRING_PREF_ID = "spmclientprefs";
	private static String STRING_PREF_TOKEN = "tokenprefs";
	private static String STRING_PREF_PHONE = "phoneprefs";
	private static SharedPreferences getPrefs(Context context) {
		return context.getSharedPreferences(STRING_PREF_ID, 0);
	}

	public static String getPrefRootPath() {
		return "http://mqtt.moallem.sch.ir/";
	}
	public static String getPrefMqttRootPath() {
		return "mqtt.moallem.sch.ir";
	}

	public static String getPrefPhone(Context context) {
		return getPrefs(context).getString(STRING_PREF_PHONE, null);
	}

	public static void setPrefPhone(Context context, String value) {
		// perform validation etc..
		getPrefs(context).edit().putString(STRING_PREF_PHONE, value).commit();
	}

	public static String getPrefToken(Context context) {
		return getPrefs(context).getString(STRING_PREF_TOKEN, null);
	}

	public static void setPrefToken(Context context, String value) {
		// perform validation etc..
		getPrefs(context).edit().putString(STRING_PREF_TOKEN, value).commit();
	}

	public static void setPrefMsg(Context context, String value) {
		getPrefs(context).edit().putString(STRING_PREF_MSG, value).commit();
		
	}
	public static String getPrefMsg(Context context) {
		return getPrefs(context).getString(STRING_PREF_MSG, null);
	}
}
