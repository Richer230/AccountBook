package activitytest.example.com.accountbook2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class YearBillAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private List<YearCostBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Double> mMoneyList;
    private List<Double> incomeList;
    private List<Double> expandList;

    public YearBillAdapter(Context context, List<YearCostBean> list,List<Double> incomeList,List<Double> expandList){
        mContext = context;
        mList = list;
        this.incomeList = incomeList;
        this.expandList = expandList;
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
        YearBillAdapter.ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new YearBillAdapter.ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.list_year,null);
            viewHolder.mTvCostDate = (TextView)convertView.findViewById(R.id.year_date);
            viewHolder.mTvIncomeMoney = (TextView)convertView.findViewById(R.id.year_income_money);
            viewHolder.mTvExpandMoney = convertView.findViewById(R.id.year_expand_money);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (YearBillAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.mTvCostDate.setText(mList.get(position).getCostMonth());
        viewHolder.mTvIncomeMoney.setText(String.valueOf(incomeList.get(position)));
        if (expandList.get(position)<0){
            viewHolder.mTvExpandMoney.setText(String.valueOf(-expandList.get(position)));
        }else{
            viewHolder.mTvExpandMoney.setText(String.valueOf(expandList.get(position)));
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
        String headerText =mList.get(position).getCostYear()+"";
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mList.get(position).getCostYear();
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
