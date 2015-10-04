package bobzien.com.smartrecyclerviewadapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter.ViewHolder
import bobzien.com.smartrecyclerviewadapter.ViewHolder.LetterViewHolder
import bobzien.com.smartrecyclerviewadapter.ViewHolder.NumViewHolder
import bobzien.com.smartrecyclerviewadapter.ViewHolder.TypedViewHolder1
import bobzien.com.smartrecyclerviewadapter.ViewHolder.TypedViewHolder2
import butterknife.ButterKnife

import kotlinx.android.synthetic.fragment_main.*;

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {
//    protected lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.inject(this, view)

        val context = activity

        val integers = integers
        val typedObjects = typedObjects

        val viewHolders = getViewHolderArray(context)

        val adapter = SmartRecyclerViewAdapter(viewHolders)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)


        /** Let's set the initial list for the adapter  */
        adapter.setItems(integers)

        /** Let's add another ViewHolder to display Strings  */
        adapter.addViewHolder(LetterViewHolder(context, String::class.java, null) as ViewHolder<Any>)

        /** Let's add some Strings at position 3 since we also have the bobzien.com.smartrecyclerviewadapter.ViewHolder.bobzien.com.smartrecyclerviewadapter.ViewHolder.LetterViewHolder which can display Strings */
        adapter.addItems(3, "A", "B", "C")

        /** Let's add the typedObjects at position 10  */
        adapter.addItems(10, typedObjects)

        /** Let's remove the first item  */
        adapter.removeItem(0)

        adapter.selectedPosition = 3
    }

    private fun getViewHolderArray(context: Context): Array<ViewHolder<Any>> {
        /** Here we generate an array of ViewHolders which define what kind of items should be displayed  */
        return arrayOf(NumViewHolder(context, Integer::class.java , null) as ViewHolder<Any>,
                TypedViewHolder1(context, TypedObject::class.java, null) as ViewHolder<Any>,
                TypedViewHolder2(context, TypedObject::class.java, null) as ViewHolder<Any>)
    }

    private
            /** Here we get a list of integers which we want to add to our adapter. Those should be displayed by the NumViewHolder  */
    val integers: List<Int>
        get() {
            val integers = ArrayList<Int>()
            for (i in 0..19) {
                integers.add(i)
            }
            return integers
        }

    private
            /** Here we get a list of TypedObjects which we want to add to our adapter. Those should be displayed
             * by the TypedViewHolder1 and TypedViewhOlder2 depending on the type  */
    val typedObjects: List<TypedObject>
        get() {
            val typedObjects = ArrayList<TypedObject>()
            typedObjects.add(TypedObject(1))
            typedObjects.add(TypedObject(2))
            typedObjects.add(TypedObject(1))
            typedObjects.add(TypedObject(2))
            typedObjects.add(TypedObject(2))
            return typedObjects
        }
}
