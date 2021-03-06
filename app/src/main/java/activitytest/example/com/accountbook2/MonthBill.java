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

import java.io.DataOutput;
import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MonthBill extends AppCompatActivity {


    private MyDate myDate  = new MyDate();
    private List<MonthCostBean > dateList;
    private List<Double> moneyList;
    private List<Double> incomeList;
    private List<Double> expandList;
    private MonthBillAdapter billAdapter;
    private StickyListHeadersListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_bill);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("月账单");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        incomeList = new ArrayList<>();
        expandList = new ArrayList<>();
        dateList = getAvailableDate();
        moneyList = getMonthBill(dateList);
        listView =  (StickyListHeadersListView) findViewById(R.id.test);
        billAdapter = new MonthBillAdapter(this,dateList,incomeList,expandList);
        listView.setAdapter(billAdapter);
    }


    public List getAvailableDate(){
        List<String> mListDate = new ArrayList<>();
        List<MonthCostBean> mListCostBean = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getAllCostData();
        if(cursor!=null){
            while(cursor.moveToNext()){
                if (!mListDate.contains(cursor.getString(cursor.getColumnIndex("cost_date")))){
                    mListDate.add(cursor.getString(cursor.getColumnIndex("cost_date")));
                    MonthCostBean costBean = new MonthCostBean();
                    costBean.setCostDate(cursor.getString(cursor.getColumnIndex("cost_date")));
                    costBean.setCostYear((myDate.getYear(cursor.getLong(cursor.getColumnIndex("cost_time")))));
                    costBean.setCostMonth(myDate.getMonth(cursor.getLong(cursor.getColumnIndex("cost_time"))));
                    mListCostBean.add(costBean);
                }
            }
        }
        return mListCostBean;
    }

    public List getMonthBill(List<MonthCostBean> mList){
        List<Double> mListMoney = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getAllCostData();
        for(int i=0;i<mList.size();i++){
            mListMoney.add(0.00);
            incomeList.add(0.00);
            expandList.add(0.00);
            cursor.moveToFirst();
            do{
                if(cursor.getString(cursor.getColumnIndex("cost_date")).equals(mList.get(i).getCostDate())){
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
        getMenuInflater().inflate(R.menu.monthbill_menu, menu);
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
            case R.id.year:
                intent = new Intent(MonthBill.this,YearBill.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
