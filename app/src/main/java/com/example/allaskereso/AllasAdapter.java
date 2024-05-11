package com.example.allaskereso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AllasAdapter
        extends RecyclerView.Adapter<AllasAdapter.ViewHolder>
        implements Filterable {
    // Member variables.
    private ArrayList<AllasItem> mAllasData;
    private ArrayList<AllasItem> mAllasDataAll;
    private Context mContext;
    private int lastPosition = -1;

    AllasAdapter(Context context, ArrayList<AllasItem> itemsData) {
        this.mAllasData = itemsData;
        this.mAllasDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public AllasAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AllasAdapter.ViewHolder holder, int position) {
        // Get current sport.
        AllasItem currentItem = mAllasData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentItem);


        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mAllasData.size();
    }


    /**
     * RecycleView filter
     * **/
    @Override
    public Filter getFilter() {
        return shoppingFilter;
    }

    private Filter shoppingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<AllasItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = mAllasDataAll.size();
                results.values = mAllasDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(AllasItem item : mAllasDataAll) {
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mAllasData = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mItemImage;

        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mInfoText = itemView.findViewById(R.id.itemDesc);
            mItemImage = itemView.findViewById(R.id.itemImg);
            itemView.findViewById(R.id.apply).setOnClickListener(view -> {
                Toast.makeText(this.itemView.getContext(), "Sikeresen jelentkezett az állásra", Toast.LENGTH_LONG).show();
            });
        }

        void bindTo(AllasItem currentItem){
            mTitleText.setText(currentItem.getName());
            mInfoText.setText(currentItem.getLeiras());
            // Load the images into the ImageView using the Glide library.
            Glide.with(mContext).load(currentItem.getImgResource()).into(mItemImage);
        }
    }
}
