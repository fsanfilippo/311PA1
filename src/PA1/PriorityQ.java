package PA1;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class PriorityQ {

    private List<Pair<String, Integer>> q;
    private List<Integer> pArray;

    public PriorityQ(){
        q = new ArrayList<>();
        pArray = new ArrayList<>();
    }

    public String toString(){
        String result = "Contents: \n";
        int i = 0;
        int j = 1;
        int k = 1;
        for(Pair<String, Integer> p: q){

            result += "( " + p.getPriority() + " )";
            i++;
            if(i == k){
                k += (int) Math.pow(2, j);
                j++;
                result += "\n";
            }

        }
        return result;
    }

    public boolean add(String s, int p){
        q.add(new Pair<String, Integer>(s, p));
        percolateUp();
        return true;
    }
    public String returnMax(){
        return q.get(0).getValue();
    }
    public String extractMax(){
        Collections.swap(q, 0, q.size() - 1);
        Pair<String, Integer> p = q.remove(q.size() - 1);
        percolateDown(0);
        return p.getValue();
    }
    public Pair<String, Integer> remove(int i){
        if(outBounds(i) || isEmpty()){
            return null;
        }

        Collections.swap(q, i, q.size() - 1);
        Pair<String, Integer> p = q.remove(q.size() - 1);
        percolateDown(i);
        return p;
    }

    public boolean decrementPriority(int i, int k){
        if(outBounds(i)){
            return false;
        }

        Pair<String, Integer> p = q.get(i);
        p.setPriority(p.getPriority() - k);
        q.set(i, p);
        percolateDown(i);
        return true;
    }

    //Not implemented Yet
    public List<Integer> priorityArray(){
        return pArray;
    }

    public int getKey(int i){
        if(outBounds(i)){
            return -1;
        }
        return q.get(i).getPriority();
    }

    public String getValue(int i){
        if(outBounds(i)){
            return "Your'e Out of Bounds pal";
        }
        return q.get(i).getValue();
    }

    public boolean isEmpty(){
        return q.isEmpty();
    }

    private boolean percolateUp(){
        if(q.isEmpty()){
            return true;
        }

        int childIndex = q.size() - 1;

        int parentIndex = (int) Math.ceil(((double) childIndex/2)) - 1;
        Pair<String, Integer> parent;
        Pair<String, Integer> child;
        while(childIndex != 0){
            child = q.get(childIndex);
            parent = q.get(parentIndex);
            if(parent.getPriority() < child.getPriority()){
                Collections.swap(q, childIndex, parentIndex);
                childIndex = parentIndex;
                parentIndex = (int) Math.ceil(((double) childIndex/2)) - 1;
            }
            else{
                return true;
            }
        }
        return true;
    }

    private boolean percolateDown(int i){
        if(outBounds(i)){
            return false;
        }

        Pair<String, Integer> p = q.get(i);
        Pair<String, Integer> LChild;
        Pair<String, Integer> RChild;
        while((i * 2) + 1 < q.size()){
            int LChildIndex = (i * 2) + 1;
            int RChildIndex = (i * 2) + 2;
            LChild = q.get(LChildIndex);
            RChild = (RChildIndex > q.size() - 1)?null:q.get(RChildIndex);
            //Smaller than one of them?
            if(p.getPriority() < LChild.getPriority() || ((RChild != null) && p.getPriority() < RChild.getPriority())){
                //Is it the left one?
                if (RChild == null || LChild.getPriority() > RChild.getPriority()){
                    Collections.swap(q, i, LChildIndex);
                    i = LChildIndex;
                }
                //It's the right one
                else{
                    Collections.swap(q, i, RChildIndex);
                    i = RChildIndex;
                }
            }
            //it's percolated all the way down
            else{
                return true;
            }
        }
        return true;
    }

    public boolean outBounds(int i){
        return i < 0 || i >= q.size();
    }

}
