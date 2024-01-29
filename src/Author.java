import java.util.ArrayList;

public class Author {
	public String fullname = "";
	public String abbrevName = "";
	public String university = "";
	public ArrayList<Paper> papers = new ArrayList<Paper>();
	
	public Author(String type, String FAU, String AU, String AD) {
		if(type.equals("pubmed")) {
			fullname = FAU.substring(6);
			abbrevName = AU.substring(6);
			university = AD.substring(6);
		}
		else if(type.equals("scopus")) {
			fullname = FAU;
			abbrevName = AU;
			university = AD;
		}
	}
	
	public Author(String name, String location) {
			fullname = name;
			university = location;
	}
	
	public String toString() {
		return fullname + " --- " + university;
	}
}
