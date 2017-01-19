package com.example.mehedi.mycost.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mehedi.mycost.models.CostData;
import com.example.mehedi.mycost.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehedi on 1/18/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    Context context;
    List<CostData> dataList = new ArrayList<>();
    LayoutInflater inflater;
    Listener listener;

    public ListAdapter(Context context, List<CostData> dataList) {

        this.context = context;
        this.dataList = dataList;
        this.listener = (Listener) context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = inflater.inflate(R.layout.datacost_list, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        holder.bind(dataList.get(position), listener);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        TextView show_date;
        TextView show_cost;

        public void bind(final CostData costData, final Listener listener) {
            show_date.setText(costData.date);
            show_cost.setText(costData.cost);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.dateDetails(costData);
                }
            });

        }

        public ListViewHolder(View itemView) {
            super(itemView);

            show_date = (TextView) itemView.findViewById(R.id.show_date);
            show_cost = (TextView) itemView.findViewById(R.id.show_cost);

        }
    }


}