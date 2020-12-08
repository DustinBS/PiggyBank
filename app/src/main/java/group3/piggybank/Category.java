package group3.piggybank;

import java.util.Objects;

/**
 * This data class allows the handling of categories independently
 * (as a separate objects) of money.
 *
 * @author Serey Ratanak Roth
 * @author Dustin Bernard Sumarli
 * @author Chenyu Niu
 * @version 2/17/2020
 */
public class Category implements Comparable<Category> {

    private String name;
    private int imageResourceID;

    /**
     * Default constructor necessary for Firebase.
     */
    public Category() {
    }

    /**
     * Full constructor. Initializes the name of the category and the image.
     *
     * @param name            the name of the category
     * @param imageResourceID the image of the category
     */
    public Category(String name, int imageResourceID) {
        this.name = name;
        this.imageResourceID = imageResourceID;
    }

    /**
     * Accesses the name of the category.
     *
     * @return the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * Mutates the name of the category
     *
     * @param name the name of the category
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accesses the image of the category
     *
     * @return the image of the category
     */
    public int getImageResourceID() {
        return imageResourceID;
    }

    /**
     * Mutates the image of the category
     *
     * @param imageResourceID the image of the category
     */
    public void setImageResourceID(int imageResourceID) {
        this.imageResourceID = imageResourceID;
    }

    /**
     * Gives the name and image of the category.
     *
     * @return name: image
     */
    @Override
    public String toString() {
        return name + ": " + imageResourceID;
    }

    /**
     * Compares the name of a category to determine if they are the same.
     *
     * @param o the other category
     * @return true if they are equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    /**
     * Categories with the same name have the same hashcode.
     *
     * @return the hashcode value
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * A category is follows another category if its name lexicographically
     * follows the other category's name.
     *
     * @param category the other category
     * @return 0 if equals; less than 0 if the caller precedes the other;
     * greater than 0 if the caller follows the other.
     */
    @Override
    public int compareTo(Category category) {
        return category.getName().compareTo(name);
    }

}
