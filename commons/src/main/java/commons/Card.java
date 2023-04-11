package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import javax.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String title;

    @SuppressWarnings("unused")
    public Card() {
        // for object mappers
    }

    /**
     * The constructor of the Card
     * @param title The title of the Card
     */
    public Card(String title) {
        this.title = title;
    }

    /**
     * Equals method
     * @param obj The object to check if equals
     * @return True if equals, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == this.getClass()
                && ((Card) obj).id == this.id;
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