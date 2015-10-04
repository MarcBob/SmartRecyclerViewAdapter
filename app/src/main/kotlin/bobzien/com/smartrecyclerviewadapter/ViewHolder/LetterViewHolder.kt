package bobzien.com.smartrecyclerviewadapter.ViewHolder

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView

import bobzien.com.smartrecyclerviewadapter.R
import bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter.ViewHolder

class LetterViewHolder(context: Context, clazz: Class<*>, itemView: View?) : ViewHolder<String>(context, clazz, itemView) {
    protected lateinit var textView: TextView

    init {
        if (itemView != null) {
            textView = itemView.findViewById(R.id.text) as TextView
        }
    }

    override fun getInstance(itemView: View): ViewHolder<Any> {
        return LetterViewHolder(context, getHandledClass(), itemView) as ViewHolder<Any>
    }

    override val layoutResourceId: Int
        get() = R.layout.list_item_letter

    override fun bindViewHolder(item: String, selected: Boolean) {
        textView.text = item

        itemView.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                adapter?.selectedPosition = position
            }
        })

        if (selected) {
            textView.setBackgroundColor(context.resources.getColor(R.color.background_floating_material_dark))
        } else {
            textView.setBackgroundColor(context.resources.getColor(R.color.background_floating_material_light))
        }
    }
}
