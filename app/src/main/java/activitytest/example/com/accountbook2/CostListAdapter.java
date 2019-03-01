package activitytest.example.com.accountbook2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class CostListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<CostBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MyDate date = new MyDate();

    public CostListAdapter(Context context, List<CostBean> list){
        mContext = context;
        mList = list;
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
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.list_item,null);
            viewHolder.mTvCostTitle = (TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.mTpCostTime = (TextView) convertView.findViewById(R.id.tv_time) ;
            viewHolder.mTvCostMoney = (TextView)convertView.findViewById(R.id.tv_cost);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CostBean bean = mList.get(position);
        viewHolder.mTvCostTitle.setText(bean.getCostTitle());
        viewHolder.mTvCostMoney.setText(String.valueOf(bean.getCostMoney()));
        viewHolder.mTpCostTime.setText(bean.getTime());
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
        String headerText =mList.get(position).getCostDate();
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return (date.getYear(mList.get(position).getCostTime())*10000)+(date.getMonth(mList.get(position).getCostTime())*100)+(date.getDay(mList.get(position).getCostTime()));
    }


    private static class ViewHolder{
        public TextView mTvCostTitle;
        public TextView mTvCostMoney;
        public TextView mTpCostTime;
    }

    class HeaderViewHolder {
        TextView text;
    }
}
