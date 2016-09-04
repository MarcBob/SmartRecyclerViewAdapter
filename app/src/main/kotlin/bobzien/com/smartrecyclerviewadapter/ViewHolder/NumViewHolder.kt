package bobzien.com.smartrecyclerviewadapter.ViewHolder

import android.content.Context
import android.view.View
import android.widget.TextView
import bobzien.com.smartrecyclerviewadapter.R
import com.marmor.smartrecyclerviewadapter.SmartRecyclerViewAdapter.ViewHolder

class NumViewHolder(context: Context, clazz: Class<Integer>, itemView: View?) : ViewHolder<Integer>(context, clazz, itemView) {
    protected lateinit var textView: TextView

    init {
        if (itemView != null) {
            textView = itemView.findViewById(R.id.text) as TextView
        }
    }

    override fun getInstance(itemView: View): ViewHolder<Any> {
        return NumViewHolder(context, getHandledClass() as Class<Integer>, itemView) as ViewHolder<Any>
    }

    override val layoutResourceId: Int
        get() = R.layout.list_item_num

    override fun bindViewHolder(item: Integer, selected: Boolean) {
        textView.text = item.toString()
    }
}
