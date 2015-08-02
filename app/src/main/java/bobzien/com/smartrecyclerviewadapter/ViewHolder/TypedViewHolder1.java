package bobzien.com.smartrecyclerviewadapter.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import bobzien.com.smartrecyclerviewadapter.R;
import bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter.TypeViewHolder;
import bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter.ViewHolder;
import bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter.Wrapper;
import bobzien.com.smartrecyclerviewadapter.TypedObject;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class TypedViewHolder1 extends TypeViewHolder<TypedObject>
{
    @InjectView(R.id.text)
    protected TextView textView;

    public TypedViewHolder1(@NonNull Context context, @NonNull Class clazz, @Nullable View itemView)
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
        return new TypedViewHolder1(getContext(), getClass(), itemView);
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.list_item_typed1;
    }

    @NonNull
    @Override
    public void bindViewHolder(@NonNull TypedObject item, boolean selected)
    {
        textView.setText("Type " + item.type);
    }

    @Override
    protected boolean canHandle(@NonNull TypedObject item)
    {
        if(item.type == 1)
        {
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    protected Wrapper getWrappedObject(@Nullable final TypedObject item)
    {
        return new Wrapper() {
            @Override
            public Object getItem()
            {
                return item;
            }
        };
    }
}
