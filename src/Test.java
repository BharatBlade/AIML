import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.TreeSet;

public class Test {
	public static void main(String[]args) throws Exception {
		BufferedInputStream br = new BufferedInputStream(new FileInputStream(new File("pubmed200Abstract.txt")));
		ArrayList<Integer> tree = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> paps = new ArrayList<ArrayList<Integer>>();
		int a = br.read();
		while(a != -1) {
			tree.add(a);
			a = br.read();
		}
		
		System.out.println(tree);
		br.close();
	}
}
