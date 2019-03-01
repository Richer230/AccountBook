package activitytest.example.com.accountbook2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class YearBill extends AppCompatActivity {

    private MyDate myDate = new MyDate();
    private List<YearCostBean > mYearList;
    private List<Double> incomeList;
    private List<Double> expandList;
    private List<Double> moneyList;
    private YearBillAdapter billAdapter;
    private StickyListHeadersListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_bill);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("年账单");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        incomeList = new ArrayList<>();
        expandList = new ArrayList<>();
        listView = findViewById(R.id.test);
        mYearList = getAvailableMonth();
        moneyList = getYearBill(mYearList);
        billAdapter = new YearBillAdapter(this,mYearList,incomeList,expandList);
        listView.setAdapter(billAdapter);
    }

    public List getAvailableMonth(){

        List<String> mList = new ArrayList<>();
        List<YearCostBean> mListYear = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getAllCostData();
        if(cursor!=null){
            while(cursor.moveToNext()){
                if (!mList.contains(cursor.getString(cursor.getColumnIndex("cost_month")))){
                    mList.add(cursor.getString(cursor.getColumnIndex("cost_month")));
                    YearCostBean costBean = new YearCostBean();
                    costBean.setCostMonth(cursor.getString(cursor.getColumnIndex("cost_month")));
                    costBean.setCostYear(myDate.getYear(cursor.getLong(cursor.getColumnIndex("cost_time"))));
                    mListYear.add(costBean);
                }
            }
        }
        return mListYear;
    }

    public List getYearBill(List<YearCostBean> mList){
        List<Double> mListMoney = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getAllCostData();
        for(int i=0;i<mList.size();i++){
            mListMoney.add(0.00);
            incomeList.add(0.00);
            expandList.add(0.00);
            cursor.moveToFirst();
            do{
                //Log.d("month: ", mList.get(i));
                if(cursor.getString(cursor.getColumnIndex("cost_month")).equals(mList.get(i).getCostMonth())){
                    mListMoney.set(i,mListMoney.get(i) + Double.parseDouble(cursor.getString(cursor.getColumnIndex("cost_money"))));
                    if(cursor.getInt(cursor.getColumnIndex("cost_type"))==1){
                        expandList.set(i,expandList.get(i)+Double.parseDouble(cursor.getString(cursor.getColumnIndex("cost_money"))));
                    }else{
                        incomeList.set(i,incomeList.get(i)+Double.parseDouble(cursor.getString(cursor.getColumnIndex("cost_money"))));
                    }
                }
            }while(cursor.moveToNext());

        }
        return mListMoney;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.yearbill_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.month:
                intent = new Intent(YearBill.this,MonthBill.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
