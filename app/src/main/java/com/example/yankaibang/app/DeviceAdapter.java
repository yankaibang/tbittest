package com.example.yankaibang.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yankaibang.bluetooth.model.SearchResult;
import com.example.yankaibang.myapplication.R;

import java.util.List;

/**
 * Created by Salmon on 2017/5/2 0002.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

    private List<SearchResult> data;
    private Context context;
    private OnClickListener listener;

    public DeviceAdapter(List<SearchResult> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DeviceHolder holder = new DeviceHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_devices, parent,
                        false));
        return holder;
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {
        final SearchResult result = data.get(position);

        holder.textName.setText(result.getDevice().getName());
        holder.textMac.setText(result.getDevice().getAddress());
        holder.textRssi.setText(String.valueOf(result.getRssi()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnclickListener(OnClickListener onclickListener) {
        this.listener = onclickListener;
    }

    public interface OnClickListener {
        void onClick(SearchResult searchResult);
    }

    class DeviceHolder extends RecyclerView.ViewHolder {

        private TextView textRssi;
        private TextView textName;
        private TextView textMac;
        private View itemView;

        public DeviceHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            textRssi = (TextView) itemView.findViewById(R.id.text_rssi);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            textMac = (TextView) itemView.findViewById(R.id.text_mac);
        }
    }
}
