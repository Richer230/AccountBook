package activitytest.example.com.accountbook2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private List<CostBean> mCostBeanList;
    private DatabaseHelper mDatabaseHelper;
    private CostListAdapter adapter;
    private String Title;
    private int Type;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> typesList;
    private String editTitle;
    private TextView newCostTile;
    private Button titleButton;
    private Spinner titleSpinner;
    private MyDate myDate;
    private StickyListHeadersListView stickyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("首页");
        stickyList =  (StickyListHeadersListView) findViewById(R.id.test);
        typesList = new ArrayList<>();
        mDatabaseHelper = new DatabaseHelper(this);
        mCostBeanList = new ArrayList<>();
        initCostData();
        adapter = new CostListAdapter(this, mCostBeanList);
        stickyList.setAdapter(adapter);
        initView();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View viewDialog = inflater.inflate(R.layout.new_cost_data,null);
                final RadioGroup typeGroup = (RadioGroup) viewDialog.findViewById(R.id.radio_group);
                final EditText money = (EditText) viewDialog.findViewById(R.id.et_cost_money);
                final DatePicker date = (DatePicker) viewDialog.findViewById(R.id.dp_cost_date);
                final TimePicker time = (TimePicker) viewDialog.findViewById(R.id.tp_cost_time);
                titleSpinner = viewDialog.findViewById(R.id.sp_cost_title);
                typeGroup.check(R.id.rb_expend);
                Type = 1;
                initSpinner();

                typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.rb_expend:
                                Type = 1;
                                initSpinner();
                                Title = (String) titleSpinner.getItemAtPosition(0);
                                break;
                            case R.id.rb_income:
                                Type = 2;
                                initSpinner();
                                Title = (String) titleSpinner.getItemAtPosition(0);
                                break;
                        }
                    }
                });
                arrayAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, typesList);
                arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                titleSpinner.setAdapter(arrayAdapter);
                titleSpinner.setPrompt("请选择种类" );
                titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Title = typesList.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                money.setText("50.00");
                builder.setView(viewDialog);
                builder.setTitle("记一笔");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        CostBean costBean = new CostBean();
                        costBean.setCostTitle(Title);
                        if(Type==1){
                            if(Double.parseDouble(money.getText().toString())<=0){
                                costBean.setCostMoney(Double.parseDouble(money.getText().toString()));
                            }else{
                                costBean.setCostMoney(- Double.parseDouble(money.getText().toString()));
                            }
                        }else if (Type==2){
                            if (Double.parseDouble(money.getText().toString())<=0){
                                costBean.setCostMoney(-Double.parseDouble(money.getText().toString()));
                            }else{
                                costBean.setCostMoney(Double.parseDouble(money.getText().toString()));
                            }
                        }
                        Calendar c = Calendar.getInstance();
                        c.set(date.getYear(),date.getMonth(),date.getDayOfMonth(),time.getHour(),time.getMinute());
                        costBean.setCostDate(date.getYear() + "-" + (date.getMonth()+1) + "-" + date.getDayOfMonth());
                        costBean.setCostMonth(date.getYear()+"-" + (date.getMonth()+1));
                        costBean.setCostType(Type);
                        costBean.setCostTime(c.getTimeInMillis());
                        costBean.setUuid(UUID.randomUUID().toString());
                        mDatabaseHelper.insertCost(costBean);
                        mCostBeanList.add(costBean);
                        reload();
                    }
                });

                builder.setNegativeButton("取消",null);
                builder.create().show();
            }

        });

    }

    public void reload(){
        mCostBeanList.clear();
        initCostData();
        stickyList.setAdapter(adapter);
    }

    private void initView(){
        stickyList = findViewById(R.id.test);
        stickyList.setOnItemLongClickListener(this);

    }

    private void showMyDialog(int index){
        final String[] options = {"删除","编辑","清空"};

        final CostBean selectedCostBean = mCostBeanList.get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this,options[which]+"Clicked",Toast.LENGTH_LONG).show();
                if (which==0){
                    String uuid = selectedCostBean.getUuid();
                    mDatabaseHelper.deleteData(uuid);
                    reload();
                }else if(which==1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    View viewDialog = inflater.inflate(R.layout.new_cost_data,null);
                    final CostBean newCostBean = new CostBean();
                    RadioGroup editGroup = viewDialog.findViewById(R.id.radio_group);
                    final EditText editMoney = viewDialog.findViewById(R.id.et_cost_money);
                    final Spinner editSpinner = viewDialog.findViewById(R.id.sp_cost_title);
                    final DatePicker editDate = viewDialog.findViewById(R.id.dp_cost_date);
                    final TimePicker editTime = viewDialog.findViewById(R.id.tp_cost_time);
                    Type = selectedCostBean.getCostType();
                    initSpinner();
                    editSpinner.setPrompt("请选择类别");
                    arrayAdapter = new ArrayAdapter<String>(viewDialog.getContext(),android.R.layout.simple_list_item_1,typesList);
                    editSpinner.setAdapter(arrayAdapter);
                    editSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            editTitle = typesList.get(position);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if(Type==1){
                        editGroup.check(R.id.rb_expend);
                    }else{
                        editGroup.check(R.id.rb_income);
                    }

                    editMoney.setText(String.valueOf(selectedCostBean.getCostMoney()));
                    for(int i=0;i<typesList.size();i++){
                        if(typesList.get(i).equals(selectedCostBean.getCostTitle())){
                            editSpinner.setSelection(i);
                        }
                    }
                    editGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch (checkedId){
                                case R.id.rb_expend:
                                    newCostBean.setCostType(1);
                                    Type = 1;
                                    initSpinner();
                                    editTitle = (String) editSpinner.getItemAtPosition(0);
                                    break;
                                case R.id.rb_income:
                                    newCostBean.setCostType(2);
                                    Type = 2;
                                    initSpinner();
                                    editTitle = (String) editSpinner.getItemAtPosition(0);
                                    break;
                            }
                        }
                    });
                    myDate = new MyDate();
                    editDate.init(myDate.getYear(selectedCostBean.getCostTime()), myDate.getMonth(selectedCostBean.getCostTime())-1, myDate.getDay(selectedCostBean.getCostTime()), new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            newCostBean.setCostDate(year + "-" + (monthOfYear) + "-" + dayOfMonth);
                        }
                    });
                    editTime.setHour(myDate.getHour(selectedCostBean.getCostTime()));
                    editTime.setMinute(myDate.getMinute(selectedCostBean.getCostTime()));

                    builder.setView(viewDialog);
                    builder.setTitle("编辑账单");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            newCostBean.setCostTitle(editTitle);
                            if(Type==1){
                                if(Double.parseDouble(editMoney.getText().toString())<=0){
                                    newCostBean.setCostMoney(Double.parseDouble(editMoney.getText().toString()));
                                }else{
                                    newCostBean.setCostMoney( -Double.parseDouble(editMoney.getText().toString()));
                                }
                            }else if (Type==2){
                                if (Double.parseDouble(editMoney.getText().toString())<=0){
                                    newCostBean.setCostMoney(-Double.parseDouble(editMoney.getText().toString()));
                                }else{
                                    newCostBean.setCostMoney(Double.parseDouble(editMoney.getText().toString()));
                                }
                            }
                            Calendar c = Calendar.getInstance();
                            c.set(editDate.getYear(),editDate.getMonth(),editDate.getDayOfMonth(),editTime.getHour(),editTime.getMinute());
                            newCostBean.setCostDate(editDate.getYear()+"-"+(editDate.getMonth()+1)+"-"+editDate.getDayOfMonth());
                            newCostBean.setCostMonth(editDate.getYear()+"-"+(editDate.getMonth()+1));
                            newCostBean.setCostType(Type);
                            newCostBean.setCostTime(c.getTimeInMillis());
                            newCostBean.setUuid(selectedCostBean.getUuid());
                            mDatabaseHelper.editData(selectedCostBean.getUuid(),newCostBean);
                            adapter.notifyDataSetChanged();
                            reload();
                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.create().show();

                }else if(which==2){
                    mDatabaseHelper.deleteAllData();
                    reload();
                }

            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }

    private void initSpinner() {
            if (Type==1){
                typesList.clear();
                typesList.add("衣服裤子");
                typesList.add("鞋帽包包");
                typesList.add("化妆饰品");
                typesList.add("早午晚餐");
                typesList.add("烟酒茶");
                typesList.add("水果零食");
                typesList.add("房租");
                typesList.add("物管水电");
                typesList.add("住宿费");
                typesList.add("出租车");
                typesList.add("加油费");
                typesList.add("停车费");
                typesList.add("飞机票");
                typesList.add("火车票");
                typesList.add("汽车票");
                typesList.add("轮船票");
                typesList.add("轮船票");
                typesList.add("轮船票");
                typesList.add("手机费");
                typesList.add("景点门票");
                typesList.add("运动健身");
                typesList.add("腐败聚会");
                typesList.add("书刊杂志");
                typesList.add("医药费");
            }else if (Type==2){
                typesList.clear();
                typesList.add("工资收入");
                typesList.add("礼金收入");
                typesList.add("中奖收入");
                typesList.add("意外来钱");
            }
            if(arrayAdapter!=null) {
                arrayAdapter.notifyDataSetChanged();
            }
    }

    private void initCostData() {
        Cursor cursor = mDatabaseHelper.getAllCostData();
        if (cursor!=null){
            while(cursor.moveToNext()){
                CostBean costBean = new CostBean();
                costBean.setCostTitle(cursor.getString(cursor.getColumnIndex("cost_title")));
                costBean.setCostDate(cursor.getString(cursor.getColumnIndex("cost_date")));
                costBean.setCostMoney(cursor.getDouble(cursor.getColumnIndex("cost_money")));
                costBean.setCostTime(cursor.getLong(cursor.getColumnIndex("cost_time")));
                costBean.setUuid(cursor.getString(cursor.getColumnIndex("cost_id")));
                costBean.setCostType(cursor.getInt(cursor.getColumnIndex("cost_type")));
                costBean.setCostMonth(cursor.getString(cursor.getColumnIndex("cost_month")));
                mCostBeanList.add(costBean);
            }
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;

        switch (item.getItemId()){
            case R.id.year:
                intent = new Intent(MainActivity.this,YearBill.class);
                startActivity(intent);
                break;
            case R.id.month:
                intent = new Intent(MainActivity.this,MonthBill.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showMyDialog(position);
        return false;
    }



}
