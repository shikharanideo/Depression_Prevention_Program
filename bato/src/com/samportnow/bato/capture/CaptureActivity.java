package com.samportnow.bato.capture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.samportnow.bato.MainActivity;
import com.samportnow.bato.R;
import com.samportnow.bato.database.CalendarDbAdapter;

public class CaptureActivity extends Activity
{
	private static final int THOUGHTS_CREATION_LIMIT = 3;

	private static Set<String> mNegativeWords;
	
	private HorizontalScrollView mTrainHsv;
	
	RelativeLayout container;
	NegativeThought mNeg;
	PositiveThought[] mPos = new PositiveThought[4];
	LaserBeam[] mLaserBeam = new LaserBeam[4];
	int width;
	BattleField mBattle;
	int mPosCounter;
	AutoCompleteTextView mChallengingThought;
	AlphaAnimation mGone;
	private String[] inputTokens;
	private Pattern four_letter_words = Pattern.compile("not|cant|cnt|can't");
	boolean laser_created;
	
	private String[] mCategoryTitles = null;
	private String[] mCategoryDescriptions = null;
	
	//private List<View.OnClickListener> mTraincarListeners = null;

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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_capture);

		mCategoryTitles = getResources().getStringArray(R.array.add_event_user_category_titles);
		mCategoryDescriptions = getResources().getStringArray(R.array.add_event_user_category_descriptions);
		
		final List<View.OnClickListener> traincarListeners = new ArrayList<View.OnClickListener>(mCategoryTitles.length);
		
		for (int i = 0; i < mCategoryTitles.length; i++)		
		{		
			ViewGroup trainVg = (ViewGroup) findViewById(R.id.train);
			View view = getLayoutInflater().inflate(R.layout.block_traincar, trainVg, false);
			
			((TextView) view.findViewById(R.id.traincar_title)).setText(mCategoryTitles[i]);
			((TextView) view.findViewById(R.id.traincar_description)).setText(mCategoryDescriptions[i]);
			
			View.OnClickListener listener = new View.OnClickListener()
			{				
				@Override
				public void onClick(View v)
				{
					int index = traincarListeners.indexOf(this);
					
					Toast.makeText(v.getContext(), mCategoryTitles[index] + " selected!", Toast.LENGTH_SHORT).show();				
				}
			};
			
			view.setOnClickListener(listener);
			traincarListeners.add(listener);
			
			trainVg.addView(view);
		}
		
//		mCategoryDescriptionTextViews = new ArrayList<TextView>(11);
//
//		Context mContext = this;
//		// layout inflater
//		final LayoutInflater inflater = LayoutInflater.from(this);
//		populatePositiveWords(mContext);
//
//		// Cursor cursor = mCalHelper.
//		// set content view
//
//		CalendarDbAdapter mCalHelper = new CalendarDbAdapter(mContext);
//		mCalHelper.open();
//		Cursor cursor = mCalHelper.fetchAllChallenging();
//		ArrayList<String> mChallengingThoughts = new ArrayList<String>();
//		while (cursor.moveToNext())
//		{
//			String mThought = cursor.getString(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_COUNTER_THOUGHT));
//			if (!mChallengingThoughts.contains(mThought))
//			{
//				Log.e("it is", "" + mThought);
//				mChallengingThoughts.add(mThought);
//			}
//			else
//			{
//				Log.e("well here is", "" + mThought);
//			}
//		}
//		cursor.close();
//		mCalHelper.close();
//		// the autocompletetextview for creating challenging thoughts
//		mChallengingThought = (AutoCompleteTextView) findViewById(R.id.thoughts);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mChallengingThoughts);
//		mChallengingThought.setAdapter(adapter);
//		// the relativelayout that will contain the "dancing" cloud
//		container = (RelativeLayout) findViewById(R.id.container);
//		// the scroll that contains the train that will jump into the view
//		// width of the view
//		Display display = this.getWindowManager().getDefaultDisplay();
//		Point size = new Point();
//		display.getSize(size);
//		// need width to animate the train. it needs to moved off the screen
//		width = size.x;
//		mTrainHsv = (HorizontalScrollView) findViewById(R.id.scroller);
//		// the battle field!
//		mBattle = (BattleField) findViewById(R.id.battle_field);	

		
//		// the layout that contains the edit text and button, set to invisible
//		// at the begining
//		// setup the animation for the train coming in
//		TranslateAnimation mSlide = new TranslateAnimation(width, 0, 0, 0);
//		mSlide.setDuration(100);
//		mSlide.setFillAfter(true);
//		mTrainHsv.setAnimation(mSlide);
//		mTrainHsv.startAnimation(mSlide);
//		// the animation listener that will remove the train
//		AnimationListener remove = new AnimationListener()
//		{
//
//			@Override
//			public void onAnimationEnd(Animation arg0)
//			{
//				container.removeView(mTrainHsv);
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation arg0)
//			{
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onAnimationStart(Animation arg0)
//			{
//				// TODO Auto-generated method stub
//
//			}
//
//		};		
//		
//		// set up the other animations
//		mGone = new AlphaAnimation(1.0f, 0.0f);
//		mGone.setAnimationListener(remove);
//		mGone.setDuration(2000);
//		mGone.setFillAfter(true);
		
