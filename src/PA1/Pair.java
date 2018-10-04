package PA1;

public class Pair<L,R> {

    private  L left;
    private  R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getValue() { return left; }
    public R getPriority() { return right; }
    public void setValue(L val){this.left = val;}
    public void setPriority(R priority){this.right = priority;}

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return this.left.equals(pairo.getValue()) &&
                this.right.equals(pairo.getPriority());
    }

}