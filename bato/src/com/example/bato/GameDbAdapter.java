package com.example.bato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

<<<<<<< HEAD

	public class GameDbAdapter {
		//FIRST STEP CREATE THE VARS YOU NEED FOR THE DATABASE
	    private static final String DATABASE_NAME = "game_data"; //my database name
	    private static final String DATABASE_TABLE = "game"; //this particular table is the activities table. I might make a separate table for a ranking system. We will see. 
	    private static final int DATABASE_VERSION = 2;
	    public static final String COLUMN_NAME_TIME="Time";
	    public static final String COLUMN_NAME_RT = "RT";
	    public static final String COLUMN_NAME_SCORE = "Score";
	    public static final String COLUMN_NAME_TRIAL = "Trial";
	    public static final String COLUMN_NAME_POSITIVE_THOUGHT = "positive_thought";
	    public static final String COLUMN_NAME_NEGATIVE_THOUGHT = "negative_thought";
	    public static final String COLUMN_NAME_GAME_NUMBER = "GameNumber";
	    public static final String COLUMN_NAME_GAME_COMPLETE = "Game_Complete";
	    public static final String COLUMN_NAME_SUCCESS = "Success";
	    public static final String KEY_ROWID = "_id"; //all my vars are now declared 

	    private static final String TAG = "GameDbAdapter";
	    private DatabaseHelper mGameDbHelper;
	    private SQLiteDatabase mGameDb;
	    
	    private static final String DATABASE_CREATE =  //create the database! you already know!! // modified android code of text . I want to allow for null text! 
		        "create table game (_id integer primary key autoincrement, " +
		        "Time long, RT long, Score int, GameNumber int, Game_Complete text, Trial int, negative_thought text, positive_thought text, Success text)";
	 
	    
	    private final Context mGameCtx; //declare a context. activity extends from context. it's a basic part of android app. need to research this more. 

	    private static class DatabaseHelper extends SQLiteOpenHelper {

	        DatabaseHelper(Context context) 
	        {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db) {

	            db.execSQL(DATABASE_CREATE); //and here is where the database is created. because we declared the string DATA_BASE_CREATE above
	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //if the db gets upgraded. android docs say that the memory from the db is wiped. I need a workaround here!! 
	            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                    + newVersion + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS game");
	            onCreate(db);
	        }
	    }

	    /**
	     * Constructor - takes the context to allow the database to be
	     * opened/created
	     * 
	     * @param ctx the Context within which to work
	     */
	    public GameDbAdapter(Context ctx) {  //ActivitiyDbAdapter? Look into this one sahn. 
	        this.mGameCtx = ctx;
	    }

	    /**
	     * Open the notes database. If it cannot be opened, try to create a new
	     * instance of the database. If it cannot be created, throw an exception to
	     * signal the failure
	     * 
	     * @return this (self reference, allowing this to be chained in an
	     *         initialization call)
	     * @throws SQLException if the database could be neither opened or created
	     */
	    public GameDbAdapter open() throws SQLException {
	        mGameDbHelper = new DatabaseHelper(mGameCtx);
	        mGameDb = mGameDbHelper.getWritableDatabase();
	        return this;
	    }

	    public void close() {
	        mGameDbHelper.close();
	    }

	    

	    
	 
	    /**
	     * Create a new note using the title and body provided. If the note is
	     * successfully created return the new rowId for that note, otherwise return
	     * a -1 to indicate failure.
	     * 
	     * @param title the title of the note
	     * @param body the body of the note
	     * @return rowId or -1 if failed
	     */
	    
	    //now do a create, update, and fetch functions!!!
	    
	    public long createGame(long time, long RT, long Score, int game_number, String success, int Trial, String positive_thought, String negative_thought, String game_complete  ) {
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(COLUMN_NAME_TIME, time);
	        initialValues.put(COLUMN_NAME_RT, RT);
	        initialValues.put(COLUMN_NAME_SCORE, Score);
	        initialValues.put(COLUMN_NAME_GAME_NUMBER, game_number);
	        initialValues.put(COLUMN_NAME_GAME_COMPLETE, game_complete);
	        initialValues.put(COLUMN_NAME_TRIAL, Trial);
	        initialValues.put(COLUMN_NAME_NEGATIVE_THOUGHT, negative_thought);
	        initialValues.put(COLUMN_NAME_POSITIVE_THOUGHT, positive_thought);     
	        initialValues.put(COLUMN_NAME_SUCCESS, success);
	        return mGameDb.insert(DATABASE_TABLE, null, initialValues);
	    }
	    
	    

	    public Cursor fetchGame(int game_number)
	    {
	        return mGameDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,COLUMN_NAME_RT, COLUMN_NAME_TRIAL, COLUMN_NAME_GAME_NUMBER}, null, null, null, null, null);	        
	    }
	    
	    public Cursor fetchRT()
	    {
	        return mGameDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,COLUMN_NAME_RT, COLUMN_NAME_TRIAL, COLUMN_NAME_SUCCESS}, COLUMN_NAME_SUCCESS+" = ?", new String[] {"Yes"}, null, null, null, "9");
	        
	    }
	    
	    
	    public Cursor fetchGames()
	    {
	        return mGameDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, COLUMN_NAME_SUCCESS}, null, null, null, null, null);
	        
	    }
	    
	    
	    public Cursor fetchHighScores()
	    {
	        return mGameDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, COLUMN_NAME_SCORE}, COLUMN_NAME_GAME_COMPLETE+" = ?", new String[] {"Yes"}, null, null, COLUMN_NAME_SCORE +" DESC");    
	    }
	    

	    public Cursor fetchAll()
	    {
	        return mGameDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, COLUMN_NAME_TIME, COLUMN_NAME_RT,
	        		COLUMN_NAME_SCORE, COLUMN_NAME_GAME_NUMBER,
	        		COLUMN_NAME_GAME_COMPLETE, COLUMN_NAME_TRIAL,
	        		COLUMN_NAME_NEGATIVE_THOUGHT, COLUMN_NAME_POSITIVE_THOUGHT,
	        		COLUMN_NAME_SUCCESS}, null, null, null, null, null);

	    }
	   

	}
