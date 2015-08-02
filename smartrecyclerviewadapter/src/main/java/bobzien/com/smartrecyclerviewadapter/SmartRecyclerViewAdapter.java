package bobzien.com.smartrecyclerviewadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmartRecyclerViewAdapter extends Adapter
{
    private HashMap<Class, ViewHolder> viewHolders = new HashMap<>();
    private HashMap<Integer, ViewHolder> itemViewTypeToViewHolders = new HashMap<>();
    private List<Object> items = new ArrayList<>();
    private List<TypeViewHolder> typeViewHolders = new ArrayList<>();
    private int selectedPosition;

    public SmartRecyclerViewAdapter(ViewHolder[] viewHolders) throws DuplicateItemViewTypeException
    {
        for (ViewHolder viewHolder : viewHolders)
        {
            addViewHolder(viewHolder);
        }
    }

    public void addViewHolders(ViewHolder... viewHolders)
    {
        for(ViewHolder viewHolder : viewHolders)
        {
            addViewHolder(viewHolder);
        }
    }

    public void addViewHolder(ViewHolder viewHolder)
    {
        if (itemViewTypeToViewHolders.containsKey(viewHolder.getGenericItemViewType()))
        {
            throw (new DuplicateItemViewTypeException(itemViewTypeToViewHolders.get(viewHolder.getGenericItemViewType()).getClass().getSimpleName()
                    + " has same ItemViewType as " + viewHolder.getClass().getSimpleName()
                    + ". Overwrite getGenericItemViewType() in one of the two classes and assign a new type. Consider using an android id."));
        }
        itemViewTypeToViewHolders.put(viewHolder.getGenericItemViewType(), viewHolder);

        if(viewHolder instanceof TypeViewHolder)
        {
            typeViewHolders.add((TypeViewHolder) viewHolder);
        }
        this.viewHolders.put(viewHolder.getHandledClass(), viewHolder);
    }

    public void setItems(List items)
    {
        this.items = new ArrayList<>(items);
        wrapItems();
        notifyDataSetChanged();
    }

    public void addItems(int position, Object ... newItems)
    {
        int insertionPosition = position;
        for(Object item : newItems)
        {
            items.add(position++, wrapItem(item));
        }
        notifyItemRangeInserted(insertionPosition, newItems.length);
    }

    public void addItems(int position, List newItems)
    {
        int insertionPosition = position;
        for(Object item : newItems)
        {
            items.add(position++, wrapItem(item));
        }
        wrapItems();
        notifyItemRangeInserted(insertionPosition, newItems.size());
    }

    public void removeItem(int position)
    {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItemRange(int startPosition, int itemCount)
    {
        for(int i = 0; i < itemCount; i++)
        {
            items.remove(startPosition);
        }
        notifyItemRangeRemoved(startPosition, itemCount);
    }

    public Object getItem(int position)
    {
        if(position >= getItemCount() || position < 0)
        {
            return null;
        }

        Object item = items.get(position);
        if(item instanceof Wrapper)
        {
            return ((Wrapper)item).getItem();
        }
        return item;
    }

    private void wrapItems()
    {
        if(typeViewHolders.size() > 0)
        {
            int i = 0;
            for (Object item : items)
            {
                items.set(i, wrapItem(item));
                i++;
            }
        }
    }

    private Object wrapItem(Object item)
    {
        for(TypeViewHolder typeViewHolder : typeViewHolders)
        {
            if(typeViewHolder.internalCanHandle(item))
            {
                Wrapper wrappedObject = typeViewHolder.getWrappedObject(item);
                if(wrappedObject == null)
                {
                    throw new WrapperNullException("The wrapper returned by " + typeViewHolder.getClass().getSimpleName() + " was null."
                    + " Please implement the method getWrappedObject and let it return an inner class which implements Wrapper."
                    + " The method getItem() should return the item that was passed into the getWrappedObject() method as a parameter.");
                }
                return wrappedObject;
            }
        }
        return item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ViewHolder viewHolder = itemViewTypeToViewHolders.get(viewType);

        ViewHolder instance = viewHolder.getInstance(parent);
        instance.setAdapter(this);
        return instance;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Object item = items.get(position);
        if(holder instanceof TypeViewHolder)
        {
            item = ((Wrapper)item).getItem();
        }

        ((ViewHolder)holder).bindViewHolder(item, position == selectedPosition);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        ViewHolder viewHolder = getViewHolder(position);

        return viewHolder.getGenericItemViewType();
    }

    private ViewHolder getViewHolder(int position)
    {
        Object item = items.get(position);

        Class<?> itemClass = item.getClass();

        ViewHolder viewHolder = viewHolders.get(itemClass);

        if(viewHolder == null)
        {
            viewHolder = tryToFindSuperClass(itemClass);
            if (viewHolder != null)
            {
                return viewHolder;
            }
            throw(new MissingViewHolderException("There is no Viewholder for " + itemClass.getSimpleName()
                    + " or one of its superClasses."));
        }
        return viewHolder;
    }

    private ViewHolder tryToFindSuperClass(Class<?> itemClass)
    {
        ViewHolder viewHolder;
        Class<?> superclass = itemClass.getSuperclass();
        while(superclass != null)
        {
            viewHolder = viewHolders.get(superclass);
            if(viewHolder != null)
            {
                viewHolders.put(superclass, viewHolder);
                return viewHolder;
            }
            superclass = superclass.getSuperclass();
        }
        return null;
    }

    public int getSelectedPosition()
    {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition)
    {
        int oldSelectedPosition = this.selectedPosition;
        this.selectedPosition = selectedPosition;
        this.selectedPosition = selectedPosition;

        notifyItemChanged(oldSelectedPosition);
        notifyItemChanged(selectedPosition);
    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder
    {
        private Context context;
        private Class clazz;
        private SmartRecyclerViewAdapter adapter;

        /**
         * This constructor is just used to create a factory that can produce the
         * ViewHolder that contains the actual itemView using the getInstance(View parent) method.
         * @param context
         * @param clazz
         */
        public ViewHolder(@NonNull Context context, @NonNull Class clazz, @Nullable View itemView)
        {
            super(itemView == null ? new View(context) : itemView);
            this.context = context;
            this.clazz = clazz;
        }

        private ViewHolder getInstance(@NonNull ViewGroup parent)
        {
            View view = LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), parent, false);
            return getInstance(view);
        }

        /**
         * This has to create a new instance which was created using a constructor which calls super(itemView).
         * @param itemView
         * @return the new ViewHolder
         */
        public abstract ViewHolder getInstance(@NonNull View itemView);

        /**
         * Gets the LayoutResourceId of the layout this ViewHolder uses
         * @return
         */
        public abstract int getLayoutResourceId();

        /**
         * Override this if itemViewTypes are colliding. Consider using an android Id.
         * @return itemViewType
         */
        public int getGenericItemViewType()
        {
            return getLayoutResourceId();
        }

        /**
         * Bind the ViewHolder and return it.
         * @param item
         * @param selected
         * @return ViewHolder
         */
        @NonNull
        public abstract void bindViewHolder(@NonNull T item, boolean selected);

        protected Class getHandledClass()
        {
            return clazz;
        }

        public Context getContext()
        {
            return context;
        }

        public SmartRecyclerViewAdapter getAdapter()
        {
            return adapter;
        }
        public void setAdapter(SmartRecyclerViewAdapter adapter)
        {
            this.adapter = adapter;
        }
    }

    public static abstract class TypeViewHolder<T> extends ViewHolder<T>
    {
        public TypeViewHolder(@NonNull Context context, @NonNull Class clazz, @Nullable View itemView)
        {
            super(context, clazz, itemView);
        }

        private boolean internalCanHandle(@NonNull Object item)
        {
            if(item.getClass().equals(super.getHandledClass()))
            {
                return canHandle((T)item);
            }
            return false;
        }

        protected abstract boolean canHandle(@NonNull T item);

        @NonNull
        protected abstract Wrapper getWrappedObject(@Nullable final T item);

        @Override
        protected final Class getHandledClass()
        {
            return getWrappedObject(null).getClass();
        }
    }

    public interface Wrapper
    {
        Object getItem();
    }

    public static class DuplicateItemViewTypeException extends RuntimeException
    {
        public DuplicateItemViewTypeException(String detailMessage)
        {
            super(detailMessage);
        }
    }

    public static class MissingViewHolderException extends RuntimeException
    {
        public MissingViewHolderException(String detailMessage)
        {
            super(detailMessage);
        }
    }

    public static class WrapperNullException extends RuntimeException
    {
        public WrapperNullException(String detailMessage)
        {
            super(detailMessage);
        }
    }
}
