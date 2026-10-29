/**
 * @author scharffc
 * 
 */
public class ScannerDemo {

	// Changed this to look for the file in the project root folder
	private static String file1 = "COMPILER/prog1.jay";
	private static int counter = 1;

	public static void main(String args[]) {
		TokenStream ts = new TokenStream(file1);
		System.out.println(file1);
		
		while (!ts.isEndofFile()) {
			Token tk = ts.nextToken();
			// Print the token type and value
			System.out.println("Token " + counter++ + " - Type: " + tk.getType() + " - Value: " + tk.getValue());
		}
	}
}