=======
public class GameDbAdapter
{
	//FIRST STEP CREATE THE VARS YOU NEED FOR THE DATABASE
    private static final String DATABASE_NAME = "game_data"; //my database name
    private static final String DATABASE_TABLE = "game"; //this particular table is the activities table. I might make a separate table for a ranking system. We will see. 
    private static final int DATABASE_VERSION = 2;
    public static final String COLUMN_NAME_TIME="Time";
    public static final String COLUMN_NAME_RT = "RT";
    public static final String COLUMN_NAME_SCORE = "Score";
    public static final String COLUMN_NAME_TRIAL = "Trial";
    public static final String COLUMN_NAME_POSITIVE_THOUGHT = "positive_thought";
    public static final String COLUMN_NAME_NEGATIVE_THOUGHT = "negative_thought";
    public static final String COLUMN_NAME_GAME_NUMBER = "GameNumber";
    public static final String COLUMN_NAME_GAME_COMPLETE = "Game_Complete";
    public static final String COLUMN_NAME_SUCCESS = "Success";
    public static final String KEY_ROWID = "_id"; //all my vars are now declared 

    private static final String TAG = "GameDbAdapter";
    private DatabaseHelper mGameDbHelper;
    private SQLiteDatabase mGameDb;
    
    private static final String DATABASE_CREATE =  //create the database! you already know!! // modified android code of text . I want to allow for null text! 
	        "create table game (_id integer primary key autoincrement, " +
	        "Time long, RT long, Score int, GameNumber int, Game_Complete text, Trial int, negative_thought text, positive_thought text, Success text)";
 
    
    private final Context mGameCtx; //declare a context. activity extends from context. it's a basic part of android app. need to research this more. 

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE); //and here is where the database is created. because we declared the string DATA_BASE_CREATE above
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //if the db gets upgraded. android docs say that the memory from the db is wiped. I need a workaround here!! 
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS activities");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public GameDbAdapter(Context ctx) {  //ActivitiyDbAdapter? Look into this one sahn. 
        this.mGameCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public GameDbAdapter open() throws SQLException {
        mGameDbHelper = new DatabaseHelper(mGameCtx);
        mGameDb = mGameDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mGameDbHelper.close();
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    
    //now do a create, update, and fetch functions!!!
    
    public long createGame(long time, long RT, long Score, int game_number, String success, int Trial, String positive_thought, String negative_thought, String game_complete  ) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME_TIME, time);
        initialValues.put(COLUMN_NAME_RT, RT);
        initialValues.put(COLUMN_NAME_SCORE, Score);
        initialValues.put(COLUMN_NAME_GAME_NUMBER, game_number);
        initialValues.put(COLUMN_NAME_GAME_COMPLETE, game_complete);
        initialValues.put(COLUMN_NAME_TRIAL, Trial);
        initialValues.put(COLUMN_NAME_NEGATIVE_THOUGHT, negative_thought);
        initialValues.put(COLUMN_NAME_POSITIVE_THOUGHT, positive_thought);     
        initialValues.put(COLUMN_NAME_SUCCESS, success);
        return mGameDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public Cursor fetchGame(int game_number)
    {
        return mGameDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,COLUMN_NAME_RT, COLUMN_NAME_TRIAL, COLUMN_NAME_GAME_NUMBER}, null, null, null, null, null);	        
    }
    
    public Cursor fetchRT()
    {
        return mGameDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,COLUMN_NAME_RT, COLUMN_NAME_TRIAL, COLUMN_NAME_SUCCESS}, COLUMN_NAME_SUCCESS+" = ?", new String[] {"Yes"}, null, null, null, "9");
        
    }
    
    public Cursor fetchGames()
    {
        return mGameDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, COLUMN_NAME_SUCCESS}, null, null, null, null, null);
        
    }
    
    public Cursor fetchHighScores()
    {
        return mGameDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, COLUMN_NAME_SCORE}, COLUMN_NAME_GAME_COMPLETE+" = ?", new String[] {"Yes"}, null, null, COLUMN_NAME_SCORE +" DESC");    
    }
}
>>>>>>> 60a1790c27f8fc860c93cb2e20f662c834c5e14a






