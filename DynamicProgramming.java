import java.util.ArrayList;

public class DynamicProgramming extends Algorithm {

    public DynamicProgramming(int width, int height, int depth, ArrayList<Integer> values, char[] inputArray)
    {
        super(width,height,depth,values,inputArray);
    }
    @Override
    public int[][][] solve() {
        //here should be your starting method
        return this.boxOfIDs;
    }
}
