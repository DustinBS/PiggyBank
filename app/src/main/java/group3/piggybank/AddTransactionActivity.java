package group3.piggybank;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

/**
 * This class creates an activity that allows the addition of new transactions
 * or the editing of existing ones.
 * <p>
 * References:
 * https://stackoverflow.com/questions/16031314/how-can-i-get-selected-date-in-calenderview-in-android
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 3/14/2020
 */
public class AddTransactionActivity extends AppCompatActivity {

    private TextView lblSelectedCategoryPrompt;
    private TextView lblSelectedCategory;
    private ImageView iViewSelectedCategory;
    private Button btnSelectCategory;
    private TextView txtAmount;
    private String selectedMoneyType = "none";
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;
    private DataManager dm;
    private int addEditCode = -1;
    private int position = -1;

    /**
     * Creates the activity and sets toolbar and title.
     * Determines if editing or adding from intent.
     *
     * @param savedInstanceState the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Transaction");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        dm = DataManager.getDataManager();
        Intent intent = getIntent();
        addEditCode = intent.getIntExtra("ADD_OR_EDIT", -1);
        if (addEditCode == 1) {
            position = intent.getIntExtra("position", -1);
        }
        setUp();
    }

    /**
     * Pre-populates the labels if editing, otherwise it is empty.
     */
    private void setUp() {
        txtAmount = findViewById(R.id.txtAmount);
        lblSelectedCategoryPrompt = findViewById(R.id.lblSelectedCategoryPrompt);
        lblSelectedCategory = findViewById(R.id.lblSelectedCategoryName);
        iViewSelectedCategory = findViewById(R.id.iViewSelectedCategory);
        btnSelectCategory = findViewById(R.id.btnSelectCategory);
        btnSelectCategory.setEnabled(false);
        lblSelectedCategoryPrompt.setVisibility(View.INVISIBLE);
        lblSelectedCategory.setVisibility(View.INVISIBLE);
        iViewSelectedCategory.setVisibility(View.INVISIBLE);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = findViewById(checkedId);
                if (rb.getText().equals("Expense")) {
                    btnSelectCategory.setEnabled(true);
                    lblSelectedCategoryPrompt.setVisibility(View.VISIBLE);
                    lblSelectedCategory.setVisibility(View.VISIBLE);
                    iViewSelectedCategory.setVisibility(View.VISIBLE);
                    selectedMoneyType = "Expense";
                } else {
                    btnSelectCategory.setEnabled(false);
                    lblSelectedCategoryPrompt.setVisibility(View.INVISIBLE);
                    lblSelectedCategory.setVisibility(View.INVISIBLE);
                    iViewSelectedCategory.setVisibility(View.INVISIBLE);
                    selectedMoneyType = "Income";
                }
            }
        });
        CalendarView cView = findViewById(R.id.cView);
        Calendar now = Calendar.getInstance();
        selectedDay = now.get(Calendar.DAY_OF_MONTH);
        selectedMonth = now.get(Calendar.MONTH) + 1;
        selectedYear = now.get(Calendar.YEAR);
        //https://stackoverflow.com/questions/16031314/how-can-i-get-selected-date-in-calenderview-in-android
        cView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                selectedDay = dayOfMonth;
                selectedMonth = month + 1;
                selectedYear = year;

            }
        });
        if (addEditCode == 1) {
            Money money = dm.getMoneyAt(position);
            txtAmount.setText(String.valueOf(money.getAmount()));
            if (money.getMoneyType().equals("Income")) {
                radioGroup.check(R.id.rbtnIncome);
            } else {
                radioGroup.check(R.id.rbtnExpense);
                btnSelectCategory.setEnabled(true);
                lblSelectedCategoryPrompt.setVisibility(View.VISIBLE);
                lblSelectedCategory.setVisibility(View.VISIBLE);
                lblSelectedCategory.setText(money.getExpenseCategory().getName());
                iViewSelectedCategory.setVisibility(View.VISIBLE);
                iViewSelectedCategory.setImageResource(money.getExpenseCategory()
                        .getImageResourceID());
                selectedMoneyType = "Expense";
            }

        }
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu the menu resource
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    /**
     * What happens when a menu item is clicked. Saves moneys to
     * the data manager if "save" is clicked.
     *
     * @param item items in the menu
     * @return true if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            if (!(txtAmount.getText().toString().equals("") || selectedMoneyType.equals("none"))) {
                String selectedCategoryName = "";
                if (selectedMoneyType.equals("Expense")) {
                    selectedCategoryName = lblSelectedCategory.getText().toString();
                }
                if (selectedMoneyType.equals("Income") ||
                        (!(selectedCategoryName.equals("")))) {
                    if (addEditCode == 0) {
                        dm.addMoney(new Money(Double.parseDouble(txtAmount.getText().toString()),
                                selectedDay,
                                selectedMonth, selectedYear, selectedMoneyType,
                                selectedCategoryName));
                    } else if (addEditCode == 1) {
                        dm.updateMoney(new Money(Double.parseDouble(txtAmount.getText().toString()),
                                selectedDay,
                                selectedMonth, selectedYear, selectedMoneyType,
                                selectedCategoryName), position);
                    }

                    finish();
                }
            }
            startActivity(new Intent(this,TransactionListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets what happens when the "select" button is clicked.
     * Opens the list of Categories in an activity.
     *
     * @param view the view being clicked
     */
    public void onClickSelect(View view) {
        Intent intent = new Intent(AddTransactionActivity.this,
                CategoryActivity.class);
        startActivityForResult(intent, 1);
    }

    /**
     * Does something when an activity for result is finished.
     * From category selection, sets the labels for category name and image.
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            assert data != null;
            String name = data.getStringExtra("CATEGORY_NAME");
            int imageId = data.getIntExtra("CATEGORY_IMAGE_ID", 1);
            assert name != null;
            if (!name.equals("")) {
                lblSelectedCategoryPrompt.setVisibility(View.VISIBLE);
                lblSelectedCategory.setVisibility(View.VISIBLE);
                lblSelectedCategory.setText(name);
                iViewSelectedCategory.setVisibility(View.VISIBLE);
                iViewSelectedCategory.setImageResource(imageId);
            }
        }
    }
}
