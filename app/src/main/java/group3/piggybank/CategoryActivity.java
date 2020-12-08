package group3.piggybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class creates the activity which displays the category recycler view.
 * Categories can also be added or selected here.
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 3/14/2020
 */
public class CategoryActivity extends AppCompatActivity {

    private DataManager manager;
    private RecyclerView recyclerView;
    private AdapterCategory adapter;
    private ArrayList<String> categoryNames;
    private ArrayList<Integer> categoryImageIds;

    /**
     * Creates the activity and sets toolbar and title.
     *
     * @param savedInstanceState the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Expense Categories");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        manager = DataManager.getDataManager();
        categoryNames = Money.getCategoryNamesList();
        categoryImageIds = Money.getImageIdList();
        recyclerView = findViewById(R.id.rvCategory);
        adapter = new AdapterCategory(this, manager.getCategories());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.requestFocus();
    }

    /**
     * Adds a category to the list if it doesn't exist yet (by name).
     * Refreshes the adapter.
     *
     * @param view the view being clicked
     */
    public void onClickAdd(View view) {
        EditText txtCategoryName = findViewById(R.id.txtCategoryName);
        String name = txtCategoryName.getText().toString();
        if (!(name.equals("") || Money.getCategoryNamesList().contains(name))) {
            addCategory(name);
            adapter = new AdapterCategory(this, manager.getCategories());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        hideKeyboard(this);
    }

    /**
     * Sends the selected categories name and image, if it exists
     * when "Done" is selected.
     *
     * @param view the view being clicked
     */
    public void onClickDone(View view) {
        Category selectedCategory = adapter.getSelected();
        Intent intent = new Intent();
        if (selectedCategory != null) {
            intent.putExtra("CATEGORY_NAME", selectedCategory.getName());
            intent.putExtra("CATEGORY_IMAGE_ID", selectedCategory.getImageResourceID());
        } else {
            intent.putExtra("CATEGORY_NAME", "");
            intent.putExtra("CATEGORY_IMAGE_ID", 1);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Hides the keyboard.
     *
     * @param activity the activity to do the method
     */
    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Adds a category and sorts the list of categories.
     *
     * @param name the new (added) category
     */
    private void addCategory(String name) {
        categoryNames.add(name);
        categoryImageIds.add(R.drawable.question_mark);
        int position = categoryNames.size() - 1;
        while (position > 0 && categoryNames.get(position).
                compareTo(categoryNames.get(position - 1)) < 0) {
            swap(categoryNames, position - 1, position);
            swap(categoryImageIds, position - 1, position);
            position--;
        }

    }

    /**
     * Swaps elements of a given arraylist
     *
     * @param arr the arraylist
     * @param n1  the element to swap from
     * @param n2  the element to swap with
     * @param <T> the data type inside the list
     */
    private <T> void swap(ArrayList<T> arr, int n1, int n2) {
        T temp = arr.get(n1);
        arr.set(n1, arr.get(n2));
        arr.set(n2, temp);
    }

}
