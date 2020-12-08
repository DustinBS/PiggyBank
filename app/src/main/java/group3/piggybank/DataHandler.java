package group3.piggybank;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class handles most of the interactions (adding, deleting, replacing) with
 * the FireStore database.
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 3/2/2020
 */

public class DataHandler {

    private FirebaseAuth auth;
    private static String user;


    /**
     * Deletes an item from the Cloud data store that matches the given money hashcode.
     *
     * @param money the Money item to delete
     */
    void delete(Money money) {
        auth = FirebaseAuth.getInstance();
        DataHandler.user = auth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("accounts").document(user).collection("moneys")
                .document(String.valueOf(money.getId())).delete();
    }

    /**
     * Adds a new item to the Cloud data store. If another item exists with the same money hashcode,
     * it will be replaced with this one.
     *
     * @param money the Money item to add to the data store
     */
    void add(Money money) {
        auth = FirebaseAuth.getInstance();
        DataHandler.user = auth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("accounts").document(user).collection("moneys")
                .document(String.valueOf(money.getId())).set(money);
    }

    /**
     * Replace an item at the Cloud data store
     *
     * @param money the Money item to add to the data store
     */
    void replace(Money money) {
        auth = FirebaseAuth.getInstance();
        DataHandler.user = auth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("accounts").document(user).collection("moneys")
                .document(String.valueOf(money.getId())).set(money);
    }
}
