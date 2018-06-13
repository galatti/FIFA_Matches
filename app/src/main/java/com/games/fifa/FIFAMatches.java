/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.games.fifa;

import java.util.Arrays;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * A list view example where the data for the list comes from an array of
 * strings.
 */
public class FIFAMatches extends ListActivity {

	public static final String PREFS_NAME = "FIFAMatch";
	public static final String KEY_ROWID = "id";

	private static final int SEND_FIXTURE_ID = Menu.FIRST;
	private static final int SEND_RESULTS_ID = Menu.FIRST + 1;
	private static final int SHOW_RESULTS_ID = Menu.FIRST + 2;
	private static final int RESET_RESULTS_ID = Menu.FIRST + 3;
	private static final int EDIT_TEAMS_ID = Menu.FIRST + 4;
	private static final int EDIT_PLAYERS_ID = Menu.FIRST + 5;
	private static final int RANDOM_ALL_ID = Menu.FIRST + 6;
	private static final int RANDOM_CLUB_ID = Menu.FIRST + 7;
	private static final int RANDOM_TEAM_ID = Menu.FIRST + 8;
	private static final int RANDOM_PLAYER_ID = Menu.FIRST + 9;

	public static final int NOT_STARTED = -1;
	public static final String TBD = "TBD";

	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_TEAMS = 2;
	private static final int ACTIVITY_PLAYERS = 3;

	private static final int DIALOG_STANDINGS = 0;

	public static final int NUM_PLAYERS = 4;
	private static final int NUM_GAMES = 15;
	private static final int CLUB_FACTOR = 3;
	private static final int TEAM_FACTOR = 0;

	private String[] playerName = new String[NUM_PLAYERS + 1];
	private String[] email = new String[NUM_PLAYERS];
	private String[][] team = new String[NUM_GAMES][2];
	private String[] game = new String[NUM_GAMES];
	private int[][] score = new int[NUM_GAMES][2];
	private int[][] playerID = new int[NUM_GAMES][NUM_PLAYERS];

	private SharedPreferences settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		settings = getSharedPreferences(PREFS_NAME, 0);

		playerName[NUM_PLAYERS] = TBD;

		fillData();

