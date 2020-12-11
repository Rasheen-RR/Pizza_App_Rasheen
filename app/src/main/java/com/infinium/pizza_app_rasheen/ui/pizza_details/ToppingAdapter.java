package com.infinium.pizza_app_rasheen.ui.pizza_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Toppings;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ViewHolder> {


    private Context context;
    private List<Toppings> items;
    private List<Toppings> selectedItems = new ArrayList<>();
    private ToppingListner toppingListner;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewItemName;

        private final ImageView image;

        private final LinearLayout card;

        private final ConstraintLayout overlay;

        public ViewHolder(View view) {
            super(view);
            textViewItemName = (TextView) view.findViewById(R.id.topping_name);
            image = (ImageView) view.findViewById(R.id.topping_image);
            card = view.findViewById(R.id.topping_card);
            overlay = view.findViewById(R.id.select_overlay);
        }

        public TextView getTextViewItemName() {
            return textViewItemName;
        }

        public ImageView getItemImage() {
            return image;
        }

        public LinearLayout getCard() {
            return card;
        }

        public ConstraintLayout getOverlay() {
            return overlay;
        }


    }

    public ToppingAdapter(List<Toppings> dataSet, Context context, ToppingListner toppingListner) {
        items = dataSet;
        this.context = context;
        this.toppingListner = toppingListner;
    }

    @NonNull
    @Override
    public ToppingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.topping_list, parent, false);
        return new ToppingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ToppingAdapter.ViewHolder holder, final int position) {
        holder.getTextViewItemName().setText(items.get(position).getName());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();


        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(((PizzaDetailsActivity)context)));;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(items.get(position).getImage(),holder.getItemImage(),options);

        holder.getCard().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedItems.size() != 0){
                    if(selectedItems.contains(items.get(position))){
                        toppingListner.onDeSelect(items.get(position));
                        for (Iterator<Toppings> iterator = selectedItems.iterator(); iterator.hasNext();) {
                            Toppings obj= iterator.next();
                            if (obj.getName().equals(items.get(position).getName())) {
                                iterator.remove();
                            }
                        };
                        holder.getOverlay().setVisibility(View.INVISIBLE);
                    }else{
                        toppingListner.onSelect(items.get(position));
                        holder.getOverlay().setVisibility(View.VISIBLE);
                        selectedItems.add(items.get(position));
                    }
                }else{
                    toppingListner.onSelect(items.get(position));
                    holder.getOverlay().setVisibility(View.VISIBLE);
                    selectedItems.add(items.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
