package ir.moallem.app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Utils {
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();

			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					Log.i("Class", info[i].getState().toString());
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isNetworkAvailable(Context context, Boolean showalert) {
		if (isNetworkAvailable(context)) {
			return true;
		} else {
			if (showalert) {
//				new CuToast(context, "دسترسی به اینترنت میسر نیست!",
//						Toast.LENGTH_LONG,CuToast.MSGTYPEERROR).show();
			}
		}
		return false;
	}

	public String getconvertdate1(String date) {
		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
		Date parsed = new Date();
		try {
			parsed = inputFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String outputText = outputFormat.format(parsed);
		return outputText;
	}

	public static Date DateStringToDatetime(String date) {// Desire date Format:
		// yyyy-MM-dd
		date += " 23:00:00";
		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date parsed = null;
		try {
			parsed = inputFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parsed;
	}

	public static String DatetimeToDateString(Date date) {

		DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		String outputText = outputFormat.format(date);
		return outputText;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop()
				+ listView.getPaddingBottom();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	public Boolean isOnline() {
	    try {
	        Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 www.google.com");
	        int returnVal = p1.waitFor();
	        boolean reachable = (returnVal==0);
	        return reachable;
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } 
	    return false;
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static Drawable getDrawable(Context context,int drawid)
	{
		try
		{
			return context.getDrawable(drawid);
		}
		catch(NoSuchMethodError e)
		{
			return context.getResources().getDrawable(drawid);
		}
	}
	static boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
	    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	static boolean isMobileAvailable(Context appcontext) {
		TelephonyManager tel = (TelephonyManager) appcontext
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((tel.getNetworkOperator() != null && tel.getNetworkOperator()
				.equals("")) ? false : true);
	}
}
