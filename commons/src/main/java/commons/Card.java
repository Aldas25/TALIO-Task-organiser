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

    public Card(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == this.getClass()
                && ((Card) obj).id == this.id;
    }

    @Override
    public int hashCode() {
        return (int)id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}