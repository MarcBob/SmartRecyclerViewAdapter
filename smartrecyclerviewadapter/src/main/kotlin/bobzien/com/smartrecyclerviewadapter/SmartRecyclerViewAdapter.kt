package bobzien.com.smartrecyclerviewadapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList
import java.util.HashMap

class SmartRecyclerViewAdapter @Throws(SmartRecyclerViewAdapter.DuplicateItemViewTypeException::class)
constructor(viewHolders: Array<SmartRecyclerViewAdapter.ViewHolder<Any>>) : Adapter<SmartRecyclerViewAdapter.ViewHolder<Any>>() {
    private val viewHolders = HashMap<Class<Any>, ViewHolder<Any>>()
    private val itemViewTypeToViewHolders = HashMap<Int, ViewHolder<Any>>()
    private var items: MutableList<Any> = ArrayList()
    private val typeViewHolders = ArrayList<TypeViewHolder<Any>>()
    var selectedPosition: Int = 0
        set(selectedPosition: Int) {
            val oldSelectedPosition = this.selectedPosition
            field = selectedPosition

            notifyItemChanged(oldSelectedPosition)
            notifyItemChanged(selectedPosition)
        }

    init {
        for (viewHolder in viewHolders) {
            addViewHolder(viewHolder)
        }
    }

    fun addViewHolders(vararg viewHolders: ViewHolder<Any>) {
        for (viewHolder in viewHolders) {
            addViewHolder(viewHolder)
        }
    }

    fun addViewHolder(viewHolder: ViewHolder<Any>) {
        if (itemViewTypeToViewHolders.containsKey(viewHolder.genericItemViewType)) {
            throw (DuplicateItemViewTypeException((itemViewTypeToViewHolders.get(viewHolder.genericItemViewType) as Any).javaClass.simpleName
                    + " has same ItemViewType as " + viewHolder.javaClass.simpleName
                    + ". Overwrite getGenericItemViewType() in one of the two classes and assign a new type. Consider using an android id."))
        }
        itemViewTypeToViewHolders.put(viewHolder.genericItemViewType, viewHolder)

        if (viewHolder is TypeViewHolder<Any>) {
            typeViewHolders.add(viewHolder)
        }
        this.viewHolders.put(viewHolder.getHandledClass() as Class<Any>, viewHolder)
    }

    fun setItems(items: List<Any>) {
        this.items = ArrayList(items)
        wrapItems()
        notifyDataSetChanged()
    }

    fun addItems(position: Int, vararg newItems: Any) {
        var index = position
        val insertionPosition = position
        for (item in newItems) {
            items.add(index++, wrapItem(item))
        }
        notifyItemRangeInserted(insertionPosition, newItems.size())
    }

    fun addItems(position: Int, newItems: List<Any>) {
        var index = position
        val insertionPosition = position
        for (item in newItems) {
            items.add(index++, wrapItem(item))
        }
        wrapItems()
        notifyItemRangeInserted(insertionPosition, newItems.size())
    }

    fun removeItem(position: Int) {
        items.remove(position)
        notifyItemRemoved(position)
    }

    fun removeItemRange(startPosition: Int, itemCount: Int) {
        for (i in 0..itemCount - 1) {
            items.remove(startPosition)
        }
        notifyItemRangeRemoved(startPosition, itemCount)
    }

    fun getItem(position: Int): Any? {
        if (position >= itemCount || position < 0) {
            return null
        }

        val item = items.get(position)
        if (item is Wrapper) {
            return item.item
        }
        return item
    }

    private fun wrapItems() {
        if (typeViewHolders.size() > 0) {
            var i = 0
            for (item in items) {
                items.set(i, wrapItem(item))
                i++
            }
        }
    }

    private fun wrapItem(item: Any): Any {
        for (typeViewHolder in typeViewHolders) {
            if (typeViewHolder.internalCanHandle(item)) {
                val wrappedObject = typeViewHolder.getWrappedObject(item)
                return wrappedObject
            }
        }
        return item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<Any> {
        val viewHolder = itemViewTypeToViewHolders.get(viewType)

        val instance = viewHolder!!.getInstance(parent)
        instance.adapter = this
        return instance
    }

    override fun onBindViewHolder(holder: ViewHolder<Any>?, position: Int) {
        var item = items.get(position)
        if (holder is TypeViewHolder<*>) {
            item = (item as Wrapper).item
        }

        (holder as ViewHolder<Any>).bindViewHolder(item, position == selectedPosition)
    }

    override fun getItemCount(): Int {
        return items.size()
    }

    override fun getItemViewType(position: Int): Int {
        val viewHolder = getViewHolder(position)

        return viewHolder.genericItemViewType
    }

    private fun getViewHolder(position: Int): ViewHolder<Any> {
        val item = items.get(position)

        val itemClass = item.javaClass

        var viewHolder: ViewHolder<Any>? = viewHolders.get(itemClass)

        if (viewHolder == null) {
            viewHolder = tryToFindSuperClass(itemClass)
            if (viewHolder != null) {
                return viewHolder
            }
            throw (MissingViewHolderException("There is no Viewholder for " + itemClass.simpleName + " or one of its superClasses."))
        }
        return viewHolder
    }

    private fun tryToFindSuperClass(itemClass: Class<*>): ViewHolder<Any>? {
        var viewHolder: ViewHolder<Any>?
        var superclass: Class<*>? = itemClass.superclass
        while (superclass != null) {
            viewHolder = viewHolders.get(superclass)
            if (viewHolder != null) {
                viewHolders.put(superclass as Class<Any>, viewHolder)
                return viewHolder
            }
            superclass = superclass.superclass
        }
        return null
    }

    abstract class ViewHolder<T>
    /**
     * This constructor is just used to create a factory that can produce the
     * ViewHolder that contains the actual itemView using the getInstance(View parent) method.
     * @param context
     * *
     * @param clazz
     */
    (val context: Context, handledClass: Class<*>, itemView: View?) : RecyclerView.ViewHolder(itemView ?: View(context)) {

        var adapter: SmartRecyclerViewAdapter? = null

        private var _handledClass: Class<*>
        open fun getHandledClass(): Class<*>{
            return _handledClass
        }

        init {
            _handledClass = handledClass
        }

        fun getInstance(parent: ViewGroup): ViewHolder<Any> {
            val view = LayoutInflater.from(context).inflate(layoutResourceId, parent, false)
            return getInstance(view)
        }

        /**
         * This has to create a new instance which was created using a constructor which calls super(itemView).
         * @param itemView
         * *
         * @return the new ViewHolder
         */
        abstract fun getInstance(itemView: View): ViewHolder<Any>

        /**
         * Gets the LayoutResourceId of the layout this ViewHolder uses
         * @return
         */
        abstract val layoutResourceId: Int

        /**
         * Override this if itemViewTypes are colliding. Consider using an android Id.
         * @return itemViewType
         */
        val genericItemViewType: Int
            get() = layoutResourceId

        /**
         * Bind the ViewHolder and return it.
         * @param item
         * *
         * @param selected
         * *
         * @return ViewHolder
         */
        abstract fun bindViewHolder(item: T, selected: Boolean)
    }

    abstract class TypeViewHolder<T>(context: Context, clazz: Class<Any>, itemView: View?) : ViewHolder<T>(context, clazz, itemView) {

        private lateinit var _handledClass: Class<*>

        init{
            _handledClass = getWrappedObject(null).javaClass
        }

        fun internalCanHandle(item: Any): Boolean {
            if (item.javaClass == super.getHandledClass()) {
                return canHandle(item as T)
            }
            return false
        }

        protected abstract fun canHandle(item: T): Boolean

        abstract fun getWrappedObject(item: T?): Wrapper

        override fun getHandledClass(): Class<*>{
            return _handledClass
        }
    }

    interface Wrapper {
        val item: Any
    }

    class DuplicateItemViewTypeException(detailMessage: String) : RuntimeException(detailMessage)

    class MissingViewHolderException(detailMessage: String) : RuntimeException(detailMessage)
}
