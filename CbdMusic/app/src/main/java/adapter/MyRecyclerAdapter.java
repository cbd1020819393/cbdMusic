package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bean.MusicBean;
import cbd.com.cbdmusic.R;

/**
 * Created by A on 2016/10/29.
 */
public class MyRecyclerAdapter extends BaseRecyclerAdapter<MusicBean.ShowapiResBodyBean.PagebeanBean.SonglistBean> {


    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, MusicBean.ShowapiResBodyBean.PagebeanBean.SonglistBean data) {
        if(viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).textView.setText(data.getSingername());
        }
    }

    public class ViewHolder extends BaseRecyclerAdapter.ViewHolder{
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.recycle_item_tv);
        }
    }
}