//		OnClickListener startGameListener = new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				int position = mCategoryDescriptionTextViews.indexOf(v);
//				String categoryTitle = mCategoryTitles[position];				
//
//				CalendarDbAdapter calendarDbAdapter = new CalendarDbAdapter(v.getContext()).open();				
//				Cursor cursor = calendarDbAdapter.fetchNegsByType(categoryTitle);
//
//				if (cursor.moveToFirst())
//				{
//					String mNegThought = cursor.getString(cursor.getColumnIndexOrThrow(CalendarDbAdapter.COLUMN_NAME_NEGATIVE_THOUGHT));
//					// the thought that will dance on the screen
//					mNeg = new NegativeThought(v.getContext());
//					mNeg.setText(mNegThought);
//
//					mTrainHsv.startAnimation(mGone);
//					AlphaAnimation mGo = new AlphaAnimation(0.0f, 1.0f);
//					mGo.setDuration(2000);
//					mGo.setFillAfter(true);
//					mGo.setDuration(2000);
//					LinearLayout mEditTextContainer = (LinearLayout) findViewById(R.id.edit_container);
//					mEditTextContainer.setVisibility(View.VISIBLE);
//					mEditTextContainer.startAnimation(mGo);
//					// should set button clickable here
//				}
//				else
//				{
//					Toast.makeText(v.getContext(), "No negative thoughts in this train", Toast.LENGTH_SHORT).show();
//				}
//
//				cursor.close();
//				calendarDbAdapter.close();
//			}
//
//		};

//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.mind_reading));
//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.fortune_telling));
//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.catastrophizing));
//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.labeling));
//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.dark_glass));
//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.discounting_positives));
//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.black_and_white_thinking));
//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.overgeneralizing));
//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.personalizing));
//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.shoulds));
//		mCategoryDescriptionTextViews.add((TextView) findViewById(R.id.unfair_comparisons));

//		for (TextView textView : mCategoryDescriptionTextViews)
//			textView.setOnClickListener(startGameListener);

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
				String inputLine = mChallengingThought.getText().toString();
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

					final String mChallenging = mChallengingThought.getText().toString();
					mPos[mPosCounter] = new PositiveThought(arg0.getContext(), null, inputLine);
					mLaserBeam[mPosCounter] = new LaserBeam(arg0.getContext(), mPosCounter);
					mPos[mPosCounter].setText(inputLine);
					laser_created = true;
					final Context mText = arg0.getContext();
					AlertDialog.Builder build_believe = new AlertDialog.Builder(mText);
					final View view = getLayoutInflater().inflate(R.layout.believe_dialog, null);
					build_believe.setView(view);
					build_believe.setTitle("Rate your thought");
					build_believe.setCancelable(false);
					build_believe.setPositiveButton("OK", new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							CalendarDbAdapter mCalHelper = new CalendarDbAdapter(CaptureActivity.this.getApplicationContext());
							mCalHelper.open();
							int belief = ((SeekBar) view.findViewById(R.id.believe)).getProgress();
							int helpful = ((SeekBar) view.findViewById(R.id.help)).getProgress();
							mCalHelper.createChallenging(mNeg.getText().toString(), mChallenging, belief, helpful);
							mCalHelper.close();
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
								Toast.makeText(mText, "Great job! Come up with another thought!", Toast.LENGTH_SHORT).show();

							}
						}

					});
					build_believe.create().show();
					mCreateThought.setEnabled(true);
					mChallengingThought.setText(null);
					InputMethodManager imm = (InputMethodManager) CaptureActivity.this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mChallengingThought.getWindowToken(), 0);
					mPosCounter += 1;
				}
			}
		});

		SharedPreferences preferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
		if (preferences.getString("capture instructions", null) == null)
		{
			CaptureTutorialFragment tutorialFragment = new CaptureTutorialFragment();
			
			tutorialFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
			tutorialFragment.show(getFragmentManager(), null);			

			// preferences.edit().putString("capture instructions",
			// "Yes").commit();
			// change this after the presentation
		}

	}

	public void endGame()
	{
		final Context mContext = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Great Job!");
		builder.setNegativeButton("Go Home", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				{
					dialog.dismiss();
					finish();
					Intent i = new Intent(mContext, MainActivity.class);
					mContext.startActivity(i);
				}

			}

		});

		builder.setPositiveButton("Play again!", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Context mContext = CaptureActivity.this.getApplicationContext();
				Intent i = new Intent(mContext, CaptureActivity.class);
				mContext.startActivity(i);
			}

		});

		builder.create().show();
	}

}