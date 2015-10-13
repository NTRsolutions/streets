package net.blaklizt.streets.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import net.blaklizt.streets.android.R;
import net.blaklizt.streets.android.common.StreetsCommon;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: photon
 * Date: 2015/06/11
 * Time: 4:30 PM
 */
public class Startup extends AppCompatActivity implements View.OnClickListener
{
	private Button loginBtn;
	private EditText password = null;
	int counter = 5;

	private class LoginTask extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog progressDialog;

		@Override
		protected Void doInBackground(Void... param)
		{
			Log.i(TAG, "Authenticating...");

//			ServerCommunication.sendServerRequest("action=Login&channel=" + StreetsCommon.CHANNEL + "&username=" + username.getText().toString() + "&password=" + password.getText().toString());
			String loginResponse = "{response_code:1, response_message:\"success\"}";

			if (loginResponse == null)
			{
				runOnUiThread(new Runnable() {
					@Override public void run() {
						Toast.makeText(getInstance(), "Login Failed. Check Internet Connection.", Toast.LENGTH_SHORT).show();
					}
				});
				return null;
			}

			try
			{
				JSONObject responseJSON = new JSONObject(loginResponse);

				if (responseJSON.getInt("response_code") == 1)//ResponseCode.SUCCESS.getValue())
				{
					Log.i(TAG, "Login successful");
					runOnUiThread(new Runnable() { @Override public void run() { Toast.makeText(getInstance(), "Login successful", Toast.LENGTH_SHORT).show(); } });
					Intent mainActivity = new Intent(getInstance(), Streets.class);
					startActivity(mainActivity);
					StreetsCommon.registerStreetsActivity(Streets.getInstance());
				}
				else if (responseJSON.getInt("response_code") < 0)
				{
					Log.i(TAG, "Login failed with internal error: " + responseJSON.getString("response_message"));
					runOnUiThread(new Runnable() { @Override public void run() { Toast.makeText(getInstance(), "Login Failed. An unknown error occurred on the server.", Toast.LENGTH_SHORT).show(); } });
				}
				else
				{
					final String loginResponseStr = responseJSON.getString("response_message");
					Log.i(TAG, "Login failed: " + responseJSON.getString("response_message"));
					runOnUiThread(new Runnable() { @Override public void run() { Toast.makeText(getInstance(), loginResponseStr, Toast.LENGTH_SHORT).show(); } });

					if (--counter <= 0)
					{
						runOnUiThread(new Runnable() {
							@Override public void run() {
								password.setEnabled(false);
								loginBtn.setEnabled(false);
								Toast.makeText(getInstance(), "Maximum login attempts. Please contact support", Toast.LENGTH_LONG).show();
							}
						});
					}
				}
			}
			catch (Exception e)
			{
				Log.e(TAG, "Login failed: " + e.getMessage(), e);
				e.printStackTrace();
				runOnUiThread(new Runnable() { @Override public void run() { Toast.makeText(getInstance(), "Login Failed. An unknown error occurred on the server.", Toast.LENGTH_SHORT).show(); } });
			}
			return null;
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = ProgressDialog.show(getInstance(), "Authenticating", "Authenticating...", true, false);
			progressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Void... progress) { }

		@Override
		protected void onPostExecute(Void result)
		{
			progressDialog.hide();
		}
	}

	private static final String TAG = StreetsCommon.getTag(Startup.class);

	private VideoView videoView;

	private static Startup startup;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);
		try
		{
			startup = this;
			password = (EditText) findViewById(R.id.loginPassword);
			loginBtn = (Button) findViewById(R.id.btnLogin);

//			if (StreetsCommon.getInstance(this, 0).getUserPreference("ShowIntro").equals("0")) {
//				//Go directly to Login, do not pass video, do not disrupt music
//				if (Streets.getInstance() != null)        //coming backwards, so we are exiting
//				{
//					Log.i(TAG, "Existing Streets classes still running. Terminating.");
//					StreetsCommon.endApplication();
//					return;
//				}
//				else                                //going forward, let's get this work!
//				{
//					Log.i(TAG, "Initiating Tha Streets.");
//
//					Intent loginActivity = new Intent(this, Streets.class);
//					startActivity(loginActivity);
//					StreetsCommon.registerStreetsActivity(Streets.getInstance());
//					return;
//				}
//			}

			Log.i(TAG, "Playing intro clip...");

			videoView = (VideoView) findViewById(R.id.videoView);
			videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_clip));
			videoView.setMediaController(null);
			videoView.setClickable(false);
			videoView.setSoundEffectsEnabled(false);
			videoView.requestFocus();
			videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.setVolume(0, 0);
					mp.setLooping(false);
					videoView.start();
				}
			});
			videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					if (Streets.getInstance() != null)                        //coming backwards, so we are exiting
					{
						Log.i(TAG, "Existing Streets classes still running. Terminating.");
						StreetsCommon.endApplication();
					} else                                                //going forward, let's get this work!
					{
						Log.i(TAG, "Initiating Tha Streets.");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Log.i(TAG, "Going to login...");
						Intent loginActivity = new Intent(getApplication(), Streets.class);
						startActivity(loginActivity);
						StreetsCommon.registerStreetsActivity(Streets.getInstance());
					}
				}
			});
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			Log.e(TAG, "Failed to start application: " + ex.getMessage(), ex);
			runOnUiThread(new Runnable() {
				@Override public void run() {
					Toast.makeText(getApplication(), "Failed to start application! An error occurred.",
					Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	@Override
	public void onClick(View view)
	{
		new LoginTask().execute();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
//		loginBtn.setOnClickListener(this);
	}

	public static Startup getInstance() { return startup; }

}
