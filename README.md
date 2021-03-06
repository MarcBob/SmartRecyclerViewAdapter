# SmartRecyclerViewAdapter
If you are using the RecyclerView you might also get tired of writing adapters over and over again. The SmartRecyclerViewAdapter removes this pain. Just implement the ViewHolders and you don't have to deal with the adapter anymore.

All you need to do is to use the SmartRecyclerViewAdapter and define a custom ViewHolder for each kind of list items. This ViewHolder can be reused wherever you want to display this kind of item in a RecyclerView.

How do you do this?

#ViewHolder

First you create the ViewHolder for your different items. There are two different possibilities. If you have a list of items, which you want to display and the view you want to use to display the different items depends on the class of the items, your ViewHolder has to extend the normal SmartRecyclerView.ViewHolder

The ViewHolder has two purposes
    
1. It acts as a factory that creates new ViewHolders to display the actual items.

2. It contains the information about

 * What kind of item (class of the item) it can display.

 * What layout to use.

 * How to bind the model to the view.

This means you have to implement a couple of methods.

###The constructor:
The constructor needs to call the super constructor. If you don't have an ItemView, which you don't if you create the ViewHolder outside of the adapter, you can just pass in null. But you need to be able to pass in an ItemView in order for the adapter to work with the ViewHolder later on.

###bindViewHolder
            void bindViewHolder(@NonNull String item, boolean selected)

This method is used to bind the data to the view. The selected flag indicates if this item is selected so you can change the state of the view accordingly.

###getLayoutResourceId
             int getLayoutResourceId()

Here just return the layout resource that is used to display this item.

###getInstance
             ViewHolder getInstance(@NonNull View itemView)

This method acts as a factory method to construct new ViewHolders for the adapter. Just return a new ViewHolder of this class which this time gets the itemView instead of null

##Items with the same class but different types

If your different items are of the same class but should still be displayed differently based on some item type your ViewHolder should extend the SmartRecyclerView.TypeViewHolder.

Here you need to implement two more methods.

###canHandle
            boolean canHandle(@NonNull TypedObject item)

This method determines if the item can and should be handled by this viewHolder. It should return true if it can and false if it can't.

###getWrappedObject

            Wrapper getWrappedObject(@Nullable final TypedObject item)
This method is very easy to use. Just let it return this:

            return new Wrapper() 
            {
                @Override
                public Object getItem()
                    {
                        return item;
                    }
            };


It looks weird but it's needed to internally give that item a different class based on the type. Attention!!! This behaviour can not be inherited by multiple child classes. If some other class extends this class it has to overwrite this method again and it has to have this code inside it!!!

###getGenericItemViewType
This is usually not needed. If two of your ViewHolders use the same layout, one of them has to overwrite getGenericItemViewType to return a different ItemViewType. Consider using an android id for this. But keep in mind, usually this is not needed.

#SmartRecyclerViewAdapter
Once you have created ViewHolders for all your item types, all there is left to do is to create the SmartRecyclerViewAdapter which takes an array of instances of all the ViewHolders it should use. (When instantiating the viewHolders here it is fine to pass null as the itemView)


            ViewHolder[] viewHolders = {viewHolder1, viewHolder2, ...};
            new SmartRecyclerViewAdapter(viewHolders);

###addViewHolder and addViewHolders

You can add more ViewHolders at run time with those two methods.

###setItems and addItems

With setItems you set the list of items to display. This list can be any List of objects. As long as you defined a ViewHolder capable of displaying this class or one of its parent classes.

###removing items

with removeItemRange and removeItem you can remove items from the list

#Installation
Just import the module SmartRecyclerViewAdapter into your project for now.

#SampleApp
Check out the code in the directory app. Here you see some basic examples of how to use the SmartRecyclerView.
