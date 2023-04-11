package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.List;

@Entity
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String title;

    /**
     * EAGER fetching tells Hibernate to get the related entities with the initial query.
     * In this case, Hibernate will get cardList when doing queries with cards.
     */
    @OneToMany (fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    public List<Card> cards;

    @SuppressWarnings("unused")
    public CardList() {
        // for object mapper
    }

    /**
     * The constructor of CardList
     * @param title The title of the CardList
     * @param cards The list of cards in CardList
     */
    public CardList(String title, List<Card> cards) {
        this.title = title;
        this.cards = cards;
    }

    /**
     * Equals method
     * @param obj The object to check if equals
     * @return True if equals, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == this.getClass()
                && ((CardList) obj).id == this.id;
    }

    /**
     * Hash code function
     * @return The hash value
     */
    @Override
    public int hashCode() {
        return (int)id;
    }

    /**
     * @return The string version of this objet
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}