package group3.piggybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class is the launcher activity that displays a list of transactions
 * in a recycler view in real time and the total dollar value of them all.
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 3/15/2020
 */
public class TransactionListActivity extends AppCompatActivity implements ItemClickListener {

    private DataManager manager;
    private RecyclerView recyclerView;
    public AdapterTransactions adapter;
    private TextView txtWallet;
    private ArrayList<Money> moneys = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    /**
     * Creates the activity and sets toolbar, floating action button, and title.
     * Also, checks if a user is signed in or not, otherwise open up a sign in page.
     *
     * @param savedInstanceState the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionListActivity.this,
                        AddTransactionActivity.class);
                intent.putExtra("ADD_OR_EDIT", 0);
                startActivity(intent);
            }
        });
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    manager = DataManager.getDataManager();
                    txtWallet = findViewById(R.id.lblAmountWallet);
                    recyclerView = findViewById(R.id.rvTransaction);
                    db.collection("accounts").document(auth.getCurrentUser().getUid())
                            .collection("moneys").get().
                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        moneys.clear();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            moneys.add(document.toObject(Money.class));
                                        }
                                        DecimalFormat df = new DecimalFormat("$0.00");
                                        manager.setMoneyList(moneys);
                                        txtWallet.setText(df.format(manager.getWallet()));
                                        adapter = new AdapterTransactions(TransactionListActivity.this,
                                                manager.getReverseMoneyList());
                                        adapter.setClickListener(TransactionListActivity.this);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(
                                                TransactionListActivity.this));
                                    }
                                }
                            });
                    recyclerView.requestFocus();
                } else {
                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false).build(), 1);
                }
            }
        };

    }

    /**
     * Checks for changes in the list of transactions whenever the
     * activity receives focus. Adds the authentication listener
     * if it didn't already exist.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (auth != null) {
            auth.addAuthStateListener(authStateListener);
        }
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String user = auth.getCurrentUser().getUid();
            manager = DataManager.getDataManager();
            txtWallet = findViewById(R.id.lblAmountWallet);
            recyclerView = findViewById(R.id.rvTransaction);
            ListenerRegistration reg = db.collection("accounts").document(user)
                    .collection("moneys").
                            addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        return;
                                    }
                                    moneys.clear();
                                    assert queryDocumentSnapshots != null;
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        moneys.add(document.toObject(Money.class));
                                    }
                                    DecimalFormat df = new DecimalFormat("$0.00");
                                    manager.setMoneyList(moneys);
                                    txtWallet.setText(df.format(manager.getWallet()));
                                    adapter = new AdapterTransactions(TransactionListActivity.this,
                                            manager.getReverseMoneyList());
                                    adapter.setClickListener(TransactionListActivity.this);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(
                                            TransactionListActivity.this));
                                }
                            });
            recyclerView.requestFocus();
        }
    }

    /**
     * Removes the authentication listener when the activity loses focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (auth != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    /**
     * Inflates the menu. This adds items to the action bar if it is present.
     *
     * @param menu the menu source
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * What happens when a menu item is clicked.
     * "Log Out" signs out the user.
     * "Statistics" opens up statistics activity.
     *
     * @param item items in the menu
     * @return true if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logOut) {
            auth.signOut();
            return true;
        } else if (id == R.id.action_stats) {
            Intent intent = new Intent(this, ChartActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * "Search" button on-click listener. Searches for money based on date
     *
     * @param view the view being clicked
     */
    public void onClickSearch(View view) {
        EditText txtDate = findViewById(R.id.txtDate);
        String date = String.valueOf(txtDate.getText());
        if (!date.equals("")) {
            ArrayList<Money> list = manager.getMoneyList(date);
            if (!list.isEmpty()) {
                adapter = new AdapterTransactions(this, list);
            }
        } else {
            adapter = new AdapterTransactions(this, manager.getReverseMoneyList());
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        hideKeyboard(this);
    }

    /**
     * The result when AuthUI finishes. Creates Toasts depending on
     * the result of the login attempt.
     *
     * @param requestCode matches the correct code block to run depending on
     *                    the requesting activity.
     * @param resultCode to determind if the user is signed in or not
     * @param data the data from intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * Hides the keyboard.
     * <p>
     * References:
     * https://stackoverflow.com/questions/1109022/close-hide-android-soft-keyboard
     *
     * @param activity the activity that needs to hide keyboard
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
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * When an item is clicked, view the details of that transaction in a new activity.
     *
     * @param view     the view being clicked
     * @param position the position of the selected item.
     */
    @Override
    public void onClickItem(View view, int position) {
        Intent intent = new Intent(this, ViewTransactionActivity.class);
        intent.putExtra("position", manager.getNumberOfMoney() - 1 - position);
        startActivity(intent);
    }
}
