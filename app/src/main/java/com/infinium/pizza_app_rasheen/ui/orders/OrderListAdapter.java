package com.infinium.pizza_app_rasheen.ui.orders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.infinium.pizza_app_rasheen.ui.order_details.OrderDetailsActivity;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;
import com.nostra13.universalimageloader.core.DisplayImageOptions;


public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {


    private Context context;
    private OrderPizza items;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView orderTotal, orderId;
        private final CardView cardView;

        public ViewHolder(View view) {
            super(view);
            orderTotal = (TextView) view.findViewById(R.id.order_total);
            orderId = (TextView) view.findViewById(R.id.order_id);
            cardView = view.findViewById(R.id.order_item);
        }

        public TextView getTextViewItemName() {
            return orderTotal;
        }

        public TextView getOrderId(){return orderId;}

        public CardView getCardView() {
            return cardView;
        }
    }

    public OrderListAdapter(OrderPizza dataSet, Context context) {
        items = dataSet;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false);
        return new OrderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderListAdapter.ViewHolder holder, final int position) {

        holder.getTextViewItemName().setText( String.format("%.2f CAD for %d item(s)", items.getCartPizzaList().get(position).getTotal(),items.getCartPizzaList().get(position).getPizzaList().size()));

        holder.getOrderId().setText(items.getCartPizzaList().get(position).getId());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("order", items.getCartPizzaList().get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.getCartPizzaList().size();
    }


}
