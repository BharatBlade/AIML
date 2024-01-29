import java.util.ArrayList;

public class Constants {
	public static ArrayList<Paper> papersTotal = new ArrayList<Paper>();
	public static ArrayList<Author> authorsTotal = new ArrayList<Author>();

	
	public static int authorExists(Author author) {
		for(int i = 0; i < authorsTotal.size(); i++) {
			if(author.fullname.equals(authorsTotal.get(i).fullname) && author.university.equals(authorsTotal.get(i).university)) {
				return i;				
			}
		}
		return -1;
	}
	
	public static boolean paperExists(Paper b) {
		for(int i = 0; i < papersTotal.size(); i++) {
			Paper a = papersTotal.get(i);
			if(a.equals(b)) {
				return true;
			}
		}
		return false;
	}

}
