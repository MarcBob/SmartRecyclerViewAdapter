package bobzien.com.smartrecyclerviewadapter.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import bobzien.com.smartrecyclerviewadapter.R;
import bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter.ViewHolder;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class NumViewHolder extends ViewHolder<Integer>
{
    @InjectView(R.id.text)
    protected TextView textView;

    public NumViewHolder(@NonNull Context context, @NonNull Class clazz, @Nullable View itemView)
    {
        super(context, clazz, itemView);

        if(itemView != null)
        {
            ButterKnife.inject(this, itemView);
        }
    }

    @Override
    public ViewHolder getInstance(@NonNull View itemView)
    {
        return new NumViewHolder(getContext(), getClass(), itemView);
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.list_item_num;
    }

    @NonNull
    @Override
    public void bindViewHolder(@NonNull Integer item, boolean selected)
    {
        textView.setText(item + "");
    }
}
