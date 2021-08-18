package ir.moallem.app;




import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class CuActivity extends Activity 
		 {
	boolean settoolbarid = true;
	String toolbartitle = "";;
	String toolbarsubtitle = "";
	private ImageView btnback;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (toolbartitle.equals("")) {
			toolbartitle = getResources().getString(R.string.app_name);
		}
		super.onCreate(savedInstanceState);
		

		// getSupportActionBar().setBackgroundDrawable(new
		// ColorDrawable(R.color.bg_actionbar));
		// getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.navbg));

	}

	



	void setcustomtoolbar() {

	}

	@Override
	public void setContentView(int layoutResID) {

		super.setContentView(layoutResID);
		if (settoolbarid == true) {
			
			

			setUpActionButtons();
		}
	}

	void setUpActionButtons() {
		
		
	}
}
