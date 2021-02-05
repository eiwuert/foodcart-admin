package de.habibhaidari.foodcart.ui.main;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.habibhaidari.foodcart.R;
import de.habibhaidari.foodcart.model.Order;
import de.habibhaidari.foodcart.model.Position;
import de.habibhaidari.foodcart.util.CurrencyUtils;

import static de.habibhaidari.foodcart.constant.FormatConstants.ORDER_NUMBER_FORMAT;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    public final ArrayList<Order> data;
    private final LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public OrderListAdapter(Context context, ArrayList<Order> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void update(ArrayList<Order> orders) {
        data.clear();
        data.addAll(orders);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = data.get(position);
        holder.numberTextView.setText(String.format(ORDER_NUMBER_FORMAT, order.getId()));
        holder.usernameTextView.setText(order.getName());
        holder.timeTextView.setText(DateUtils.getRelativeTimeSpanString(order.getCreatedAt().getTime()) + " · " + order.getState().getName());
        holder.totalTextView.setText(CurrencyUtils.getFormattedCurrency(order.getTotal()) + " €");
        holder.streetTextView.setText(order.getStreet());
        holder.articlesTextView.setText(String.format(inflater.getContext().getResources().getString(R.string.articles), order.getPositions().stream().map(Position::getQuantity).reduce(0, Integer::sum)));

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView numberTextView;
        final TextView usernameTextView;
        final TextView totalTextView;
        final TextView timeTextView;
        final TextView streetTextView;
        final TextView articlesTextView;
        final RelativeLayout orderItemLayout;


        ViewHolder(View itemView) {
            super(itemView);
            orderItemLayout = itemView.findViewById(R.id.order_item_layout);
            numberTextView = itemView.findViewById(R.id.number);
            usernameTextView = itemView.findViewById(R.id.username);
            totalTextView = itemView.findViewById(R.id.total);
            streetTextView = itemView.findViewById(R.id.street);
            timeTextView = itemView.findViewById(R.id.time);
            articlesTextView = itemView.findViewById(R.id.articles);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}
