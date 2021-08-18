package ir.moallem.app;

import java.io.UnsupportedEncodingException;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Actshowmsg extends CuActivity {

	private TextView tv_msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_showmsg);
		tv_msg = (TextView) findViewById(R.id.tv_msg);
		String[] curmessage = Prefs.getPrefMsg(this).toString()
				.split("\\|\\|\\|\\|");

		tv_msg.setText(curmessage[0]);


	}

}
