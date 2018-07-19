package com.cgy.common.module.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cgy.common.R;
import com.cgy.common.module.news.model.NewsEntity;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.basemodel.utils.LogUtil;
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
        if (viewType == ITEM_IMAGE) {
            return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false));
        } else if (viewType == ITEM_IMAGES){
            return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_images, parent, false));
        } else {
            footerView = LayoutInflater.from(context).inflate(viewFooter, parent, false);
            return new BaseViewHolder(footerView);
        }
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        if (!(viewFooter != 0 && position == getItemCount() - 1)) {
            int type = getItemViewType(position);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(position, holder);
                    }
                });
            }

            NewsEntity newsEntity = data.get(position);
            try {
                if (type == ITEM_IMAGE) {
                    ImageView imageView = holder.getView(R.id.iv_news);
                    holder.setText(R.id.tv_title, newsEntity.getTitle());
                    holder.setText(R.id.tv_desc, newsEntity.getDigest());
                    ImageLoaderUtils.loadingImg(context, imageView, newsEntity.getImgsrc());
                } else {
                    holder.setText(R.id.tv_title, newsEntity.getTitle());
                    LinearLayout llImages = holder.getView(R.id.ll_images);
                    llImages.removeAllViews();
                    for (int i = 0; i < newsEntity.getImgextra().size(); i++) {
                        ImageView imageView = new ImageView(context);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemWidth);
                        if (i == 1) {
                            params.setMargins(SettingUtil.dip2px(context, 4), 0, SettingUtil.dip2px(context, 4), 0);
                        }
                        imageView.setLayoutParams(params);
                        ImageLoaderUtils.loadingImg(context, imageView, newsEntity.getImgextra().get(i).getImgsrc());
                        llImages.addView(imageView);
                    }
                }
            } catch (Exception e) {
                LogUtil.d("文字内容为空");
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        if (viewFooter != 0) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int type = ITEM_IMAGE;
        if (viewFooter != 0 && position == getItemCount() - 1) {
            type = TYPE_FOOTER;
            return type;
        }
        if (data.get(position).getImgextra() == null) {
            type = ITEM_IMAGE;
        } else {
            type = ITEM_IMAGES;
        }
        return type;
    }

    public void addFooterView(int footerView) {
        this.viewFooter = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setFooterVisible(int visible) {
        footerView.setVisibility(visible);
    }

    private OnItemClickListener onItemClickListener;

    //设置点击事件
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, BaseViewHolder viewHolder);
    }
}
