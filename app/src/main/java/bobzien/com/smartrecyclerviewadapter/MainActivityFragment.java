package bobzien.com.smartrecyclerviewadapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter.ViewHolder;
import bobzien.com.smartrecyclerviewadapter.ViewHolder.LetterViewHolder;
import bobzien.com.smartrecyclerviewadapter.ViewHolder.NumViewHolder;
import bobzien.com.smartrecyclerviewadapter.ViewHolder.TypedViewHolder1;
import bobzien.com.smartrecyclerviewadapter.ViewHolder.TypedViewHolder2;
import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    @InjectView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.inject(this, view);

        Context context = getActivity();

        List<Integer> integers = getIntegers();
        List<TypedObject> typedObjects = getTypedObjects();

        ViewHolder[] viewHolders = getViewHolderArray(context);

        SmartRecyclerViewAdapter adapter = new SmartRecyclerViewAdapter(viewHolders);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        /** Let's set the initial list for the adapter **/
        adapter.setItems(integers);

        /** Let's add another ViewHolder to display Strings **/
        adapter.addViewHolder(new LetterViewHolder(context, String.class, null));

        /** Let's add some Strings at position 3 since we also have the LetterViewHolder which can display Strings**/
        adapter.addItems(3, "A", "B", "C");

        /** Let's add the typedObjects at position 10 **/
        adapter.addItems(10, typedObjects);

        /** Let's remove the first item **/
        adapter.removeItem(0);

        adapter.setSelectedPosition(3);
    }

    private ViewHolder[] getViewHolderArray(Context context)
    {
        /** Here we generate an array of ViewHolders which define what kind of items should be displayed **/
        return new ViewHolder[]{
                    new NumViewHolder(context, Integer.class, null),
                    new TypedViewHolder1(context, TypedObject.class, null),
                    new TypedViewHolder2(context, TypedObject.class, null)
            };
    }

    private List<Integer> getIntegers()
    {
        /** Here we get a list of integers which we want to add to our adapter. Those should be displayed by the NumViewHolder **/
        List<Integer> integers = new ArrayList<>();
        for(int i = 0; i < 20; i++)
        {
            integers.add(i);
        }
        return integers;
    }

    private List<TypedObject> getTypedObjects()
    {
        /** Here we get a list of TypedObjects which we want to add to our adapter. Those should be displayed
         * by the TypedViewHolder1 and TypedViewhOlder2 depending on the type **/
        List<TypedObject> typedObjects = new ArrayList<>();
        typedObjects.add(new TypedObject(1));
        typedObjects.add(new TypedObject(2));
        typedObjects.add(new TypedObject(1));
        typedObjects.add(new TypedObject(2));
        typedObjects.add(new TypedObject(2));
        return typedObjects;
    }
}
