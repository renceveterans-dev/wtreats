package com.wandertech.wandertreats.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wandertech.wandertreats.R;
import com.wandertech.wandertreats.utils.Constants;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;

public class ImagePreviewAdapter  extends PagerAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> imagelist;
    private OnItemClickListener onItemClickListener;
    public String filePath = Constants.SERVER+"uploads/products/";

    public ImagePreviewAdapter(Context context, ArrayList<String> imagelist) {
        this.context = context;
        this.imagelist = imagelist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_image_preview, container, false);
        ImageView banner_image = (ImageView) view.findViewById(R.id.item_image);

        Picasso.with(context)
                .load(imagelist.get(position))
                .placeholder(context.getResources().getDrawable(R.color.gray))
                .error(context.getResources().getDrawable(R.color.gray))
                .into(banner_image);

        banner_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onItemClickListener != null){
                    onItemClickListener.onItemClickList(position, imagelist.get(position));
                }
            }
        });

        container.addView(view);
        return view;
    }

    public interface OnItemClickListener {
        void onItemClickList(int position, String image);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.onItemClickListener = mItemClickListener;
    }


    @Override
    public int getCount() {
        return imagelist.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = (View) object;
        container.removeView(v);
    }

    @Override
    public boolean isViewFromObject(View v, Object object) {
        return v == object;
    }
}