		registerForContextMenu(getListView());
	}

	private void fillData() {
		String versus;

		playerName[0] = settings.getString("player0", "Alberto");
		playerName[1] = settings.getString("player1", "Alexandre");
		playerName[2] = settings.getString("player2", "Marco");
		playerName[3] = settings.getString("player3", "Rodrigo");

		email[0] = settings.getString("email0", "alberto_pacifico@yahoo.com");
		email[1] = settings.getString("email1",
				"alexandre_olivieri@hotmail.com");
		email[2] = settings.getString("email2", "marcoforini@hotmail.com");
		email[3] = settings.getString("email3", "galatti@gmail.com");

		for (int i = 0; i < NUM_GAMES; i++) {
			team[i][0] = settings.getString("game" + i + "team0", TBD);
			team[i][1] = settings.getString("game" + i + "team1", TBD);
			score[i][0] = settings.getInt("game" + i + "score0", NOT_STARTED);
			score[i][1] = settings.getInt("game" + i + "score1", NOT_STARTED);

			for (int j = 0; j < NUM_PLAYERS; j++) {
				playerID[i][j] = settings.getInt("game" + i + "player" + j,
						NUM_PLAYERS);
			}

			if (score[i][0] == NOT_STARTED) {
				versus = "X";
			} else {
				versus = score[i][0] + " " + " X " + " " + score[i][1];
			}

			game[i] = String.format("%02d", i + 1) + " - ("
					+ playerName[playerID[i][0]] + "/"
					+ playerName[playerID[i][1]] + ") " + team[i][0] + " "
					+ versus + " " + team[i][1] + " ("
					+ playerName[playerID[i][2]] + "/"
					+ playerName[playerID[i][3]] + ")";
		}

		// Use an existing ListAdapter that will map an array
		// of strings to TextViews
		setListAdapter(new MyArrayAdapter(this,
				android.R.layout.simple_list_item_1, game));
		getListView().setTextFilterEnabled(true);

	}

	public class MyArrayAdapter extends ArrayAdapter<Object> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = getLayoutInflater().inflate(
					android.R.layout.simple_list_item_1, null);

			TextView i = (TextView) v.findViewById(android.R.id.text1);

			i.setText(game[position]);

			if (team[position][0].compareTo(TBD) == 0
					|| playerID[position][0] == NUM_PLAYERS) {
				i.setTextColor(Color.RED);
			} else if (score[position][0] == NOT_STARTED) {
				i.setTextColor(Color.YELLOW);
			} else {
				i.setTextColor(Color.GREEN);
			}

			return i;
		}

		public MyArrayAdapter(Context context, int textViewResourceId,
				Object[] objects) {
			super(context, textViewResourceId, objects);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SEND_FIXTURE_ID, 0, R.string.menu_send_fixture);
		menu.add(0, SEND_RESULTS_ID, 0, R.string.menu_send_results);
		menu.add(0, SHOW_RESULTS_ID, 0, R.string.menu_show_results);
		menu.add(0, RESET_RESULTS_ID, 0, R.string.menu_reset_results);
		menu.add(0, EDIT_TEAMS_ID, 0, R.string.menu_edit_teams);
		menu.add(0, EDIT_PLAYERS_ID, 0, R.string.menu_edit_players);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent i;

		switch (item.getItemId()) {
		case SEND_FIXTURE_ID:
			sendFixture();
			return true;
		case SEND_RESULTS_ID:
			sendResults();
			return true;
		case SHOW_RESULTS_ID:
			showDialog(DIALOG_STANDINGS);
			return true;
		case RESET_RESULTS_ID:
			resetResults();
			return true;
		case EDIT_TEAMS_ID:
			i = new Intent(this, TeamsEdit.class);
			startActivityForResult(i, ACTIVITY_TEAMS);
			return true;
		case EDIT_PLAYERS_ID:
			i = new Intent(this, PlayersEdit.class);
			startActivityForResult(i, ACTIVITY_PLAYERS);
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, RANDOM_ALL_ID, 0, R.string.random_all);
		menu.add(0, RANDOM_PLAYER_ID, 0, R.string.random_player);
		menu.add(0, RANDOM_CLUB_ID, 0, R.string.random_club);
		menu.add(0, RANDOM_TEAM_ID, 0, R.string.random_team);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case RANDOM_ALL_ID:
			randomPlayer(info.position);
			randomTeam(info.position, new Random().nextInt(CLUB_FACTOR));
			fillData();
			return true;
		case RANDOM_PLAYER_ID:
			randomPlayer(info.position);
			fillData();
			return true;
		case RANDOM_CLUB_ID:
			randomTeam(info.position, CLUB_FACTOR);
			fillData();
			return true;
		case RANDOM_TEAM_ID:
			randomTeam(info.position, TEAM_FACTOR);
			fillData();
			return true;
		}
		;

		return super.onContextItemSelected(item);
	}

	private void randomTeam(int position, int factor) {
		int team0, team1;
		Random randon = new Random();
		String[] teams;

		// club or national teams
		if (factor == TEAM_FACTOR) {
			teams = teamList;
		} else {
			teams = clubList;
		}

		team0 = randon.nextInt(teams.length);

		do {
			team1 = randon.nextInt(teams.length);
		} while (team0 == team1);

		SharedPreferences.Editor editor = settings.edit();

		editor.putString("game" + position + "team" + 0, teams[team0]);
		editor.putString("game" + position + "team" + 1, teams[team1]);

		// Don't forget to commit your edits!!!
		editor.commit();
	}

	private void randomPlayer(int position) {
		Random random = new Random();
		boolean home;
		int players[] = new int[NUM_PLAYERS];
		int sortedPlayer;

		home = random.nextBoolean();
		sortedPlayer = random.nextInt(NUM_PLAYERS - 1);

		switch (sortedPlayer) {
		case 0:
			if (home) {
				players[0] = 0;
				players[1] = 3;
				players[2] = 1;
				players[3] = 2;
			} else {
				players[0] = 1;
				players[1] = 2;
				players[2] = 0;
				players[3] = 3;
			}
			break;
		case 1:
			if (home) {
				players[0] = 1;
				players[1] = 3;
				players[2] = 0;
				players[3] = 2;
			} else {
				players[0] = 0;
				players[1] = 2;
				players[2] = 1;
				players[3] = 3;
			}
			break;
		case 2:
			if (home) {
				players[0] = 2;
				players[1] = 3;
				players[2] = 0;
				players[3] = 1;
			} else {
				players[0] = 0;
				players[1] = 1;
				players[2] = 2;
				players[3] = 3;
			}
			break;
		}

		SharedPreferences.Editor editor = settings.edit();

		for (int i = 0; i < NUM_PLAYERS; i++) {
			editor.putInt("game" + position + "player" + i, players[i]);
		}

		// Don't forget to commit your edits!!!
		editor.commit();
	}

	private void resetResults() {
		SharedPreferences.Editor editor = settings.edit();

		for (int i = 0; i < NUM_GAMES; i++) {
			editor.putInt("game" + i + "score0", NOT_STARTED);
			editor.putInt("game" + i + "socore1", NOT_STARTED);
			editor.putString("game" + i + "team0", TBD);
			editor.putString("game" + i + "team1", TBD);
			for (int j = 0; j < NUM_PLAYERS; j++) {
				editor.putInt("game" + i + "player" + j, NUM_PLAYERS);
			}
		}
		// Don't forget to commit your edits!!!
		editor.commit();

		fillData();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;

		switch (id) {
		case DIALOG_STANDINGS:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("STANDINGS");
			builder.setMessage(getStandings()).setCancelable(false)
					.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			dialog = builder.create();
			break;
		}

		return dialog;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, GameEdit.class);
		i.putExtra(KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}

	private void sendEmail(String subject, String body) {
		GMailSender sender = new GMailSender("fifamatch", "jogatina"); // SUBSTITUTE
		try {
			String recipients = email[0];
			for (int i = 1; i < NUM_PLAYERS; i++) {
				recipients = recipients.concat("," + email[i]);
			}
			sender.sendMail(subject, body
					+ "\nPowered by FIFA Match Android Beta",
					"fifamatch@gmail.com", recipients);
		} catch (Exception e) {
			Log.e("SendMail", e.getMessage(), e);
		}
	}

	private void sendFixture() {
		String fixture = "";

		for (int i = 0; i < NUM_GAMES; i++) {
			fixture = fixture.concat(String.format("%02d", i + 1) + " - ("
					+ playerName[playerID[i][0]] + "/"
					+ playerName[playerID[i][1]] + ") " + team[i][0] + " X "
					+ team[i][1] + " (" + playerName[playerID[i][2]] + "/"
					+ playerName[playerID[i][3]] + ")\n");
		}

		sendEmail("Next Tournament Fixture", fixture);
	}

	private void sendResults() {
		String results = "RESULTS\n\n";
		for (int i = 0; i < NUM_GAMES; i++) {
			results = results.concat(game[i] + "\n");
		}

		sendEmail("Last Tournament Results", results + "\nSTANDINGS\n\n"
				+ getStandings());
	}

	private String getStandings() {
		PlayerStanding[] playerStanding = new PlayerStanding[NUM_PLAYERS];

		for (int i = 0; i < NUM_PLAYERS; i++) {
			playerStanding[i] = new PlayerStanding();
			playerStanding[i].player = (playerName[i]);

		}
		for (int i = 0; i < NUM_GAMES; i++) {
			if ((score[i][0] != NOT_STARTED) && (playerID[i][0] != NUM_PLAYERS)) {
				if (score[i][0] == score[i][1]) {
					playerStanding[0].points++;
					playerStanding[1].points++;
					playerStanding[2].points++;
					playerStanding[3].points++;

				} else if (score[i][0] > score[i][1]) {
					playerStanding[playerID[i][0]].points = playerStanding[playerID[i][0]].points + 3;
					playerStanding[playerID[i][1]].points = playerStanding[playerID[i][1]].points + 3;
					playerStanding[playerID[i][0]].victories++;
					playerStanding[playerID[i][1]].victories++;
				} else {
					playerStanding[playerID[i][2]].points = playerStanding[playerID[i][2]].points + 3;
					playerStanding[playerID[i][3]].points = playerStanding[playerID[i][3]].points + 3;
					playerStanding[playerID[i][2]].victories++;
					playerStanding[playerID[i][3]].victories++;
				}

				playerStanding[playerID[i][0]].goalsPro = playerStanding[playerID[i][0]].goalsPro
						+ score[i][0];
				playerStanding[playerID[i][1]].goalsPro = playerStanding[playerID[i][1]].goalsPro
						+ score[i][0];
				playerStanding[playerID[i][2]].goalsPro = playerStanding[playerID[i][2]].goalsPro
						+ score[i][1];
				playerStanding[playerID[i][3]].goalsPro = playerStanding[playerID[i][3]].goalsPro
						+ score[i][1];

				playerStanding[playerID[i][0]].goalsBalance = playerStanding[playerID[i][0]].goalsBalance
						+ (score[i][0] - score[i][1]);
				playerStanding[playerID[i][1]].goalsBalance = playerStanding[playerID[i][1]].goalsBalance
						+ (score[i][0] - score[i][1]);
				playerStanding[playerID[i][2]].goalsBalance = playerStanding[playerID[i][2]].goalsBalance
						+ (score[i][1] - score[i][0]);
				playerStanding[playerID[i][3]].goalsBalance = playerStanding[playerID[i][3]].goalsBalance
						+ (score[i][1] - score[i][0]);
			}
		}

		// sort the results
		Arrays.sort(playerStanding);

		String standings = "PLAYER                P  V GB GP\n";
		for (int i = 0; i < NUM_PLAYERS; i++) {
			standings = standings
					+ String
							.format("%1$-" + 20 + "s", playerStanding[i].player)
					+ String.format("%1$" + 03 + "s", playerStanding[i].points)
					+ String.format("%1$" + 03 + "s",
							playerStanding[i].victories)
					+ String.format("%1$" + 03 + "s",
							playerStanding[i].goalsBalance)
					+ String.format("%1$" + 03 + "s",
							playerStanding[i].goalsPro) + "\n";
		}
		return standings;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_STANDINGS:
			((AlertDialog) (dialog)).setMessage(getStandings());
			break;
		}
		super.onPrepareDialog(id, dialog);
	}

	@SuppressWarnings( { "unchecked" })
	private class PlayerStanding implements Comparable {
		public Integer points = 0, victories = 0, goalsBalance = 0,
				goalsPro = 0;;
		public String player = "";

		@Override
		public int compareTo(Object arg0) {
			int result = this.points.compareTo(((PlayerStanding) arg0).points);

			if (result == 0) {
				result = this.victories
						.compareTo(((PlayerStanding) arg0).victories);

				if (result == 0) {
					result = this.goalsBalance
							.compareTo(((PlayerStanding) arg0).goalsBalance);

					if (result == 0) {
						result = this.goalsPro
								.compareTo(((PlayerStanding) arg0).goalsPro);
					}
				}
			}

			return result * -1;
		}
	}

	public static final String[] clubList = { "Arsenal", "Aston Villa",
			"Chelsea", "Liverpool", "Manchester City", "Manchester United",
			"Bordeaux", "Lyon", "OM", "Bayer Munichen", "Wolfsburg",
			"Fiorentina", "Inter", "Juventus", "Milan", "Roma", "Barcelona",
			"Real Madrid", "Sevilla", "Valencia", "Villareal",
			"Atletico Madrid" };

	public static final String[] teamList = { "Argentina", "Brasil",
			"Inglaterra", "Franca", "Alemanha", "Italia", "Holanda",
			"Portugal", "Espanha" };
};
