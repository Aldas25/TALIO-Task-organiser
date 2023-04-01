package commons;

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
}
