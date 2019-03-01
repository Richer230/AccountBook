package activitytest.example.com.accountbook2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String COST_TITLE = "cost_title";
    public static final String COST_MONEY = "cost_money";
    public static final String COST_DATE = "cost_date";
    public static final String IMOOC_COST = "Cost";
    public static final String COST_TIME = "cost_time";

    public DatabaseHelper(Context context) {
        super(context, "Cost", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Cost(" +
                "id integer primary key autoincrement, " +
                "cost_title text, " +
                "cost_date date, " +
                "cost_month date, " +
                "cost_time integer, " +
                "cost_type integer, " +
                "cost_id text, " +
                "cost_money text)");
    }

    public void insertCost(CostBean costBean){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COST_TITLE,costBean.getCostTitle());
        cv.put(COST_DATE,costBean.getCostDate());
        cv.put(COST_MONEY,costBean.getCostMoney());
        cv.put(COST_TIME,costBean.getCostTime());
        cv.put("cost_type",costBean.getCostType());
        cv.put("cost_id",costBean.getUuid());
        cv.put("cost_month",costBean.getCostMonth());
        database.insert(IMOOC_COST,null,cv);
    }

    public Cursor getAllCostData(){
        SQLiteDatabase database = getWritableDatabase();
        return database.query(IMOOC_COST,null,null,null,null,null,"cost_time" + " DESC");
    }

    public void deleteAllData(){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(IMOOC_COST,null,null);
    }

    public void deleteData(String uuid){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(IMOOC_COST,"cost_id = ?", new String[]{uuid});
    }

    public void editData(String uuid,CostBean costBean){
        deleteData(uuid);
        costBean.setUuid(uuid);
        insertCost(costBean);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
