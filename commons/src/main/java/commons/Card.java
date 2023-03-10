package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

//import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "list_id", nullable = false)
//    @JsonBackReference
//    public CardList list;

    public long cardListId;

    public String title;

    @SuppressWarnings("unused")
    protected Card() {
        // for object mappers
    }

    public Card(long cardListId, String title) {
        this.cardListId = cardListId;
        this.title = title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setCardListId(long id){
        this.cardListId = id;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}