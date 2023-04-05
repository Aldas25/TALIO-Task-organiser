package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * CustomPair<> class is used for pairing up two objects.
 *
 * @param <T1> - first object
 * @param <T2> - second object
 */
public class CustomPair<T1, T2> {
    public T1 id;
    public T2 var;

    @SuppressWarnings("unused")
    public CustomPair(){}

    public CustomPair(T1 id, T2 var) {
        this.id = id;
        this.var = var;
    }
    public T1 getId() {
        return this.id;
    }

    public T2 getVar() {
        return this.var;
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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
