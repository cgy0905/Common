package com.cgy.common.module.find.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cgy.common.R;
import com.cgy.common.module.find.model.JcodeEntity;
import com.llf.basemodel.commonactivity.WebViewActivity;
import com.llf.basemodel.commonwidget.CircleImageView;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by cgy
 * 2018/7/19  14:19
 */
public class DiscoveryAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int TYPE_FOOTER = 0;
    private static final int ITEM_NOIMAGE  = 1;
    private static final int ITEM_HASIMAGE = 2;

    private List<JcodeEntity> data;
    private Context context;
    private int viewFooter;
    private View footerView;
    private static final String HOST = "http://www.jcodecraeer.com";

    public DiscoveryAdapter(List<JcodeEntity> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void replaceAll(List<JcodeEntity> elements) {
        if (data.size() > 0) {
            data.clear();
        }
        data.addAll(elements);
        notifyDataSetChanged();
    }

    public void addAll(List<JcodeEntity> elements) {
        data.addAll(elements);
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_NOIMAGE) {
            return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_jcode_no_image, parent, false));
        } else if (viewType == ITEM_HASIMAGE) {
            return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_jcode_has_image, parent, false));
        } else {
            footerView = LayoutInflater.from(context).inflate(viewFooter, parent, false);
            return new BaseViewHolder(footerView);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        if (!(viewFooter != 0 && position == getItemCount() - 1)) {
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(position);
                    }
                });
            }
            int type = getItemViewType(position);
            final JcodeEntity item = data.get(position);
            if (type == ITEM_HASIMAGE) {
                ImageLoaderUtils.loadingImg(context, (ImageView) holder.getView(R.id.iv_cover), HOST + item.getImgUrl());
            }
            CircleImageView avatar = holder.getView(R.id.cir_avatar);
            holder.setText(R.id.tv_title, item.getTitle());
            holder.setText(R.id.tv_content, item.getContent());
            holder.setText(R.id.tv_author, item.getAuthor());
            holder.setText(R.id.tv_seeNum, item.getWatch());
            holder.setText(R.id.tv_commentNum, item.getComments());
            holder.setText(R.id.tv_loveNum, item.getLike());
            ImageLoaderUtils.loadingImg(context, avatar, HOST + item.getAuthorImg());
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.lanuch(context, HOST + "/member/index.php?uid" + item.getAuthor());
                }
            });
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
        int type = ITEM_HASIMAGE;
        if (viewFooter != 0 && position == getItemCount() - 1) {
            type = TYPE_FOOTER;
            return type;
        }
        if (TextUtils.isEmpty(data.get(position).getImgUrl())) {
            type = ITEM_NOIMAGE;
        } else {
            type = ITEM_HASIMAGE;
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
        void onItemClick(int position);
    }
}
