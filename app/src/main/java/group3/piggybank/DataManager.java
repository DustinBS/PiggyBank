package group3.piggybank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This singleton class maintains a local copy of data needed for this app and
 * communicates with the DataHandler in order to update the cloud data store.
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 3/2/2020
 */
public class DataManager {

    private DataHandler dh;
    private ArrayList<Money> moneyList;
    private static DataManager manager;
    private ArrayList<Double> eachCategoryAmount;
    private ArrayList<String> categoryNames;

    /**
     * Private constructor to prevent multiple instances of manager.
     */
    private DataManager() {
        dh = new DataHandler();
        moneyList = new ArrayList<>();
    }

    /**
     * Method to instantiate a data manager.
     *
     * @return the data manager object
     */
    public static DataManager getDataManager() {
        if (manager == null) {
            manager = new DataManager();
        }
        return manager;
    }

    /**
     * Accesses the list of dates
     *
     * @return a list of dates
     */
    public ArrayList<String> getDates() {
        ArrayList<String> dates = new ArrayList<>();
        for (Money m : moneyList) {
            dates.add(m.getDate());
        }
        return dates;
    }

    /**
     * Accesses the list of amounts of money.
     *
     * @return a list of amounts of money
     */
    public ArrayList<Double> getAmounts() {
        ArrayList<Double> dates = new ArrayList<>();
        for (Money m : moneyList) {
            dates.add(m.getAmount());
        }
        return dates;
    }

    /**
     * Accesses the list of money types.
     *
     * @return a list of money types
     */
    public ArrayList<String> getMoneyTypes() {
        ArrayList<String> types = new ArrayList<>();
        for (Money m : moneyList) {
            types.add(m.getMoneyType());
        }
        return types;
    }

    /**
     * Accesses the total amount of money currently in the list
     *
     * @return the total amount of money
     */
    public double getWallet() {
        double wallet = 0;
        for (int i = 0; i < moneyList.size(); i++) {
            if (moneyList.get(i).getMoneyType().equals("Income")) {
                wallet += moneyList.get(i).getAmount();
            } else {
                wallet -= moneyList.get(i).getAmount();
            }
        }
        return wallet;
    }

    /**
     * Accesses the list of moneys
     *
     * @return the list of moneys
     */
    public ArrayList<Money> getMoneyList() {
        return moneyList;
    }

    /**
     * Accesses the list of moneys but in reverse
     *
     * @return the list of moneys in reverse
     */
    public ArrayList<Money> getReverseMoneyList() {
        ArrayList<Money> list = new ArrayList<>();
        for (int i = moneyList.size() - 1; i >= 0; i--) {
            list.add(moneyList.get(i));
        }
        return list;
    }

    /**
     * Gives the total number of money objects in the list.
     *
     * @return the total number of money objects in the list
     */
    public int getNumberOfMoney() {
        return moneyList.size();
    }

    /**
     * Mutates the current list of moneys into a new given list of moneys
     *
     * @param moneyList the new list of moneys
     */
    public void setMoneyList(ArrayList<Money> moneyList) {
        this.moneyList = moneyList;
    }

    /**
     * Gives a list of moneys that have a specified date property.
     *
     * @param date the specified date property (ex. March 19 2020)
     * @return the list of moneys with the specified date property.
     */
    public ArrayList<Money> getMoneyList(String date) {
        ArrayList<Money> list = new ArrayList<>();
        String[] dateArr = date.split(" ");
        if (dateArr.length == 2) {
            dateArr[1] = dateArr[1].substring(0, 1);
        }
        if (dateArr.length == 3) {
            for (int i = 0; i < moneyList.size(); i++) {
                if (moneyList.get(i).getDate().equals(date)) {
                    list.add(moneyList.get(i));
                }
            }
        } else if (dateArr.length == 2) {
            for (int i = 0; i < moneyList.size(); i++) {
                if (moneyList.get(i).getMonth().equals(dateArr[0]) &&
                        moneyList.get(i).getDay() == Integer.valueOf(dateArr[1])) {
                    list.add(moneyList.get(i));
                }
            }
        } else if (dateArr.length == 1) {
            for (int i = 0; i < moneyList.size(); i++) {
                if (moneyList.get(i).getMonth().equals(dateArr[0])) {
                    list.add(moneyList.get(i));
                }
            }
        }
        return list;
    }

