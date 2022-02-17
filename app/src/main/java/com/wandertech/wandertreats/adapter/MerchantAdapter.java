package com.wandertech.wandertreats.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wandertech.wandertreats.MyApp;
import com.wandertech.wandertreats.ProductActivity;
import com.wandertech.wandertreats.R;
import com.wandertech.wandertreats.StoreActivity;
import com.wandertech.wandertreats.general.GeneralFunctions;
import com.wandertech.wandertreats.general.StartActProcess;
import com.wandertech.wandertreats.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class MerchantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<HashMap<String, String>> resArrList;
    public Context mContext;
    public ItemOnClickListener itemOnClickListener;
    boolean isFooterEnabled = false;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;
    public static final int TYPE_MERCHANT = 3;
    public int storeAreaHeight = 0;
    public View footerView;
    public GeneralFunctions appFunctions;
    public String filePath = Constants.SERVER+"uploads/store/";

    public MerchantAdapter (Context context, ArrayList<HashMap<String, String>> mapArrayList, boolean isFooterEnabled) {
        this.mContext = context;
        this.resArrList = mapArrayList;
        this.isFooterEnabled = isFooterEnabled;
        this.appFunctions = MyApp.getInstance().getGeneralFun(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if( viewType == TYPE_HEADER) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_view, parent, false);
            HeaderHolder headerHolder = new HeaderHolder(view);
            return headerHolder;

        }else  if( viewType == TYPE_FOOTER){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false);
            FooterViewHolder footerViewHolder = new FooterViewHolder(view);
            return footerViewHolder;

        }else{

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_merchant, parent, false);
            ItemHolder itemHolder = new ItemHolder(view);
            return itemHolder;
        }




    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(holder instanceof ItemHolder) {

            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.itemName.setText(resArrList.get(position).get("vStoreName"));
            itemHolder.itemDesc.setText(resArrList.get(position).get("vUsername"));
            itemHolder.itemInfo.setText(resArrList.get(position).get("vStoreLocation"));

            Picasso.with(mContext)
                    .load(resArrList.get(position).get("vImages"))
                    .fit()
                    .placeholder(R.color.shimmer_placeholder)
                    .into(itemHolder.itemThumbnail);

            //appFunctions.showMessage(filePath+resArrList.get(position).get("vLogo"));
//
            itemHolder.itemCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //appFunctions.showMessage("click");

                    Bundle bn =  new Bundle();
                    bn.putString("data", resArrList.get(position).get("data"));
                    new StartActProcess(mContext).startActWithData(StoreActivity.class,bn);

//                    if(itemOnClickListener!=null){
//                        itemOnClickListener.setOnItemClick(position);
//                    }

                }
            });


        }else if(holder instanceof GridCategoryAdapter.FooterViewHolder) {

            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;

        }else{
           HeaderHolder headerholder = (HeaderHolder) holder;
            headerholder.itemHeaderArea.setVisibility(View.GONE);
            headerholder.itemNoteArea.setVisibility(View.VISIBLE);
            headerholder.itemNoteTxt.setText("Choose your desired store you want to buy.");
        }

    }




    public void removeLoading() {

        int position = resArrList.size() - 1;
        resArrList.remove(position);
        notifyItemRemoved(position);

    }

    @Override
    public int getItemViewType(int position) {

        if(resArrList.get(position).get("VIEWTYPE").equalsIgnoreCase("MERCHANT")){
            return TYPE_MERCHANT;
        } else if(resArrList.get(position).get("VIEWTYPE").equalsIgnoreCase("FOOTER")){
            return TYPE_FOOTER;
        }else{
            return TYPE_HEADER;
        }

    }


    private boolean isPositionFooter(int position) {
        return position == resArrList.size();
    }


    @Override
    public int getItemCount() {
        if (isFooterEnabled == true) {
            return resArrList.size() + 1;
        } else {
            return resArrList.size();
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        AppCompatTextView itemHeaderTxt, itemNoteTxt ;
        LinearLayout itemNoteArea, itemHeaderArea;

        public HeaderHolder(View storeView) {
            super(storeView);

            itemHeaderArea = storeView.findViewById(R.id.itemHeaderArea);
            itemNoteArea = storeView.findViewById(R.id.itemNoteArea);
            itemHeaderTxt = storeView.findViewById(R.id.itemHeaderTxt);
            itemNoteTxt = storeView.findViewById(R.id.itemNoteTxt);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout progressArea;

        public FooterViewHolder(View itemView) {
            super(itemView);

            progressArea = (LinearLayout) itemView;

        }
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        ImageView itemThumbnail;
        AppCompatTextView itemName;
        AppCompatTextView itemDesc;
        AppCompatTextView itemInfo;
        CardView itemCard;

        public ItemHolder(View storeView) {
            super(storeView);

            itemThumbnail = storeView.findViewById(R.id.itemThumbnail);
            itemName = storeView.findViewById(R.id.itemName);
            itemDesc = storeView.findViewById(R.id.itemDesc);
            itemInfo = storeView.findViewById(R.id.itemInfo);
            itemCard = storeView.findViewById(R.id.itemCard);

        }
    }

    public interface ItemOnClickListener {
        void setOnItemClick(int position);
    }

    public void setOnItemClick(ItemOnClickListener onItemClick) {
        this.itemOnClickListener = onItemClick;
    }

}