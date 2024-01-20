import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class AIML {
	public static void main(String[]args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File("pubmed200Abstract.txt")));
		ArrayList<Integer> lineNumbers = new ArrayList<Integer>();
		int startNum = 2;
		
		int count = startNum;
		boolean check = true;
		String total = br.readLine();
		String line = br.readLine();
		int lineCount = 2;
		while(line != null) {
			if(line.length() > String.valueOf(count).length()+2 && line.substring(0, String.valueOf(count).length() + 2).equals(count + ". ")) {
				check = false;
				lineNumbers.add(lineCount);
				count++;
				
				
				
			}
			
			if(check) {
				if(line.contains(Character.toString((char)10))) {
					System.out.println("HERE");
					total += "\n";
				}
				total += line;
			}
			else {
				if(!total.equals("")) {
					System.out.println(total);
					Thread.sleep(10000);
					total = line;
				}
				check = true;
			}
			
			lineCount++;
			line = br.readLine();
		}
		
		
		br.close();
	}
}
