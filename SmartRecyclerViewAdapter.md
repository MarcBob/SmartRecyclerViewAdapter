## bobzien.com.smartrecyclerviewadapter.bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter

The bobzien.com.smartrecyclerviewadapter.bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter makes it a lot easier to get the RecyclerView to display a list of different items. No need to create a adapter with view types and switch statements etc.

All you need to do is use the bobzien.com.smartrecyclerviewadapter.bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter and define a costum ViewHolder for each of your different list items.

How do you do this?

First you create the ViewHolder for your different items. There are two different possibilties. If the items have different classes then your ViewHolder has to extend the normal SmartRecyclerView.ViewHolder

The ViewHolder has two purposes:
	1. It acts as a factory that creates new ViewHolders to display the actual items.
	2. It contains the information about:
		* What kind of item (class of the item) it can display.
		* What layout to use.
		* How to bind the model to the view.

This means you have to implement a couple of methods.

# The constructor:
* The constructor needs to call the super constructor. If you don't have an ItemView which you don't if you create the ViewHolder outside of the adapter you can just pass in null. But you need to be able to pass in an ItemView.

# void bindViewHolder(@NonNull String item, boolean selected)
** This method is used to bind the data to the view. The seleced flag indicates if this item is selected so you can change the state of the view accordingly.

# int getLayoutResourceId()
* Here just return the layout recource that is used to display this item.

# ViewHolder getInstance(@NonNull View itemView)
* This method acts as a factory method to construct new ViewHolders for the adapter. Just return a new ViewHolder of this class which this time gets the itemView instead of null

If your different items are of the same class but should still be displayed differently based on some item type your ViewHolder should extend the SmartRecyclerView.TypeViewHolder.

Here you need to implement two more methods.

# boolean canHandle(@NonNull TypedObject item)
* This method determines if the item can and should be handled by this viewHolder. It should return true if it can and false if it can't.

# Wrapper getWrappedObject(@Nullable final TypedObject item)
* This method is very easy to use. Just let it return this:
 
'''
  	return new Wrapper() {
        @Override
        public Object getItem()
        {
            return item;
        }
    };
'''

It looks weird but it's needed to internally give that item a different class based on the type. Attention!!! This behaviour can not be inherited by multiple childclasses. If some other class extends this class it has to overwrite this method again an have this code in it!!!

Once you have created ViewHolders for all your item types, all there is left to do is to create the bobzien.com.smartrecyclerviewadapter.bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter which takes an array of instances of all the ViewHolders it should use. (When instantiating the viewHolders here it is fine to pass null as the itemView)

'''
ViewHolder[] viewHolders = {viewHolder1, viewHolder2, ...};
new bobzien.com.smartrecyclerviewadapter.bobzien.com.smartrecyclerviewadapter.SmartRecyclerViewAdapter(viewHolders);
'''

After that you can just use the different add methods on the adapter to add items to the list in different positions. They will be displayed without any casting on your side. You only have to make sure there is a ViewHolder defined for each class or type you want to display.