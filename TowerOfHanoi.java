import java.util.*;

public class TowerOfHanoi {
    static ArrayList<Integer> first;
    static ArrayList<Integer> second;
    static ArrayList<Integer> third;
    public static void main(String[] args) {
        int level = 5;

        first = new ArrayList<Integer>();
        for (int i = level - 1; i >= 0; i--) {
            first.add(i + 1);
        }
        second = new ArrayList<Integer>();
        third = new ArrayList<Integer>();

        solve(first, third, second, level);
    }

    public static void solve(ArrayList<Integer> from, ArrayList<Integer> to, ArrayList<Integer> tmp, int level){
        if (level == 1){
            to.add(from.remove(from.size() - 1));
            System.out.println(first);
            System.out.println(second);
            System.out.println(third);
            System.out.println();
        } else{
            solve(from, tmp, to, level - 1);
            to.add(from.remove(from.size() - 1));
            System.out.println(first);
            System.out.println(second);
            System.out.println(third);
            System.out.println();
            solve(tmp, to, from, level - 1);
        }
    }
}
