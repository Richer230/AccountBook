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


    public DatabaseHelper(Context context) {
        super(context, "Cost", null, 4);
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
                "cost_remark text, " +
                "cost_id text, " +
                "cost_money text)");
    }

    public void insertCost(CostBean costBean){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("cost_title",costBean.getCostTitle());
        cv.put("cost_date",costBean.getCostDate());
        cv.put("cost_money",costBean.getCostMoney());
        cv.put("cost_time",costBean.getCostTime());
        cv.put("cost_type",costBean.getCostType());
        cv.put("cost_id",costBean.getUuid());
        cv.put("cost_month",costBean.getCostMonth());
        cv.put("cost_remark",costBean.getCostRemark());
        database.insert("Cost",null,cv);
    }

    public Cursor getAllCostData(){
        SQLiteDatabase database = getWritableDatabase();
        return database.query("Cost",null,null,null,null,null,"cost_time" + " DESC");
    }

    public void deleteAllData(){
        SQLiteDatabase database = getWritableDatabase();
        database.delete("Cost",null,null);
    }

    public void deleteData(String uuid){
        SQLiteDatabase database = getWritableDatabase();
        database.delete("Cost","cost_id = ?", new String[]{uuid});
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
