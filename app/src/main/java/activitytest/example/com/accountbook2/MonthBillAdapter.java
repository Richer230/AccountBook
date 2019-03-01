package activitytest.example.com.accountbook2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class MonthBillAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private List<MonthCostBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MyDate time = new MyDate();
    private List<Double> mIncomeList;
    private List<Double> mExpandList;


    public MonthBillAdapter(Context context, List<MonthCostBean> list,List<Double> mIncomeList,List<Double> mExpandList){
        mContext = context;
        mList = list;
        this.mIncomeList = mIncomeList;
        this.mExpandList = mExpandList;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MonthBillAdapter.ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new MonthBillAdapter.ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.list_month,null);
            viewHolder.mTvCostDate = (TextView)convertView.findViewById(R.id.month_date);
            viewHolder.mTvIncomeMoney = (TextView)convertView.findViewById(R.id.month_income_money);
            viewHolder.mTvExpandMoney = convertView.findViewById(R.id.month_expand_money);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (MonthBillAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.mTvCostDate.setText(mList.get(position).getCostDate());
        viewHolder.mTvIncomeMoney.setText(String.valueOf(mIncomeList.get(position)));
        if (mExpandList.get(position)<0){
            viewHolder.mTvExpandMoney.setText(String.valueOf(-mExpandList.get(position)));
        }else{
            viewHolder.mTvExpandMoney.setText(String.valueOf(mExpandList.get(position)));
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText =mList.get(position).getCostYear()+"-"+mList.get(position).getCostMonth();
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return (mList.get(position).getCostYear()*100)+mList.get(position).getCostMonth();
    }

    private static class ViewHolder{
        public TextView mTvCostDate;
        public TextView mTvIncomeMoney;
        public TextView mTvExpandMoney;
    }
    class HeaderViewHolder {
        TextView text;
    }
}
