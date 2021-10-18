package ir.moallem.app;

import ir.moallem.app.CuMqttConnection.ConnectionStatus;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;

import androidx.core.app.NotificationManagerCompat;

/*
 * An example of how to implement an MQTT client in Android, able to receive
 *  push notifications from an MQTT message broker server.
 *  
 *  Dale Lane (dale.lane@gmail.com)
 *    28 Jan 2011
 */
public class CuMqttService3 extends MqttService implements MqttCallback {


	private String uri;
	private String server;
	private String port;
	private NotificationManager mNM;
	private final IBinder mBinder = new LocalBinder();
	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	private int NOTIFICATION = 2;
	private int NOTIFICATIONSERVICE = 3;
	String TAG = "loghotel";
	// public static final String BROKER_URL = "tcp://test.mosquitto.org:1883";

	/*
	 * In a real application, you should get an Unique Client ID of the device
	 * and use this, see
	 * http://android-developers.blogspot.de/2011/03/identifying
	 * -app-installations.html
	 */
	public static final String clientId = "android-client";

	public static final String TOPIC = "de/eclipsemagazin/blackice/warnings";
	private MqttAndroidClient mqttClient;
	private String token;
	private Context context;

	public class LocalBinder extends Binder {
		
		CuMqttService3 getService() {
			return CuMqttService3.this;
		}
	}

