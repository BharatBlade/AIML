import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

public class Paper {
	
	public String source = "";
	public String title = "";
	public String year = "";
	public String Abstract = "";
	public String doi = "";
	public String pmid = "";
	public ArrayList<Author> authors = new ArrayList<Author>();
	
	
	public Paper(String type) {
		source = type;
	}
	
	public Paper(String type, String total) throws Exception {
		source = type;
		if(type.toLowerCase().equals("pubmed")) {
			BufferedReader br = new BufferedReader(new StringReader(total));
			
			String line = br.readLine();
			ArrayList<String> parts = new ArrayList<String>();
			String part = "";
			while(line != null) {
				if(line.charAt(4) == '-') {
					parts.add(part.trim());
					part = line;						
				}
				else {
					part += line.trim() + " ";
				}
				line = br.readLine();
			}
			br.close();
			
			for(int i = 0; i < parts.size(); i++) {
				if(parts.get(i).length() > 5) {
					String prefix = parts.get(i).substring(0, 6);
					String suffix = parts.get(i).substring(6);
					if(prefix.equals("AD  - ") && parts.get(i+1).substring(0, 6).equals("AD  - ")) {
						parts.set(i, (parts.get(i+1) + " " + suffix).trim());
						parts.remove(i+1);
						i--;
					}					
				}
				
			}
			
			
			for(int i = 0; i < parts.size(); i++) {
				
				if(parts.get(i).length() > 5) {
					
					String prefix = parts.get(i).substring(0, 6);
					String suffix = parts.get(i).substring(6);
					
					if(prefix.equals("PMID- ")) {
						pmid = suffix;
					}
					else if(prefix.equals("DP  - ")) {
						year = suffix.substring(0, 4);
					}
					else if(prefix.equals("TI  - ")) {
						title = suffix;
					}
					else if(prefix.equals("LID - ")) {
						doi = suffix;
					}
					else if(prefix.equals("AB  - ")) {
						Abstract = suffix;
					}
					else if(prefix.equals("FAU - ")) {
						Author author = new Author("pubmed", parts.get(i), parts.get(i+1), parts.get(i+2));
						//System.out.println(author);
						int tempAuthor = Constants.authorExists(author);
						if(tempAuthor == -1) {
							Constants.authorsTotal.add(author);
						}
						else {
							//Constants.authorsTotal.get(tempAuthor).papers.add(this);
							//go through papers, match authors to authorsTotal, add papers then
						}
						authors.add(author);
					}
					
				}
				
				
			}
			
		}
		
		
	}
	
	public boolean equals(Paper p) {
		
		if(!title.isBlank() && title.toLowerCase().trim().equals(p.title.toLowerCase().trim())) {
			//System.out.println("### " + doi + ", " + p.doi);
			return true;
		}
		if(!doi.isBlank() && doi.toLowerCase().trim().equals(p.doi.toLowerCase().trim())) {
			//System.out.println("### " + doi + ", " + p.doi);
			return true;
		}
		if(!pmid.isBlank() && pmid.toLowerCase().trim().equals(p.pmid.toLowerCase().trim())) {
			//System.out.println("### " + doi + ", " + p.doi);
			return true;
		}
		return false;

	}
	
//	public String toString() {
//		return title + " --- " + year + " --- " + Abstract + " --- " + doi + " --- " + pmid + " --- " + authors.size();
//	}
//	public String toString() {
//		return pmid + " --- " + authors.size();
//	}
	public String toString() {
		return doi + " --- " + authors.size() + " --- " + title;
	}
	
}