    /**
     * Accesses the list of categories.
     *
     * @return the list of categories
     */
    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<Integer> imageIds = Money.getImageIdList();
        ArrayList<String> categoryNames = Money.getCategoryNamesList();
        for (int i = 0; i < imageIds.size(); i++) {
            categories.add(new Category(categoryNames.get(i), imageIds.get(i)));
        }
        return categories;
    }

    /**
     * Adds a new money object to the current list of moneys.
     *
     * @param money the new money object
     */
    public void addMoney(Money money) {
        moneyList.add(money);
        dh.add(money);
    }

    /**
     * Get the money object from the money list given an index.
     *
     * @param position the index
     * @return the money object
     */
    public Money getMoneyAt(int position) {
//        int index = -1;
//        for (int i = 0; i < moneyList.size(); i++) {
//            Money p = moneyList.get(i);
//            if (p.getId() == position) {
//                index = i;
//                break;
//            }
//        }
//        //if found
//        if (index >= 0) {
//            return moneyList.get(index);
//        } else {
//            return null;
//        }
        return moneyList.get(position);
    }

    /**
     * Deletes a money object from the list based on index.
     *
     * @param position the index
     * @return true
     */
    public boolean deleteMoneyAt(int position) {
        Money deleted = moneyList.get(position);
        moneyList.remove(deleted);
        dh.delete(deleted);
        return true;
    }

    /**
     * Updates a money object after editing it.
     *
     * @param money    the new money object
     * @param position the position of the old money object
     */
    public void updateMoney(Money money, int position) {
        Money oldMoney = moneyList.get(position);
        dh.delete(oldMoney);
        dh.add(money); // replacing
        oldMoney.setAmount(money.getAmount());
        oldMoney.setAmount(money.getAmount());
        oldMoney.setDay(money.getDay());
        oldMoney.setMonth(money.getMonth());
        oldMoney.setYear(money.getYear());
        if (money.getMoneyType().equals("Income")) {
            oldMoney.setMoneyType("Income");
        } else {
            oldMoney.setMoneyType("Expense");
            if (oldMoney.getExpenseCategory() != null) {
                oldMoney.getExpenseCategory().setName(
                        money.getExpenseCategory().getName());
                oldMoney.getExpenseCategory().setImageResourceID(
                        money.getExpenseCategory().getImageResourceID());
            } else {
                oldMoney.setExpenseCategory(
                        new Category(money.getExpenseCategory().getName(),
                                money.getExpenseCategory().getImageResourceID()));
            }
        }
    }

    /**
     * Calculates the total dollar value of each category and
     * sets it in eachCategoryAmount.
     */
    public void eachCategoryAmount() {
        eachCategoryAmount = new ArrayList<>();
        categoryNames = new ArrayList<>();
        Map<String, Double> map = new HashMap<String, Double>();

        /*
         * Get the category, get the cost, check if the hashmap has this category already
         * if yes get the current spending on this category; add cost to current spending
         * put the new spending into the map; else category put the category,cost pair in the map
         * */
        for (int i = 0; i < moneyList.size(); i++) {
            Money money = moneyList.get(i);
            if (money.getExpenseCategory() != null) {
                String name = money.getExpenseCategory().getName();
                if (map.containsKey(name)) {
                    double dollar = map.get(name);
                    dollar += money.getAmount();
                    map.put(name, dollar);
                } else {
                    map.put(name, money.getAmount());
                }
            }
        }
        for (String key : map.keySet()) {
            categoryNames.add(key);
            eachCategoryAmount.add(map.get(key));
        }

    }

    /**
     * Accesses the total dollar value of each category.
     *
     * @return a list of totals from each category
     */
    public ArrayList<Double> getEachCategoryAmount() {
        return eachCategoryAmount;
    }

    /**
     * Accesses the names of each category
     *
     * @return a list of names of each category
     */
    public ArrayList<String> getCategoryNames() {
        return categoryNames;
    }
}