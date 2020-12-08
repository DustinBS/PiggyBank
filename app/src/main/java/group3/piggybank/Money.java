package group3.piggybank;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This data class keeps track of a money's dollar value, type, date, category,
 * and unique identifier (id).
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 2/20/2020
 */
public class Money {

    private double amount;
    private int day;
    private String month;
    private int year;
    private String moneyType;
    private Category expenseCategory;
    private long id;
    private static ArrayList<Integer> imageIds = createImageIdList();
    private static ArrayList<String> categoryNames = createCategoryNameList();

    /**
     * Default constructor necessary for Firebase.
     */
    public Money() {
    }

    /**
     * Full constructor requires amount, date, type, and category to
     * be initialized.
     *
     * @param amount       the dollar value of the money
     * @param day          the day of the transaction
     * @param month        the month of the transaction
     * @param year         the year of the transaction
     * @param moneyType    the type of money (Income or Expense)
     * @param categoryName the name of the category of this particular money
     */
    public Money(double amount, int day, int month, int year, String moneyType,
                 String categoryName) {
        this.amount = amount;
        this.day = day;
        String[] MONTHS = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        this.month = MONTHS[month - 1];
        this.year = year;
        this.moneyType = moneyType;
        int index = categoryNames.indexOf(categoryName);
        this.id = System.currentTimeMillis();
        if (this.moneyType.equals("Expense") && index != -1) {
            expenseCategory = new Category(categoryName, imageIds.get(index));
        } else {
            expenseCategory = null;
        }
    }

    /**
     * Accesses the dollar value of the money.
     *
     * @return the dollar value of the money
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Mutates the dollar value of the money.
     *
     * @param amount the new dollar value of the money.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Accesses the day of the transaction.
     *
     * @return the day of the transaction
     */
    public int getDay() {
        return day;
    }

    /**
     * Mutates the day of the transaction.
     *
     * @param day the new day of the transaction
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Accesses the month of the transaction.
     *
     * @return the month of the transaction
     */
    public String getMonth() {
        return month;
    }

    /**
     * Mutates the month of the transaction.
     *
     * @param month the new month of the transaction
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Accesses the year of the transaction.
     *
     * @return the year of the transaction
     */
    public int getYear() {
        return year;
    }

    /**
     * Mutates the year of the transaction.
     *
     * @param year the new year of the transaction
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Accesses the id for this money object.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Mutates the id for this money object.
     *
     * @param id the new id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Accesses the full date of the transaction.
     *
     * @return the full date of the transaction (ex. March 3 2020).
     */
    public String getDate() {
        return month + " " + day + ", " + year;
    }

    /**
     * Acceses the type of money.
     *
     * @return the type of money
     */
    public String getMoneyType() {
        return moneyType;
    }

    /**
     * Mutates the type of money.
     *
     * @param moneyType the new type of money
     */
    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    /**
     * Accesses the category object of this money.
     *
     * @return the category object of this money
     */
    public Category getExpenseCategory() {
        return expenseCategory;
    }

    /**
     * Mutates the category object of this money.
     *
     * @param expenseCategory the new category object of this money
     */
    public void setExpenseCategory(Category expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    /**
     * Compares a money's dollar value, date, type, and category to
     * determine if they are equal.
     *
     * @param o the other money object
     * @return true if equals, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Double.compare(money.amount, amount) == 0 &&
                day == money.day &&
                year == money.year &&
                month.equals(money.month) &&
                moneyType.equals(money.moneyType) &&
                Objects.equals(expenseCategory, money.expenseCategory);
    }

    /**
     * A money with the same dollar value, date (day, month, year), type, and category
     * will have the same hashcode.
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount, day, month, year, moneyType, expenseCategory);
    }

    /**
     * Creates a default list of category image ids.
     *
     * @return a list of category image ids
     */
    private static ArrayList<Integer> createImageIdList() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.beauty);
        list.add(R.drawable.clothing);
        list.add(R.drawable.education);
        list.add(R.drawable.entertainment);
        list.add(R.drawable.food);
        list.add(R.drawable.health);
        list.add(R.drawable.transportation);
        list.add(R.drawable.travel);
        return list;
    }

    /**
     * Creates a default list of category names.
     *
     * @return a list of category names
     */
    private static ArrayList<String> createCategoryNameList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Beauty");
        list.add("Clothing");
        list.add("Education");
        list.add("Entertainment");
        list.add("Food");
        list.add("Health");
        list.add("Transportation");
        list.add("Travel");
        return list;
    }

    /**
     * Accesses the list of category image ids.
     *
     * @return the list of category image ids
     */
    public static ArrayList<Integer> getImageIdList() {
        return imageIds;
    }

    /**
     * Accesses the list of category names.
     *
     * @return the list of category names
     */
    public static ArrayList<String> getCategoryNamesList() {
        return categoryNames;
    }

}

