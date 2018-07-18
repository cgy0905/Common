package com.cgy.common.module.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgy.common.R;
import com.cgy.common.module.news.model.NewsEntity;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.SettingUtil;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/18  17:52
 */
public class NewsAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    private static final int TYPE_FOOTER = 0;
    private static final int ITEM_IMAGE = 1;
    private static final int ITEM_IMAGES = 2;

    private Context context;
    private List<NewsEntity> data;
    private int viewFooter;
    private View footerView;
    private int itemWidth;

    public NewsAdapter(List<NewsEntity> data, Context context) {
        this.context = context;
        this.data = data;
        itemWidth = (SettingUtil.getScreenWidth(context) - SettingUtil.dip2px(context, 32)) / 3;
    }

    public void replaceAll(List<NewsEntity> elements) {
        if (data.size() > 0) {
            data.clear();
        }
        data.addAll(elements);
        notifyDataSetChanged();
    }

    public void addAll(List<NewsEntity> elements) {
        data.addAll(elements);
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_IMAGE)
        return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void addFooterView(int footerView) {
        this.viewFooter = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setFooterVisible(int visible) {
        footerView.setVisibility(visible);
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }



    public interface OnItemClickListener {
        void onItemClick(int position, BaseViewHolder viewHolder);
    }
}
