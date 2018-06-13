package com.games.fifa;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TeamsEdit extends Activity {

	private EditText mTeam1Text;
	private EditText mTeam2Text;
	private EditText mTeam3Text;
	private EditText mTeam4Text;
	private EditText mTeam5Text;
	private EditText mTeam6Text;
	private EditText mTeam7Text;
	private EditText mTeam8Text;
	private EditText mTeam9Text;
	private EditText mTeam10Text;
	private EditText mTeam11Text;
	private EditText mTeam12Text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.teams_edit);

		Button confirmButton = (Button) findViewById(R.id.confirm);

		populateFields();

		confirmButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	private void saveState() {
		SharedPreferences settings = getSharedPreferences(
				FIFAMatches.PREFS_NAME, 0);

		SharedPreferences.Editor editor = settings.edit();
		editor.putString("team1", (mTeam1Text.getText().toString()));
		editor.putString("team2", (mTeam2Text.getText().toString()));
		editor.putString("team3", (mTeam3Text.getText().toString()));
		editor.putString("team4", (mTeam4Text.getText().toString()));
		editor.putString("team5", (mTeam5Text.getText().toString()));
		editor.putString("team6", (mTeam6Text.getText().toString()));
		editor.putString("team7", (mTeam7Text.getText().toString()));
		editor.putString("team8", (mTeam8Text.getText().toString()));
		editor.putString("team9", (mTeam9Text.getText().toString()));
		editor.putString("team10", (mTeam10Text.getText().toString()));
		editor.putString("team11", (mTeam11Text.getText().toString()));
		editor.putString("team12", (mTeam12Text.getText().toString()));

		// Don't forget to commit your edits!!!
		editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void populateFields() {
		SharedPreferences settings = getSharedPreferences(
				FIFAMatches.PREFS_NAME, 0);

		mTeam1Text = (EditText) findViewById(R.id.team1);
		mTeam2Text = (EditText) findViewById(R.id.team2);
		mTeam3Text = (EditText) findViewById(R.id.team3);
		mTeam4Text = (EditText) findViewById(R.id.team4);
		mTeam5Text = (EditText) findViewById(R.id.team5);
		mTeam6Text = (EditText) findViewById(R.id.team6);
		mTeam7Text = (EditText) findViewById(R.id.team7);
		mTeam8Text = (EditText) findViewById(R.id.team8);
		mTeam9Text = (EditText) findViewById(R.id.team9);
		mTeam10Text = (EditText) findViewById(R.id.team10);
		mTeam11Text = (EditText) findViewById(R.id.team11);
		mTeam12Text = (EditText) findViewById(R.id.team12);

		mTeam1Text.setText(settings.getString("team1", "Chelsea"));
		mTeam2Text.setText(settings.getString("team2", "Manchester"));
		mTeam3Text.setText(settings.getString("team3", "Brazil"));
		mTeam4Text.setText(settings.getString("team4", "Argentina"));
		mTeam5Text.setText(settings.getString("team5", "Barcelona"));
		mTeam6Text.setText(settings.getString("team6", "Real"));
		mTeam7Text.setText(settings.getString("team7", "Inter"));
		mTeam8Text.setText(settings.getString("team8", "Milan"));
		mTeam9Text.setText(settings.getString("team0", "Sao Paulo"));
		mTeam10Text.setText(settings.getString("team10", "Palmeiras"));
		mTeam11Text.setText(settings.getString("team11", "France"));
		mTeam12Text.setText(settings.getString("team12", "Italy"));

	}
}
