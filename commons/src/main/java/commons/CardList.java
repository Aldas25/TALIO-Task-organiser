package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String title;

    @OneToMany
    public List<Card> cards;

    @SuppressWarnings("unused")
    protected CardList() {
        // for object mapper
    }

    public CardList(String title) {
        this.title = title;
        this.cards = new ArrayList<>();
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