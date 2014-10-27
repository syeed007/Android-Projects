package mahbubul.syeed.modernartui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class UiMainActivity extends ActionBarActivity {

	private SeekBar mSeekBar;
	private TextView upper_left;
	private static int slideProgressDirection = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ui_main);

		mSeekBar = (SeekBar) findViewById(R.id.seekBar_colorchanger);
		upper_left = (TextView) findViewById(R.id.textleft_upper);

		upper_left.setText("Progressed: " + mSeekBar.getProgress());

		this.setSeekBarListener();

	}

	private void setSeekBarListener() {

		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				/**
				 * track the sliding direction. idea is: if the slider moves
				 * left to right the color changes to a new one if the sllider
				 * moves right to left the original color restores.
				 */
				if (progress > slideProgressDirection) {
					slideProgressDirection = progress;
					updateRectengleTextBoxColor(true);
				} else if (progress < slideProgressDirection) {
					slideProgressDirection = progress;
					updateRectengleTextBoxColor(false);
				}
				upper_left.setText("pogressed: " + progress);
				// TODO Auto-generated method stub
				// Toast.makeText(UiMainActivity.this, "Progressing: " +
				// progress, Toast.LENGTH_SHORT).show();

			}
		});
	}

	/**
	 * make the original color changing
	 * 
	 * @param sliderProgress
	 * @param direction
	 */
	private void updateRectengleTextBoxColor(Boolean direction) {

		TextView mTempText = (TextView) findViewById(R.id.textleft_upper);
		setColor(mTempText, direction);

		mTempText = (TextView) findViewById(R.id.textleft_lower);
		setColor(mTempText, direction);

		mTempText = (TextView) findViewById(R.id.textright_upper);
		setColor(mTempText, direction);

		//mTempText = (TextView) findViewById(R.id.textright_center);
		//setColor(mTempText, direction);

		mTempText = (TextView) findViewById(R.id.textright_lower);
		setColor(mTempText, direction);

	}

	private void setColor(TextView mtextview, Boolean direction) {

		ColorDrawable cd = (ColorDrawable) mtextview.getBackground();
		int colorCode = cd.getColor();

		// change the colors according to sliders dragging direction.
		if (direction) {
			mtextview.setBackgroundColor(colorCode - 1);
		} else if (!direction) {
			mtextview.setBackgroundColor(colorCode + 1);
		}
		 //upper_left.setText("color code: " + Integer.toHexString(colorCode));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ui_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			showAlertDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showAlertDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				UiMainActivity.this);

		// set title
		alertDialogBuilder.setTitle("Inspired by the works of artists such as Piet Mondrian and Beri Nicholson.");

		// set dialog message
		alertDialogBuilder
				.setMessage("Click below to learn more!")
				.setCancelable(false)
				.setPositiveButton("Not Now",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
								// if this button is clicked, close
								// current activity
								//UiMainActivity.this.finish();
							}
						})
				.setNegativeButton("Visit MOMA", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//if this button is clicked the momo wesite will be invoked
						Intent intent = new Intent(Intent.ACTION_VIEW,
								Uri.parse("http://www.moma.org"));
				    	
				    	    startActivity(intent);
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

} // end of class
