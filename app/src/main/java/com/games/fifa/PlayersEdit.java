package com.games.fifa;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PlayersEdit extends Activity {

	private EditText[] mPlayersText = new EditText[FIFAMatches.NUM_PLAYERS];
	private EditText[] mEmailsText = new EditText[FIFAMatches.NUM_PLAYERS];

	private static final int SHIFT_PLAYERS_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.players_edit);

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

		for (int i = 0, j = 1; i < FIFAMatches.NUM_PLAYERS; i++, j++) {
			editor
					.putString("player" + j, mPlayersText[i].getText()
							.toString());
			editor.putString("email" + j, mEmailsText[i].getText().toString());
		}
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

		mPlayersText[0] = (EditText) findViewById(R.id.player1);
		mPlayersText[1] = (EditText) findViewById(R.id.player2);
		mPlayersText[2] = (EditText) findViewById(R.id.player3);
		mPlayersText[3] = (EditText) findViewById(R.id.player4);

		mEmailsText[0] = (EditText) findViewById(R.id.email1);
		mEmailsText[1] = (EditText) findViewById(R.id.email2);
		mEmailsText[2] = (EditText) findViewById(R.id.email3);
		mEmailsText[3] = (EditText) findViewById(R.id.email4);

		mPlayersText[0].setText(settings.getString("player1", "Alberto"));
		mPlayersText[1].setText(settings.getString("player2", "Alexandre"));
		mPlayersText[2].setText(settings.getString("player3", "Marco"));
		mPlayersText[3].setText(settings.getString("player4", "Rodrigo"));

		mEmailsText[0].setText(settings.getString("email1",
				"alberto_pacifico@yahoo.com"));
		mEmailsText[1].setText(settings.getString("email2",
				"alexandre_olivieri@hotmail.com"));
		mEmailsText[2].setText(settings.getString("email3",
				"marcoforini@hotmail.com"));
		mEmailsText[3].setText(settings
				.getString("email4", "galatti@gmail.com"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SHIFT_PLAYERS_ID, 0, R.string.menu_shift_players);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case SHIFT_PLAYERS_ID:
			shiftPlayers();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void shiftPlayers() {
		SharedPreferences settings = getSharedPreferences(
				FIFAMatches.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();

		// shift players id. player 1 becomes 2, 2 becomes 3, and so on
		for (int i = 1; i < FIFAMatches.NUM_PLAYERS; i++) {
			editor.putString("player" + i, mPlayersText[i].getText().toString());
			editor.putString("email" + i, mEmailsText[i].getText().toString());
		}
		
		// the last player receives the former first
		editor.putString("player" + FIFAMatches.NUM_PLAYERS, mPlayersText[0].getText().toString());
		editor.putString("email" + FIFAMatches.NUM_PLAYERS, mEmailsText[0].getText().toString());
		
		// Don't forget to commit your edits!!!
		editor.commit();
		
		populateFields();
	}

}
