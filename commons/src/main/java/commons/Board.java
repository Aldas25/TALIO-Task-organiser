package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToMany (fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    public List<CardList> lists;

    public String title;

    public Board(String title, List<CardList> lists){
        this.title = title;
        this.lists = lists;
    }

    @SuppressWarnings("unused")
    public Board() {
        // for object mapper
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == this.getClass()
                && ((Board) obj).id == this.id;
        //return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return (int)id;
        //return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
