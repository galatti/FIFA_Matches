package com.games.fifa;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameEdit extends Activity {

	private EditText mScore1Text;
	private EditText mScore2Text;
	private Long mRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.game_edit);

		Button confirmButton = (Button) findViewById(R.id.confirm);

		mRowId = savedInstanceState != null ? savedInstanceState
				.getLong(FIFAMatches.KEY_ROWID) : null;
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(FIFAMatches.KEY_ROWID)
					: null;
		}

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
		if (mScore1Text.getText().toString().length() != 0
				&& mScore2Text.getText().toString().length() != 0) {
			SharedPreferences settings = getSharedPreferences(
					FIFAMatches.PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("game" + mRowId + "score0", Integer.parseInt(mScore1Text
					.getText().toString()));
			editor.putInt("game" + mRowId + "score1", Integer.parseInt(mScore2Text
					.getText().toString()));

			// Don't forget to commit your edits!!!
			editor.commit();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(FIFAMatches.KEY_ROWID, mRowId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void populateFields() {
		if (mRowId != null) {
			SharedPreferences settings = getSharedPreferences(
					FIFAMatches.PREFS_NAME, 0);

			((TextView) findViewById(R.id.team1)).setText(settings.getString(
					"game" + mRowId + "team0", FIFAMatches.TBD));
			((TextView) findViewById(R.id.team2)).setText(settings.getString(
					"game" + mRowId + "team1", FIFAMatches.TBD));

			mScore1Text = (EditText) findViewById(R.id.score1);
			mScore2Text = (EditText) findViewById(R.id.score2);

			if (settings.getInt("game" + mRowId + "score0",
					FIFAMatches.NOT_STARTED) != FIFAMatches.NOT_STARTED) {
				mScore1Text.setText(Integer.toString(settings.getInt("game"
						+ mRowId + "score0", FIFAMatches.NOT_STARTED)));
				mScore2Text.setText(Integer.toString(settings.getInt("game"
						+ mRowId + "score1", FIFAMatches.NOT_STARTED)));
			}
		}
	}
}
