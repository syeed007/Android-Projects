package mahbubul.syeed.alarmmanager;


import mahbubul.syeed.selfieme.CameraActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	
	    // Notification ID to allow for future updates
		private static final int MY_NOTIFICATION_ID = 1;

		// Notification Count (shows the number of pending todos)
		private int mNotificationCount;

		// Notification Text Elements
		private final CharSequence tickerText = "Take a selfie!";
		private final CharSequence contentTitle = "Its Selfie Time!";
		private final CharSequence contentText = "Click to Grab your moment now.";

		// Notification Action Elements
		private Intent mNotificationIntent;
		private PendingIntent mContentIntent;
	
		//for setting a new alarm
		private SetAlarm mSetAlarm;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		//first set a new alarm
		mSetAlarm = new SetAlarm(context);
		mSetAlarm.calculateAlarmTime();
		
		// A toast notification
		Toast.makeText(context, "Selfie time Darling!", Toast.LENGTH_LONG).show();

		//set up the vibrator to notify.
		Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(700);

		//set up the ring tone manager with device's default
		//notification ringtone..
		Uri alert = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		MediaPlayer mp = MediaPlayer.create(context, alert);
		mp.setVolume(100, 100);
		mp.start();
		mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
		
		
		// Define the Notification's expanded message and Intent:

		//intent is set to open the todo list onece the notification is clicked
		mNotificationIntent = new Intent(context,
				CameraActivity.class);
		
		//set the pending intent..
		mContentIntent = PendingIntent.getActivity(context, 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		//set the notification parameters..
		Notification.Builder notificationBuilder = new Notification.Builder(
				context)
				.setTicker(tickerText)
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setAutoCancel(true)
				.setContentTitle(contentTitle)
				.setContentText(
						contentText + " (" + ++mNotificationCount + ")")
				.setContentIntent(mContentIntent).setSound(alert);
				//.setVibrate(mVibratePattern);

		// Pass the Notification to the NotificationManager and to build it.
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());
			
	}

	
	

	
}
