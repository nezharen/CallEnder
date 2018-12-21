package nezharen.space.callender;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.regex.Pattern;

public class DataBase {

    private static int VERSION = 1;
    private static String TAG = "DataBase";
    private static DataBase uniqueInstance;
    private static SQLiteDatabase db;

    private DataBase(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, context.getString(R.string.database_name), null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public static synchronized DataBase getInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new DataBase(context);
        }
        return uniqueInstance;
    }

    public boolean insertNumber(String number) {
        if (existNumber(number)) {
            Log.v(TAG, "Skip intertion due to existence.");
            return false;
        }
        String sql = "insert into number_tb (number) values ('" + number + "')";
        Log.v(TAG, sql);
        db.execSQL(sql);
        return true;
    }

    public boolean existNumber(String number) {
        Cursor c = db.rawQuery("select * from number_tb where number=?", new String[]{number});
        if (c.getCount() > 0) {
            return true;
        }
        return false;
    }

    public boolean matchNumber(String number) {
        Cursor c = getAllNumbers();
        if (c.moveToFirst()) {
            while (true) {
                if (Pattern.matches(c.getString(c.getColumnIndex("number")), number)) {
                    return true;
                }
                if (c.isLast()) {
                    break;
                }
                c.moveToNext();
            }
        }
        return false;
    }

    public boolean deleteNumber(String number) {
        String sql = "delete from number_tb where number='" + number + "'";
        Log.v(TAG, sql);
        db.execSQL(sql);
        return true;
    }

    public Cursor getAllNumbers() {
        return db.rawQuery("select * from number_tb", new String[]{});
    }
}
