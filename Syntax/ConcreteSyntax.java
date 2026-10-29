// ConcreteSyntax.java
// Implementation of the Recursive Descent Parser algorithm for JAY

public class ConcreteSyntax {

	// Instance variables
	public Token token; // current token that is considered from the input stream
	public TokenStream input; // stream of tokens generated in by the lexical analysis

	// Constructor
	public ConcreteSyntax(TokenStream ts) { 
		input = ts; 
		token = input.nextToken(); 
	}

	// Method that prints a syntax error message
	private String SyntaxError(String tok) {
		String s = "Syntax error - Expecting: " + tok + " But saw: "
				+ token.getType() + " = " + token.getValue();
		System.out.println(s);
		return s;
	}

	// Match a string with the value of a token.
	private void match(String s) {
		if (token.getValue().equals(s))
			token = input.nextToken();
		else
			throw new RuntimeException(SyntaxError(s));
	}

	// Implementation of the Recursive Descent Parser

	public Program program() {
		// Program --> void main ( ) { Declarations Statements }
		Program p = new Program();
		
		// Match the required header
		match("void");
		match("main");
		match("(");
		match(")");
		match("{");
		
		p.decpart = declarations();
		p.body = statements();
		
		match("}");
		return p;
	}

	private Declarations declarations() {
		// Declarations --> { Declaration }*
		Declarations ds = new Declarations();
		// JAY grammar uses 'int' and 'boolean' for types
		while (token.getValue().equals("int") || token.getValue().equals("boolean")) {
			declaration(ds);
		}
		return ds;
	}

	private void declaration(Declarations ds) {
		// Declaration --> Type Identifiers ;
		Type t = type();
		identifiers(ds, t);
		match(";");
	}

	private Type type() {
		// Type --> int | boolean
		Type t = null;
		if (token.getValue().equals("int"))
			t = new Type(Type.INTEGER);
		else if (token.getValue().equals("boolean"))
			t = new Type(Type.BOOLEAN);
		else
			throw new RuntimeException(SyntaxError("int | boolean"));
		
		token = input.nextToken(); // pass over the type
		return t;
	}

	private void identifiers(Declarations ds, Type t) {
		// Identifiers --> Identifier { , Identifier }*
		Declaration d = new Declaration(); // first declaration
		d.t = t; 
		if (token.getType().equals("Identifier")) {
			d.v = new Variable();
			d.v.id = token.getValue(); 
			ds.addElement(d);
			token = input.nextToken();
			while (token.getValue().equals(",")) {
				d = new Declaration(); // next declaration
				d.t = t; 
				token = input.nextToken();
				if (token.getType().equals("Identifier")) {
					d.v = new Variable(); 
					d.v.id = token.getValue();
					ds.addElement(d);
					token = input.nextToken(); // get "," or ";"
				} else
					throw new RuntimeException(SyntaxError("Identifier"));
			}
		} else
			throw new RuntimeException(SyntaxError("Identifier"));
	}

	private Statement statement() {
		// Statement --> ; | Block | Assignment | IfStatement | WhileStatement
		Statement s = new Skip();
		if (token.getValue().equals(";")) { // Skip
			token = input.nextToken();
			return s;
		} else if (token.getValue().equals("{")) { // Block
			token = input.nextToken();
			s = statements();
			match("}");
			return s; // Added return here
		} else if (token.getValue().equals("if")) // IfStatement
			s = ifStatement();
		else if (token.getValue().equals("while")) { // WhileStatement
			s = whileStatement(); // Completed
		} else if (token.getType().equals("Identifier")) { // Assignment
			s = assignment(); // Completed
		} else
			throw new RuntimeException(SyntaxError("Statement"));
		return s;
	}

	private Block statements() {
		// Block --> { Statement }*
		Block b = new Block();
		while (!token.getValue().equals("}") && !input.isEndofFile()) {
			b.blockmembers.addElement(statement());
		}
		return b;
	}

	private Assignment assignment() {
		// Assignment --> Identifier = Expression ;
		Assignment a = new Assignment();
		if (token.getType().equals("Identifier")) {
			a.target = new Variable();
			a.target.id = token.getValue();
			token = input.nextToken();
			match("="); // Changed from := to = to match JAY
			a.source = expression();
			match(";");
		} else
			throw new RuntimeException(SyntaxError("Identifier"));
		return a;
	}

