package group3.piggybank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class makes the transaction recycler view adapter which displays
 * the date, amount, type, and image (for expense) of each transaction.
 * Selecting an item opens ViewTransactionActivity.
 * <p>
 * References:
 * https://www.codexpedia.com/android/defining-item-click-listener-for-recyclerview-in-android/
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 3/14/2020
 */
public class AdapterTransactions extends RecyclerView.Adapter<AdapterTransactions.ViewHolder> {

    private ArrayList<String> dates;
    private ArrayList<Double> amounts;
    private ArrayList<String> types;
    private ArrayList<Integer> imageIds;
    private Context context;
    private ItemClickListener clickListener;

    /**
     * Constructor. Requires the transaction (money) list to be initialized.
     *
     * @param c         the context of the adapter
     * @param moneyList the list of money
     */
    public AdapterTransactions(Context c, ArrayList<Money> moneyList) {
        context = c;
        dates = new ArrayList<>();
        amounts = new ArrayList<>();
        types = new ArrayList<>();
        imageIds = new ArrayList<>();
        for (Money m : moneyList) {
            dates.add(m.getDate());
            amounts.add(m.getAmount());
            types.add(m.getMoneyType());
            if (m.getExpenseCategory() != null) {
                imageIds.add(m.getExpenseCategory().getImageResourceID());
            } else {
                imageIds.add(-1);
            }
        }
    }

    /**
     * Displays the date, type, amount and image (for expense) of each category.
     *
     * @param viewHolder the display
     * @param i          the position of an item
     */
    @Override
    public void onBindViewHolder(AdapterTransactions.ViewHolder viewHolder, int i) {
        TextView lblDate = viewHolder.lblDate;
        TextView lblType = viewHolder.lblType;
        TextView lblAmount = viewHolder.lblAmount;
        ImageView iViewCategory = viewHolder.iViewCategory;
        if (!dates.isEmpty()) {
            lblDate.setText(dates.get(i));
            String type = types.get(i) + ": ";
            lblType.setText(type);
            DecimalFormat df = new DecimalFormat("$0.00");
            lblAmount.setText(df.format(amounts.get(i)));
            if (imageIds.get(i) != -1) {
                iViewCategory.setVisibility(View.VISIBLE);
                iViewCategory.setImageResource(imageIds.get(i));
            } else {
                iViewCategory.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Accesses to the size of the current list of dates
     *
     * @return the amount of dates in the current list
     */
    @Override
    public int getItemCount() {
        return dates.size();
    }

    /**
     * Changes what happens when the view is clicked.
     *
     * @param itemClickListener the new on-click listener.
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    /**
     * Creates the viewholder to hold the cards.
     *
     * @param parent is a special view that can contain other views
     * @param viewType the type of the view group
     * @return the viewholder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_row_transaction, parent, false);
        return new ViewHolder(view, context);
    }

    /**
     * This inner class tells the viewholder to be based on a recyclerview's
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lblDate;
        private TextView lblType;
        private TextView lblAmount;
        private ImageView iViewCategory;

        /**
         * Sets the properties of the viewholder's date, type,
         * amount, image, and an on click.
         *
         * @param itemView the view holding the transaction
         * @param context  the context of the view
         */
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            ConstraintLayout row = itemView.findViewById(R.id.rvTransaction);
            lblDate = itemView.findViewById(R.id.lblDate);
            lblType = itemView.findViewById(R.id.lblMoneyType);
            lblAmount = itemView.findViewById(R.id.lblAmountTransaction);
            iViewCategory = itemView.findViewById(R.id.iViewCategoryImage);
            itemView.setOnClickListener(this);
        }

        /**
         * Sets a default on click listener. Returns selected item position.
         *
         * @param view view
         */
        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClickItem(view, getAdapterPosition());
            }
        }
    }
}
