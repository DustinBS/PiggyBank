package group3.piggybank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.DecimalFormat;

/**
 * This class is creates an activity that displays the details of a transaction.
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 3/15/2020
 */
public class ViewTransactionActivity extends AppCompatActivity {

    private DataManager manager;
    private int position;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();


    /**
     * Creates the activity and sets toolbar, floating action button, and title.
     *
     * @param savedInstanceState the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("View Transaction");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        manager = DataManager.getDataManager();
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 1);
    }

    /**
     * Populates the labels with a given date, type, amount, category name, and
     * and category image id.
     *
     * @param date         the date of the transaction
     * @param moneyType    the type of money
     * @param amount       the dollar value of the money
     * @param categoryName the category of the money
     * @param imageId      the image id of the category
     */
    private void displayTransaction(String date, String moneyType, double amount,
                                    String categoryName, int imageId) {
        TextView lblDate = findViewById(R.id.lblDate);
        lblDate.setText(date);
        TextView lblType = findViewById(R.id.lblType);
        lblType.setText(moneyType);
        TextView lblAmount = findViewById(R.id.lblAmount);
        DecimalFormat df = new DecimalFormat("$0.00");
        lblAmount.setText(df.format(amount));
        if (moneyType.equals("Income")) {
            findViewById(R.id.lblCategoriesPrompt).setVisibility(View.GONE);
            findViewById(R.id.iViewCategory).setVisibility(View.GONE);
            findViewById(R.id.lblCategoryName).setVisibility(View.GONE);
        } else {
            findViewById(R.id.lblCategoriesPrompt).setVisibility(View.VISIBLE);
            TextView lblCategoryName = findViewById(R.id.lblCategoryName);
            lblCategoryName.setVisibility(View.VISIBLE);
            lblCategoryName.setText(categoryName);
            ImageView ivCategory = findViewById(R.id.iViewCategory);
            ivCategory.setVisibility(View.VISIBLE);
            ivCategory.setImageResource(imageId);
        }
    }

    /**
     * Once the activity receives focus, updates the labels if there
     * Firebase data for the chosen money receives changes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        String user = auth.getCurrentUser().getUid();
        Money money = manager.getMoneyAt(position);
        DocumentReference ref = db.collection("accounts").document(user).
                collection("moneys").document(String.valueOf(money.getId()));
        ListenerRegistration reg = ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Money updated = documentSnapshot.toObject(Money.class);
                    assert updated != null;
                    double amount = updated.getAmount();
                    String date = updated.getDate();
                    String moneyType = updated.getMoneyType();
                    String categoryName = "";
                    int imageId = 1;

                    if (moneyType.equals("Expense")) {
                        categoryName = updated.getExpenseCategory().getName();
                        imageId = updated.getExpenseCategory().getImageResourceID();
                    }

                    displayTransaction(date, moneyType, amount, categoryName, imageId);
                }
            }
        });
    }

    /**
     * Inflates the menu. This adds items to the action bar if it is present.
     *
     * @param menu the menu source
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    /**
     * What happens when a menu item is clicked.
     * "Delete" deletes the money objbect.
     * "Edit" opens up AddTransaction activity to allow editing of the money object.
     *
     * @param item items in the menu
     * @return true if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete " + item.toString())
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //perform action
                                manager.deleteMoneyAt(position);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)  //Do nothing on no
                        .show();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, AddTransactionActivity.class);
                intent.putExtra("ADD_OR_EDIT", 1);
                intent.putExtra("position", position);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
