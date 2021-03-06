package com.samportnow.bato.capture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.samportnow.bato.MainActivity;
import com.samportnow.bato.R;
import com.samportnow.bato.database.BatoDataSource;
import com.samportnow.bato.database.dao.ThoughtDao;

public class CaptureActivity extends Activity
{
	private static final int THOUGHTS_CREATION_LIMIT = 3;

	private static Set<String> mNegativeWords;
	
	NegativeThought mNegativeThought;
	PositiveThought[] mPos = new PositiveThought[4];	
	LaserBeam[] mLaserBeam = new LaserBeam[4];
	BattleField mBattle;
	
	int mPosCounter;
	boolean laser_created;
	
	private AutoCompleteTextView mCreateChallengingTv;
	private String[] inputTokens;
	private Pattern four_letter_words = Pattern.compile("not|cant|cnt|can't");	
	
	private ThoughtDao mThought = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_capture);		
		
		BatoDataSource dataSource = new BatoDataSource(CaptureActivity.this).open();
		
		int negativeType = getIntent().getIntExtra("negative_type", Integer.MAX_VALUE);
		List<ThoughtDao> thoughts = dataSource.getNegativeThoughts(negativeType);				
		
		mThought = thoughts.get((int) (Math.random() * thoughts.size()));
		
		// TODO: define default negative thought; store into strings.xml
		final String thoughtContent =
			(mThought != null)
				?  mThought.getContent()
				: "Default negative thought here."; 
		
		((TextView) findViewById(R.id.capture_game_begin_thought_content)).setText("\"" + thoughtContent + "\"");
		
		findViewById(R.id.capture_game_begin_overlay).setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{			
				v.setVisibility(View.GONE);
				
				mNegativeThought = new NegativeThought(CaptureActivity.this);
				mNegativeThought.setText(thoughtContent);
				
				AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
				
				animation.setDuration(2000);
				animation.setFillAfter(true);
				
				LinearLayout container = (LinearLayout) findViewById(R.id.create_thought_container);
				
				container.setVisibility(View.VISIBLE);
				container.startAnimation(animation);				
			}
		});		

		// Game-related functionality below!
		
		populatePositiveWords(this);
		
		ArrayAdapter<String> historyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		historyAdapter.addAll(dataSource.getAllChallengingThoughtContent());
		
		dataSource.close();

		mCreateChallengingTv = (AutoCompleteTextView) findViewById(R.id.thoughts);
		mCreateChallengingTv.setAdapter(historyAdapter);	
		
		// the battle field!
		mBattle = (BattleField) findViewById(R.id.battle_field);

		final Button mCreateThought = (Button) findViewById(R.id.scale_it);

		if (mPosCounter == THOUGHTS_CREATION_LIMIT)
			mCreateThought.setEnabled(false);

		// set an onclicklistener for the create button
		// make sure that there are no neg words, negating statements
		mCreateThought.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				final String inputLine = mCreateChallengingTv.getText().toString();
				inputTokens = inputLine.split(" ");

				if (inputLine.isEmpty())
				{
					Toast.makeText(arg0.getContext(), "You have to write something!", Toast.LENGTH_SHORT).show();
					return;
				}

				if (inputTokens.length < 3)
				{
					Toast.makeText(arg0.getContext(), "At least three words are required.", Toast.LENGTH_SHORT).show();
					return;
				}

				if (four_letter_words.matcher(inputLine).find() == true)
				{
					Toast.makeText(arg0.getContext(), "Make an affirmative statement!", Toast.LENGTH_SHORT).show();
					return;
				}

				boolean matchesToken = false;

				for (int i = 0; i < inputTokens.length; i++)
				{
					String token = inputTokens[i];
					if (mNegativeWords.contains(token.toLowerCase(Locale.US)))
					{
						matchesToken = true;
						break;
					}
				}

				if (matchesToken == true)
				{
					Toast.makeText(arg0.getContext(), "Use positive words!", Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					mCreateThought.setEnabled(false);

					mPos[mPosCounter] = new PositiveThought(arg0.getContext(), null, inputLine);
					mLaserBeam[mPosCounter] = new LaserBeam(arg0.getContext(), mPosCounter);
					mPos[mPosCounter].setText(inputLine);
					laser_created = true;

					final AlertDialog.Builder builder = new AlertDialog.Builder(CaptureActivity.this);
					
					final View view = getLayoutInflater().inflate(R.layout.believe_dialog, null);
					builder.setView(view);
					
					builder.setTitle("Rate your thought");
					builder.setCancelable(false);
					
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							final long created = Calendar.getInstance().getTimeInMillis();
							final String content = inputLine;
							final int believe = ((SeekBar) view.findViewById(R.id.believe)).getProgress();
							final int helpful = ((SeekBar) view.findViewById(R.id.help)).getProgress();
							final long thoughtId = mThought.getId();
							
							BatoDataSource dataSource = new BatoDataSource(CaptureActivity.this).open();
							
							dataSource.createChallengingThought(created, content, believe, helpful, thoughtId);
							dataSource.close();
							
							mBattle.xLessBound[1] += mBattle.container_width / 12;
							mBattle.xGreatBound[1] -= mBattle.container_width / 12;
							mBattle.mSetX=true;

							if (mBattle.xVelocity < 0)
							{
								mBattle.xVelocity += 2;
							}
							else
							{
								mBattle.xVelocity -= 2;

							}
							if (mBattle.yVelocity < 0)
							{
								mBattle.yVelocity += 2;
							}
							else
							{
								mBattle.yVelocity -= 2;

							}
							if (mPosCounter >= THOUGHTS_CREATION_LIMIT)
							{
								mBattle.xVelocity = 5;
								for (int i = 0; i < 3; i++)
								{
									mLaserBeam[i].mGameOver = true;
								}
								mBattle.mGameOver = true;
							}
							else
							{
								Toast.makeText(CaptureActivity.this, "Great job! Come up with another thought!", Toast.LENGTH_SHORT).show();

							}
						}

					});
					
					builder.create().show();
					
					mCreateThought.setEnabled(true);
					mCreateChallengingTv.setText(null);
					InputMethodManager imm = (InputMethodManager) CaptureActivity.this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mCreateChallengingTv.getWindowToken(), 0);
					mPosCounter += 1;
				}
			}
		});
	}

	// TODO: this is being called MULTIPLE times from PositiveThought::draw_it()!
	public void endGame()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Great Job!");
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				
				Intent intent = new Intent(CaptureActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				
				startActivity(intent);
			}
		});

		builder.create().show();
	}

	public static boolean populatePositiveWords(Context context)
	{
		mNegativeWords = new HashSet<String>();

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("negative_words.txt")));
			String line = reader.readLine();

			while (line != null)
			{
				mNegativeWords.add(line.toLowerCase(Locale.US));
				line = reader.readLine();
			}

			reader.close();
		}
		catch (IOException exception)
		{
			return false;
		}

		return true;
		// TODO list of negative words
	}	
}
