package base.Entities;

import javax.persistence.Entity;

/**
 * @author Rares.
 */

@Entity
public class Movie extends BaseEntity<Integer> {

    private String title;
    private int price;

    public Movie( String initTitle, int initPrice )
    {
        this.title=initTitle;
        this.price = initPrice;
    }

    public Movie() {

    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + getId() + '\'' +
                " title='" + title + '\'' +
                ", price=" + price +
                '}';
    }


    public int getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
