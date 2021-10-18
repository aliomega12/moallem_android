package ir.moallem.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
public class ActivityMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent = new Intent(this,
                ir.moallem.app.CuMqttService3.class);
        startService(intent);

        getSupportActionBar().hide();
        WebView mywebview = (WebView) findViewById(R.id.webView);
        WebView.setWebContentsDebuggingEnabled(true);
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(false);

        //mywebview.loadUrl("file:///ssh.moallem.sch.ir/static/mobile_app_android_static/index.html");
        mywebview.loadUrl("http://ssh.moallem.sch.ir/static/mobile_app_android_static/index.html");
        //mywebview.loadUrl("http://192.168.1.13:8080");


            //finish();

    }
}