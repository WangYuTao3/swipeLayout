package me.swipelayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import me.library.swipe.SwipeHelper;

/**
 * Created by wangyt on 2016/11/23.
 * : description
 */

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.ItemViewHolder> implements View.OnClickListener {

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new ItemViewHolder(SwipeHelper.getSwipeLayout(context, R.layout.top, R.layout.bg), this);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.mmPosition.setText(String.valueOf(position));
        holder.mmMenu.setTag(R.id.position, position);
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMenu:
                int pos = (int) view.getTag(R.id.position);
                Toast.makeText(view.getContext(), String.valueOf(pos), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mmPosition;
        TextView mmMenu;

        public ItemViewHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            mmPosition = (TextView) itemView.findViewById(R.id.tvPos);
            mmMenu = (TextView) itemView.findViewById(R.id.tvMenu);
            mmMenu.setOnClickListener(listener);
        }
    }
}
