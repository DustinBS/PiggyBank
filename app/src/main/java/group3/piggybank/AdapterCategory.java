package group3.piggybank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class makes the category recycler view adapter which displays
 * the image and name of each category. Selecting an item turns it red.
 * <p>
 * References:
 * https://medium.com/@droidbyme/android-recyclerview-with-single-and-multiple-selection-5d50c0c4c739
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 3/14/2020
 */
public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {
    private Context context;
    private ArrayList<Category> categories;
    private int checkedPosition = -1;
    private int selected_Item = RecyclerView.NO_POSITION;

    /**
     * Constructor. Requires the category list to be given to create the adapter.
     *
     * @param c          the context of the adapter
     * @param categories the list of categories
     */
    public AdapterCategory(Context c, ArrayList<Category> categories) {
        context = c;
        this.categories = categories;
    }

    /**
     * Displays the label and the image of each category. Changes the color of
     * viewholder when an item is selected.
     *
     * @param viewHolder the display
     * @param i          the position of an item
     */
    @Override
    public void onBindViewHolder(AdapterCategory.ViewHolder viewHolder, int i) {
        TextView lblCategory = viewHolder.lblCategory;
        ImageView iViewCategory = viewHolder.iViewCategory;
        if (!categories.isEmpty()) {
            lblCategory.setText(categories.get(i).getName());
            iViewCategory.setImageResource(categories.get(i).getImageResourceID());
        }
        viewHolder.itemView.setSelected(selected_Item == i);
        if (selected_Item == i) {
            viewHolder.itemView.findViewById(R.id.rowCategory).
                    setBackgroundResource(R.drawable.selected_state);
        } else {
            viewHolder.itemView.findViewById(R.id.rowCategory).
                    setBackgroundResource(R.drawable.textview_border);
        }
    }

    /**
     * Allows access to the size of the current list of categories.
     *
     * @return the number of categories in the list
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * Creates the category viewholder to hold the cards.
     *
     * @param parent is a special view that can contain other views
     * @param viewType the type of the view group
     * @return the viewholder
     */
    @Override
    public AdapterCategory.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_row_category, parent, false);
        return new AdapterCategory.ViewHolder(view, context);
    }

    /**
     * This inner class tells the viewholder to be based on a recyclerview's
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lblCategory;
        private ImageView iViewCategory;

        /**
         * Sets the properties of the viewholder's label, image, and an on click.
         *
         * @param itemView the view of the label
         * @param context context from the item
         */
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            ConstraintLayout row = itemView.findViewById(R.id.rvCategory);
            lblCategory = itemView.findViewById(R.id.lblCategory);
            iViewCategory = itemView.findViewById(R.id.iViewCategory);
            itemView.setOnClickListener(this);
        }

        /**
         * Select an item when clicked.
         *
         * @param view the item from the list
         */
        @Override
        public void onClick(View view) {
            if (checkedPosition != getAdapterPosition()) {
                notifyItemChanged(checkedPosition);
                checkedPosition = getAdapterPosition();
            }
            notifyItemChanged(selected_Item);
            selected_Item = getLayoutPosition();
            notifyItemChanged(selected_Item);
        }
    }

    /**
     * Accesses the selected category
     *
     * @return nothing if the position is -1, categories for not -1
     */
    public Category getSelected() {
        if (checkedPosition != -1) {
            return categories.get(checkedPosition);
        }
        return null;
    }

}