	@Override
	public void onCreate() {

		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Display a notification about us starting. We put an icon in the
		// status bar.

	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand: ");
		Log.i("hotel", "start service");

		showNotification();
		ensureServiceStaysRunning();


		token ="all";


		uri = "tcp://";
		server = "192.168.88.252";
		port = "1883";
		uri = uri + server + ":" + port;
		String message = "hello";
		String topic = token;
		Integer qos = 2;
		Boolean retained = false;
		this.context=this;
		final MqttAndroidClient client = new MqttAndroidClient(this, uri, token);
		MqttConnectOptions conOpt = new MqttConnectOptions();

		// connection options

		String username = "ali";

		String password = "aliomega12";

		int timeout = 120;
		int keepalive = 120;

		// connection.registerChangeListener(changeListener);
		// connect client

		String[] actionArgs = new String[1];
		actionArgs[0] = clientId;

		conOpt.setCleanSession(false);
		conOpt.setConnectionTimeout(timeout);
		conOpt.setKeepAliveInterval(keepalive);
		conOpt.setUserName(username);
		conOpt.setPassword(password.toCharArray());
		
		final IMqttActionListener callback = new IMqttActionListener() {

			@Override
			public void onSuccess(IMqttToken arg0) {
				try {
					client.subscribe(token, 2);
					Log.i("hotel", "connect to service");
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(IMqttToken arg0, Throwable arg1) {
				// TODO Auto-generated method stub

			}
		};

		boolean doConnect = true;

		if ((message != null) || (topic != null)) {
			// need to make a message since last will is set
			/*
			 * try { conOpt.setWill(topic, message.getBytes(), qos.intValue(),
			 * retained.booleanValue()); } catch (Exception e) {
			 * Log.e(this.getClass().getCanonicalName(), "Exception Occured",
			 * e); doConnect = false; callback.onFailure(null, e); }
			 */
		}
		client.setCallback(this);
		// set traceCallback
		client.setTraceCallback(new CuMqttTraceCallback());

		if (doConnect) {
			try {
				client.connect(conOpt, null, callback);
			} catch (MqttException e) {
				Log.e(this.getClass().getCanonicalName(),
						"MqttException Occured", e);
			}
		}
		showNotification();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy: ");
		try {
			mqttClient.disconnect(0);
		} catch (MqttException | NullPointerException e) {
			/*
			 * Toast.makeText(this, "Something went wrong!" + e.getMessage(),
			 * Toast.LENGTH_LONG).show();
			 */
			e.printStackTrace();
		}
		stopForeground(true);
		mNM.cancel(NOTIFICATION);
		Log.i("hotel", "destroy service");
		// Tell the user we stopped.
		// Toast.makeText(this, "Ø³Ø±ÙˆÛŒØ³ ØºÛŒØ± Ù�Ø¹Ø§Ù„ Ø´Ø¯", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		Log.i("hotel", "new message");
		int id=-1;
		String[] curmessage = message.toString().split("\\|\\|\\|\\|");
		PendingIntent contentIntent = null;


		int reqCode = 99;
		Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
		String CHANNEL_ID = "channel_name";// The id of the channel.
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle("مجتمع فرهنگی آموزشی پسرانه معلم")
				.setContentText(curmessage[0])
				.setAutoCancel(true)
				.setContentIntent(pendingIntent);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "Channel Name";// The user-visible name of the channel.
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
			notificationManager.createNotificationChannel(mChannel);
		}
		notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

		Log.d("showNotification", "showNotification: " + reqCode);


		Log.i("hotel", "new message");
	}

	@SuppressLint("NewApi")
	private void showStateNotification(String msg, String type,
			PendingIntent contentIntent, int id) {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = msg;
		Log.i(TAG, "showStateNotification: " + msg);
		// Set the icon, scrolling text and timestamp

		// The PendingIntent to launch our activity if the user selects this
		// notification

		// Set the info for the views that show in the notification panel.

	}

	@SuppressLint("NewApi")
	private void showNotification() {
		Log.i(TAG, "showNotification: ");
		/*
		 * // In this sample, we'll use the same text for the ticker and the
		 * expanded notification CharSequence text = "Ø³Ø±ÙˆÛŒØ³ Ù�Ø¹Ø§Ù„ Ø´Ø¯";
		 * 
		 * // Set the icon, scrolling text and timestamp Notification
		 * notification = new Notification(R.drawable.ic_launcher, text,
		 * System.currentTimeMillis());
		 * 
		 * // The PendingIntent to launch our activity if the user selects this
		 * notification PendingIntent contentIntent =
		 * PendingIntent.getActivity(this, 0, new Intent(this,
		 * Act_passenger_activelist.class), 0);
		 * 
		 * // Set the info for the views that show in the notification panel.
		 * notification.setLatestEventInfo(this, "Ø¢Ø®Ø±ÛŒÙ† ÙˆØ¶Ø¹ÛŒØª", text,
		 * contentIntent);
		 * 
		 * // Send the notification. mNM.notify(NOTIFICATION, notification);
		 */
		//startForeground(NOTIFICATIONSERVICE, notification);
		// mNM.notify(1, notification);
	}

	private void ensureServiceStaysRunning() {
		Log.i(TAG, "ensureServiceStaysRunning: ");
		// KitKat appears to have (in some cases) forgotten how to honor
		// START_STICKY
		// and if the service is killed, it doesn't restart. On an emulator &
		// AOSP device, it restarts...
		// on my CM device, it does not - WTF? So, we'll make sure it gets back
		// up and running in a minimum of 20 minutes. We reset our timer on a
		// handler every
		// 2 minutes...but since the handler runs on uptime vs. the alarm which
		// is on realtime,
		// it is entirely possible that the alarm doesn't get reset. So - we
		// make it a noop,
		// but this will still count against the app as a wakelock when it
		// triggers. Oh well,
		// it should never cause a device wakeup. We're also at SDK 19
		// preferred, so the alarm
		// mgr set algorithm is better on memory consumption which is good.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// A restart intent - this never changes...
			final int restartAlarmInterval = 20 * 60 * 1000;
			final int resetAlarmTimer = 2 * 60 * 1000;
			final Intent restartIntent = new Intent(this, CuMqttService3.class);
			restartIntent.putExtra("ALARM_RESTART_SERVICE_DIED", true);
			final AlarmManager alarmMgr = (AlarmManager) getSystemService(this.ALARM_SERVICE);
			Handler restartServiceHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// Create a pending intent
					PendingIntent pintent = PendingIntent.getService(
							getApplicationContext(), 0, restartIntent, 0);
					alarmMgr.set(AlarmManager.ELAPSED_REALTIME,
							SystemClock.elapsedRealtime()
									+ restartAlarmInterval, pintent);
					sendEmptyMessageDelayed(0, resetAlarmTimer);
				}
			};
			restartServiceHandler.sendEmptyMessageDelayed(0, 0);
		}
	}
}
