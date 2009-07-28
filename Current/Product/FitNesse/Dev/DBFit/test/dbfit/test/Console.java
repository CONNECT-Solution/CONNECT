package dbfit.test;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fit.Fixture;

public class Console {
	public static void main(String ars[]) throws SQLException{
		String query = "Select * from orders where ord_id != <<oldorderid";
		
		System.out.println(query);
		
		if (true){
			String string = "12/23/2005  asasd";
			System.out.println(string);
			
			Pattern pattern = Pattern.compile("^(\\d{1,2})/(\\d{1,2})/(\\d{4}).*");
	        Matcher matcher = pattern.matcher(string);
	        
	        matcher.matches();
	        String year = matcher.group(3);
	        System.out.println(year);
        }
		
		if (query.contains("<<")){
			System.out.println(query);
			Pattern pattern = Pattern.compile("^.{0,}<<(\\S+)\\s{0,}.{0,}$");
			Matcher matcher = pattern.matcher(query);
			
			matcher.matches();
	        
			int groups = matcher.groupCount();
			
			for (;groups > 0; groups--){
				String symbolName = matcher.group(groups);
				System.out.println(symbolName);
				query = query.replace("<<" + symbolName, ((Object)"hello").toString());
			}
		}

		System.out.println(query);
	}
}
