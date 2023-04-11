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

    /**
     * The constructor of CustomPair
     * @param id The id of custom pair
     * @param var The var of custom pair
     */
    public CustomPair(T1 id, T2 var) {
        this.id = id;
        this.var = var;
    }

    /**
     * Getter of id
     * @return The id
     */
    public T1 getId() {
        return this.id;
    }

    /**
     * Getter of var
     * @return The var
     */
    public T2 getVar() {
        return this.var;
    }

    /**
     * Equals method
     * @param obj The object to check if equals
     * @return True if equals, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hash code function
     * @return The hash value
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * @return The string version of this objet
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
