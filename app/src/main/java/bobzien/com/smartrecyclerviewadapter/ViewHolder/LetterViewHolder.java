package bobzien.com.smartrecyclerviewadapter.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import bobzien.com.smartrecyclerviewadapter.R;
import bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter.ViewHolder;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class LetterViewHolder extends ViewHolder<String>
{
    @InjectView(R.id.text)
    protected TextView textView;

    public LetterViewHolder(@NonNull Context context, @NonNull Class clazz, @Nullable View itemView)
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
        return new LetterViewHolder(getContext(), getClass(), itemView);
    }

    @Override
    public int getLayoutResourceId()
    {
        return R.layout.list_item_letter;
    }

    @Override
    public void bindViewHolder(@NonNull String item, boolean selected)
    {
        textView.setText(item);

        itemView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getAdapter().setSelectedPosition(getPosition());
            }
        });

        if(selected)
        {
            textView.setBackgroundColor(getContext().getResources().getColor(R.color.background_floating_material_dark));
        }
        else
        {
            textView.setBackgroundColor(getContext().getResources().getColor(R.color.background_floating_material_light));
        }
    }
}
