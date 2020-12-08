package group3.piggybank;

import android.view.View;

/**
 * This interface provides an on-click method for items in a list.
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 2/17/2020
 */
public interface ItemClickListener {

    /**
     * An on-click listener. Returns the position of the selected item.
     *
     * @param view     the view being clicked
     * @param position the position of the selected item.
     */
    void onClickItem(View view, int position);

}
