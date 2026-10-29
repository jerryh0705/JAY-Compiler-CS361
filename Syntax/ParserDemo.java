public class ParserDemo {

	public static void main(String[] args) {

		// Change the path if necessary (matches where your prog1.jay file is)
		TokenStream tStream = new TokenStream("COMPILER/test.jay");

		ConcreteSyntax cSyntax = new ConcreteSyntax(tStream);
		
		try {
			Program p = cSyntax.program();
			System.out.println(p.display());
			System.out.println("Parsing successful!");
		} catch (Exception e) {
			System.out.println("Parsing failed due to a syntax error.");
		}
	}
}