	private Expression expression() {
		// Expression --> Conjunction { || Conjunction }*
		Binary b;
		Expression e;
		e = conjunction();
		while (token.getValue().equals("||")) {
			b = new Binary();
			b.term1 = e;
			b.op = new Operator(token.getValue());
			token = input.nextToken();
			b.term2 = conjunction();
			e = b;
		}
		return e;
	}

	private Expression conjunction() {
		// Conjunction --> Relation { && Relation }*
		Binary b;
		Expression e;
		e = relation();
		while (token.getValue().equals("&&")) {
			b = new Binary();
			b.term1 = e;
			b.op = new Operator(token.getValue());
			token = input.nextToken();
			b.term2 = relation();
			e = b;
		}
		return e;
	}

	private Expression relation() {
		// Relation --> Addition { [< | <= | > | >= | == | !=] Addition }*
		Binary b;
		Expression e;
		e = addition();
		while (token.getValue().equals("<") || token.getValue().equals("<=") ||
			   token.getValue().equals(">") || token.getValue().equals(">=") ||
			   token.getValue().equals("==") || token.getValue().equals("!=")) {
			b = new Binary();
			b.term1 = e;
			b.op = new Operator(token.getValue());
			token = input.nextToken();
			b.term2 = addition();
			e = b;
		}
		return e;
	}

	private Expression addition() {
		// Addition --> Term { [ + | - ] Term }*
		Binary b;
		Expression e;
		e = term();
		while (token.getValue().equals("+") || token.getValue().equals("-")) {
			b = new Binary();
			b.term1 = e;
			b.op = new Operator(token.getValue());
			token = input.nextToken();
			b.term2 = term();
			e = b;
		}
		return e;
	}

	private Expression term() {
		// Term --> Negation { [ * | / ] Negation }*
		Binary b;
		Expression e;
		e = negation();
		while (token.getValue().equals("*") || token.getValue().equals("/")) {
			b = new Binary();
			b.term1 = e;
			b.op = new Operator(token.getValue());
			token = input.nextToken();
			b.term2 = negation();
			e = b;
		}
		return e;
	}

	private Expression negation() {
		// Negation --> { ! }opt Factor
		Unary u;
		if (token.getValue().equals("!")) {
			u = new Unary();
			u.op = new Operator(token.getValue());
			token = input.nextToken();
			u.term = factor();
			return u;
		} else
			return factor();
	}

	private Expression factor() {
		// Factor --> Identifier | Literal | ( Expression )
		Expression e = null;
		if (token.getType().equals("Identifier")) {
			Variable v = new Variable();
			v.id = token.getValue();
			e = v;
			token = input.nextToken();
		} else if (token.getType().equals("Literal")) {
			Value v = null;
			if (isInteger(token.getValue()))
				v = new Value(Integer.parseInt(token.getValue()));
			else if (token.getValue().equals("true"))
				v = new Value(true);
			else if (token.getValue().equals("false"))
				v = new Value(false);
			else
				throw new RuntimeException(SyntaxError("Literal"));
			e = v;
			token = input.nextToken();
		} else if (token.getValue().equals("(")) {
			token = input.nextToken();
			e = expression();
			match(")");
		} else
			throw new RuntimeException(SyntaxError("Identifier | Literal | ("));
		return e;
	}

	private Conditional ifStatement() {
		// IfStatement --> if ( Expression ) Statement { else Statement }opt
		Conditional c = new Conditional();
		match("if");
		match("(");
		c.test = expression();
		match(")");
		c.thenbranch = statement();
		
		// Check if there is an else branch
		if (token.getValue().equals("else")) {
			token = input.nextToken();
			c.elsebranch = statement();
		}
		return c;
	}

	private Loop whileStatement() {
		// WhileStatement --> while ( Expression ) Statement
		Loop l = new Loop();
		match("while");
		match("(");
		l.test = expression();
		match(")");
		l.body = statement();
		return l;
	}

	private boolean isInteger(String s) {
		boolean result = true;
		for (int i = 0; i < s.length(); i++)
			if ('0' > s.charAt(i) || '9' < s.charAt(i))
				result = false;
		return result;
	}
}