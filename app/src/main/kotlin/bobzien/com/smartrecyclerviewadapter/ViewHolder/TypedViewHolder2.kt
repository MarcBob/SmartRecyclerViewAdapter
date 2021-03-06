package bobzien.com.smartrecyclerviewadapter.ViewHolder

import android.content.Context
import android.view.View
import android.widget.TextView
import bobzien.com.smartrecyclerviewadapter.R
import bobzien.com.smartrecyclerviewadapter.TypedObject
import bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter.*

class TypedViewHolder2(context: Context, clazz: Class<TypedObject>, itemView: View?) : TypeViewHolder<TypedObject>(context, clazz as Class<Any>, itemView) {
    protected lateinit  var textView: TextView

    init {
        if (itemView != null) {
            textView = itemView.findViewById(R.id.text) as TextView
        }
    }

    override fun getInstance(itemView: View): ViewHolder<Any> {
        return TypedViewHolder2(context, getHandledClass() as Class<TypedObject>, itemView) as ViewHolder<Any>
    }

    override val layoutResourceId: Int
        get() = R.layout.list_item_typed2

    override fun bindViewHolder(item: TypedObject, selected: Boolean) {
        textView.text = "Type " + item.type
    }

    override fun canHandle(item: TypedObject): Boolean {
        if (item.type == 2) {
            return true
        }
        return false
    }

    override fun getWrappedObject(item: TypedObject?): Wrapper {
        return object : Wrapper {
            override val item: Any
                get() = item as Any
        }
    }
}
