package bobzien.com.smartrecyclerviewadapter.ViewHolder

import android.content.Context
import android.view.View
import android.widget.TextView

import bobzien.com.smartrecyclerviewadapter.R
import bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter.ViewHolder
import butterknife.ButterKnife
import butterknife.InjectView

class NumViewHolder(context: Context, clazz: Class<Int>, itemView: View?) : ViewHolder<Int>(context, clazz, itemView) {
    protected lateinit var textView: TextView

    init {
        if (itemView != null) {
            textView = itemView.findViewById(R.id.text) as TextView
        }
    }

    override fun getInstance(itemView: View): ViewHolder<Any> {
        return NumViewHolder(context, getHandledClass() as Class<Int>, itemView) as ViewHolder<Any>
    }

    override val layoutResourceId: Int
        get() = R.layout.list_item_num

    override fun bindViewHolder(item: Int, selected: Boolean) {
        textView.text = item.toString()
    }
}
