package ddwucom.mobile.ma02_20200937;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class WordDBHelper extends SQLiteOpenHelper {

    final static String TAG = "WordDBHelper";
    final static String DB_NAME = "words.db";
    public final static String TABLE_NAME = "word_table";
    public static String COL_ID = "_id";
    public final static String COL_NAME = "name";
    public final static String COL_MEAN = "meaning";
    public final static String COL_STATUS = "status";

    public WordDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " +
                COL_NAME + " TEXT, " + COL_MEAN + " TEXT, " + COL_STATUS + " TEXT)";
        Log.d(TAG, sql);
        sqLiteDatabase.execSQL(sql);
        insertSample(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void insertSample(SQLiteDatabase db) {
        db.execSQL("insert into " + TABLE_NAME + " values (null, 'recoup', '만회하다', 'new');");
        db.execSQL("insert into " + TABLE_NAME + " values (null, 'sensuous', '육감적인', 'new');");
    }
}
