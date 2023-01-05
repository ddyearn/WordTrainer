package ddwucom.mobile.ma02_20200937;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class WordDBManager {

    WordDBHelper wordDBHelper;
    Cursor cursor = null;

    public WordDBManager(Context context) {wordDBHelper = new WordDBHelper(context); }

    //모든 word 반환
    public ArrayList<Word> getAllWord() {
        ArrayList wordList = new ArrayList();
        SQLiteDatabase db = wordDBHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + WordDBHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {

            long id = cursor.getColumnIndex(WordDBHelper.COL_ID);
            String name = cursor.getString(1);
            String meaning = cursor.getString(2);
            String status = cursor.getString(3);

            wordList.add(new Word(id, name, meaning, status));
            // cursor.getColumnIndex(WordDBHelper.COL_ID)
        }
        close();

        return wordList;
    }

    // 새로운 word 추가
    public boolean addNewWord(Word newWord) {
        SQLiteDatabase db = wordDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(WordDBHelper.COL_NAME, newWord.getName());
        value.put(WordDBHelper.COL_MEAN, newWord.getMeaning());
        value.put(WordDBHelper.COL_STATUS, newWord.getStatus());

        long count = db.insert(WordDBHelper.TABLE_NAME, null, value);

        close();

        if(count > 0) return true;
        return false;
    }

    // name을 기준으로 word의 정보 변경
    public boolean modifyWord(Word word) {
        SQLiteDatabase sqLiteDatabase = wordDBHelper.getWritableDatabase();

        ContentValues row = new ContentValues();
        row.put(WordDBHelper.COL_NAME, word.getName());
        row.put(WordDBHelper.COL_MEAN, word.getMeaning());

        String whereClause = WordDBHelper.COL_NAME + "=?";
        String[] whereArgs = new String[] { word.getName() };

        int result = sqLiteDatabase.update(WordDBHelper.TABLE_NAME, row, whereClause, whereArgs);

        close();
        if(result > 0) return true;
        return false;
    }

    // name 을 기준으로 DB에서 word 삭제
    public boolean removeWord(String name) {
        SQLiteDatabase sqLiteDatabase = wordDBHelper.getWritableDatabase();
        String whereClause = WordDBHelper.COL_NAME + "=?";
        String[] whereArgs = new String[] { name };
        int result = sqLiteDatabase.delete(WordDBHelper.TABLE_NAME, whereClause, whereArgs);
        close();
        if(result > 0) return true;
        return false;
    }

    // DB 검색
    public ArrayList<Word> searchWordByText(String text) {
        ArrayList wordList = new ArrayList();
        SQLiteDatabase db = wordDBHelper.getReadableDatabase();
        // name과 meaning 둘다에서 찾음
        String selection = WordDBHelper.COL_NAME + " LIKE ? OR " + WordDBHelper.COL_MEAN + " LIKE ?";
        String[] selectArgs = new String[]{"%" + text + "%", "%" + text + "%"};

        cursor = db.query(WordDBHelper.TABLE_NAME, null, selection, selectArgs,
                null, null, null, null);
        while (cursor.moveToNext()) {
            long id = cursor.getColumnIndex(WordDBHelper.COL_ID);
            String name = cursor.getString(1);
            String meaning = cursor.getString(2);
            String status = cursor.getString(3);

            wordList.add(new Word(id, name, meaning, status));
            // cursor.getColumnIndex(WordDBHelper.COL_ID)
        }
        return wordList;
    }

    // close 수행
    public void close() {
        if (wordDBHelper != null) wordDBHelper.close();
        if (cursor != null) cursor.close();
    };
}
