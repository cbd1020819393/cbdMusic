package adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by A on 2016/10/29.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int TYPE_NORMAL = 1;
    public Integer headerInt=1000;
    public Integer footerInt=2000;
    //存放header的viewType的list
    public ArrayList<Integer>headerIntList=new ArrayList<>();
    public ArrayList<Integer>footerIntList=new ArrayList<>();
    //存放数据的list
    public ArrayList<T> datas=new ArrayList<>();
    //存放header的map
    public HashMap<Integer,View> headerMap=new HashMap<>();
    //存放footer的map
    public  HashMap<Integer,View> footerMap=new HashMap<>();

    //添加数据
    public void addDatas(ArrayList<T> datas){
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    //添加header
    public void setmHeaderView(View mHeaderView) {
        headerInt=headerInt+1;
        headerMap.put(headerInt,mHeaderView);
        headerIntList.add(headerInt);
        notifyItemInserted(headerMap.size()-1);
    }

    //添加footer
    public void setmFooterView(View mFooterView) {
        footerInt=footerInt+1;
        footerMap.put(footerInt,mFooterView);
        footerIntList.add(footerInt);
        notifyItemInserted(datas.size()+headerMap.size()+footerMap.size()-1);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position,T data);
    }

    public interface OnItemLongClickListener<T> {
        void onItemClick(View view, int position,T data);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public void setmOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //返回header类型
                    if (headerIntList.size() != 0){
                        if(position<headerIntList.size())
                            return gridLayoutManager.getSpanCount();
                    }

                    //返回footer类型
                    if (footerIntList.size() != 0){
                        if(position>(headerIntList.size()+datas.size()-1))
                            return gridLayoutManager.getSpanCount();
                    }

                    //返回普通类型
                    return 1;
                }
            });
        }
    }

    //这个方法只能设置一个位置满屏
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            for(int i=0;i<headerIntList.size();i++){
                p.setFullSpan(holder.getLayoutPosition()==i);
            }

            for(int i=0;i<footerIntList.size();i++){
                p.setFullSpan(holder.getLayoutPosition()==(headerIntList.size()+datas.size()+i));
            }

        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //返回header类型
        if (headerMap.size() != 0 ){
            for(int i=0;i<headerIntList.size();i++){
                if(viewType==headerIntList.get(i)){
                    return new ViewHolder(headerMap.get(viewType));
                }
            }
        }

        //返回footer类型
        if (footerMap.size() != 0 ){
            for(int i=0;i<footerIntList.size();i++){
                if(viewType==footerIntList.get(i)){
                    return new ViewHolder(footerMap.get(viewType));
                }
            }
        }

        //返回普通类型
        return onCreate(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //header类型
        if (headerMap.size() != 0&&position<headerMap.size()) return;

        //footer类型
        if (footerMap.size() != 0&&position>headerMap.size()+datas.size()-1) return;
        //普通类型
        final T data = datas.get(position-headerMap.size());
        onBind(holder, position, data);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view,position,data);
                }
            });
        }

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemLongClickListener.onItemClick(view,position,data);
                    return true;
                }
            });
        }

    }

    public abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, final int viewType);
    public abstract void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, T data);

    @Override
    public int getItemViewType(int position) {
        //返回header类型
        if (headerIntList.size() != 0){
            if(position<headerIntList.size())
                return headerIntList.get(position);
        }

        //返回footer类型
        if (footerIntList.size() != 0){
            if(position>(headerIntList.size()+datas.size()-1))
                return footerIntList.get(position-(headerIntList.size()+datas.size()));
        }

        //返回普通类型
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return datas.size()+headerMap.size()+footerMap.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }


    }
}
