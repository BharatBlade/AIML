import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class PubMed {
	public static void main(String[]args) throws Exception {
		pubmedParser("pubmed200.txt");
		pubmedParser("pubmed89.txt");
		embaseParser("embase2.csv");
		scopusParser("scopus2.csv");
		updatePapers();
		UpstateAuthors();
		
		
		
		for(int i = 0; i < Constants.papersTotal.size(); i++) {
			//System.out.println(Constants.papersTotal.get(i).title);
		}
		for(int i = 0; i < Constants.papersTotal.size(); i++) {
			for(int j = i; j < Constants.papersTotal.size(); j++) {
				if( i != j) {
					if(Constants.papersTotal.get(i).equals(Constants.papersTotal.get(j))) {
						System.out.println(i + ", " + j + " " + " ---------------------------- " + Constants.papersTotal.get(i).title + " --- " + Constants.papersTotal.get(j).title);
						System.out.println(Constants.papersTotal.get(i).title.equals(Constants.papersTotal.get(j).title));
					}
					
				}
			}
		}
		System.out.println(Constants.papersTotal.size());
		
	}
	
	public static void pubmedParser(String file) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		String line = br.readLine();
		
		String total = "";
		while(line != null) {
			if(line.isBlank()) {
//				System.out.println(total);
				
				Paper paper = new Paper("pubmed", total);
				
				
				if(!Constants.paperExists(paper))
					Constants.papersTotal.add(paper);
				
				//System.out.println(Constants.papersTotal.size() + ", " + Constants.authorsTotal.size() + ", " + paper);
				
				total = "";
				//Thread.sleep(1000);
			}
			else {
				total += line + "\n";
			}
			line = br.readLine();
		}
		
		br.close();
	}
	
	public static void embaseParser(String file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		String line = br.readLine();
		Paper paper = null;
		while(line != null) {
			if(line.length() > 3) {
				line = line.substring(1, line.length()-1);
				String [] arr = line.split("\",\"");
				//System.out.println(Arrays.toString(arr));
				
				if(arr[0].equals("TITLE")) {
					if(paper != null && !Constants.paperExists(paper)) {
						
						for(int i = 0; i < paper.authors.size(); i++) {
							int pos = Constants.authorExists(paper.authors.get(i));
							Constants.authorsTotal.get(pos).papers.add(paper);
						}
						
						Constants.papersTotal.add(paper);
					}
					paper = new Paper("embase");
					paper.title = arr[1].replace(".", "");
				}
				else if(arr[0].equals("PUBLICATION YEAR")) {
					paper.year = arr[1].replace(".", "");
				}
				else if(arr[0].equals("ABSTRACT")) {
					for(int i = 1; i < arr.length; i++) {
						paper.Abstract += arr[i];
					}
				}
				else if(arr[0].equals("DOI")) {
					if(arr[1].charAt(arr[1].length()-1) == '.') {
						arr[1] = arr[1].substring(0, arr[1].length() - 1);
					}
					paper.doi = arr[1];
				}
				else if(arr[0].equals("MEDLINE PMID")) {
					paper.pmid = arr[1].replace(".", "");
				}
				else if(arr[0].equals("AUTHOR NAMES")) {
					String line2 = br.readLine();
					//System.out.println(line2);
					String [] arr2 = line2.split("\",\"");
					for(int i = 1; i < arr2.length; i++) {
						String names = arr2[i].substring(arr2[i].indexOf("(")+1, arr2[i].indexOf(")"));
						//System.out.println(names);
						String location = arr2[i].substring(arr2[i].indexOf(") ")+2);
						for(int j = 1; j < arr.length; j++) {
							if(names.contains(arr[j])) {
								Author author = new Author(arr[j], location);
								//System.out.println(author);
								Constants.authorsTotal.add(author);
								paper.authors.add(author);
							}
						}
					}
					
				}
				
			}
			line = br.readLine();
		}
		
//		for(int i = 0; i < Constants.embasePapers.size(); i++) {
//			System.out.println(Constants.embasePapers.get(i));
//		}
//		System.out.println(Constants.embasePapers.size());
	}
	
	public static void scopusParser(String file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		br.readLine();
		String line = br.readLine();
		while(line != null) {
			line = line.substring(1, line.length()-1);
			String [] arr = line.split("\",\"");
			
			
			ArrayList<String> FAUs = new ArrayList<String>(Arrays.asList(arr[1].split("; ")));
			ArrayList<String> AUs = new ArrayList<String>(Arrays.asList(arr[0].split("; ")));
			ArrayList<String> preADs = new ArrayList<String>(Arrays.asList(arr[16].split("; ")));
			
			
			for(int i = 0; i < preADs.size(); i++) {
				preADs.set(i, preADs.get(i).substring(preADs.get(i).indexOf(",")+2));
			}
			
			Paper paper = new Paper("scopus");
			paper.title = arr[3];
			paper.Abstract = arr[17];
			paper.doi = arr[13];
			paper.pmid = arr[24];
			paper.year = arr[4];
			
			//System.out.println(arr[3] + ", " + FAUs.size() + ", " + AUs.size() + ", " + preADs.size());
			if(FAUs.size() == AUs.size() && FAUs.size() == preADs.size()) {
				for(int i = 0; i < FAUs.size(); i++) {
					Author author = new Author("scopus", FAUs.get(i), AUs.get(i), preADs.get(i));
					int pos = Constants.authorExists(author);
					if(pos > -1) {
						paper.authors.add(Constants.authorsTotal.get(pos));
					}
					else {
						Constants.authorsTotal.add(author);
						paper.authors.add(Constants.authorsTotal.get(Constants.authorsTotal.size()-1));					
					}
				}
				
				if(!Constants.paperExists(paper)) {
					Constants.papersTotal.add(paper);
				}
			}
			line = br.readLine();
			
			
		}
	}
	
	
	public static void updatePapers() throws Exception{
		for(int i = 0; i < Constants.papersTotal.size(); i++) {
			for(int j = 0; j < Constants.papersTotal.get(i).authors.size(); j++) {
				int pos = Constants.authorExists(Constants.papersTotal.get(i).authors.get(j));
				if(pos > -1) {
					Constants.authorsTotal.get(pos).papers.add(Constants.papersTotal.get(i));
				}
			}
		}
	}
	
	public static void UpstateAuthors() throws Exception{
		ArrayList<String> entries = new ArrayList<String>();
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();

		for(int i = 0; i < Constants.authorsTotal.size(); i++) {
			if(Constants.authorsTotal.get(i).university.toLowerCase().contains("upstate")) {
//				if(authors.contains(Constants.authorsTotal.get(i).fullname)) {
//					int id = authors.indexOf(Constants.authorsTotal.get(i).fullname);
//					
//					papers.set(id, papers.get(id) + Constants.authorsTotal.get(i).papers.size());
//				}
//				else {
//					authors.add(Constants.authorsTotal.get(i).fullname);
//					papers.add(0);
//				}
				
				entries.add(Constants.authorsTotal.get(i).fullname + " --- " + Constants.authorsTotal.get(i).papers.size());
			}
		}
		
		for(int i = 0; i < entries.size(); i++) {
			String s = entries.get(i);
			String key = s.substring(0, s.indexOf(" --- "));
			int value = Integer.parseInt(s.substring(s.indexOf(" --- ") + 5));
			
			if(map.containsKey(key)) {
				map.put(key, map.get(key) + value);
			}
			else {
				map.put(key, value);
			}
		}
		
		while(map.size() > 0) {
			System.out.println(map.firstEntry());
			map.remove(map.firstKey());
		}
		
//		for(int i = 0; i < authors.size(); i++) {
//			System.out.println(papers.get(i) + ", " + authors.get(i));
//		}
	}
}
