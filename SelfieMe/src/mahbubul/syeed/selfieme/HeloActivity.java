package mahbubul.syeed.selfieme;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HeloActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_empty_layout);
		
		// set the actionbar color
				getActionBar().setBackgroundDrawable(
						new ColorDrawable(Color.parseColor("#ADA458")));
	}

	
}
