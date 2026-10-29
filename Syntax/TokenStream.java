// TokenStream.java

// Implementation of the Scanner for JAY

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {

	// Instance variables 
	private boolean isEof = false; // is end of file
	private char nextChar = ' '; // next character in input stream
	private BufferedReader input;

	// This function was added to make the demo file work
	public boolean isEof() {
		return isEof;
	}

	// Constructor
	public TokenStream(String fileName) {
		try {
			input = new BufferedReader(new FileReader(fileName));
			// Read the very first character to initialize nextChar
			nextChar = readChar();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
			isEof = true;
		}
	}

	public Token nextToken() { 
		Token t = new Token();
		t.setType("Other"); 
		t.setValue("");

		// First check for whitespaces and bypass them
		skipWhiteSpace();

		// Then check for a comment, and bypass it
		// but remember that / may also be a division operator.
		while (nextChar == '/') {
			nextChar = readChar();
			if (nextChar == '/') { // If / is followed by another /
				// skip rest of line - it's a comment.
				while (!isEndOfLine(nextChar) && !isEof) {
					nextChar = readChar();
				}
				skipWhiteSpace(); // skip the newline after the comment
			} else {
				// A slash followed by anything else must be an operator.
				t.setValue("/");
				t.setType("Operator");
				return t;
			}
		}

		// Then check for an operator
		if (isOperator(nextChar)) {
			t.setType("Operator");
			t.setValue(t.getValue() + nextChar);
			switch (nextChar) {
			case '<':
				// Check for <=
				nextChar = readChar();
				if (nextChar == '=') {
					t.setValue(t.getValue() + nextChar);
					nextChar = readChar();
				}
				return t;
			case '>':
				// Check for >=
				nextChar = readChar();
				if (nextChar == '=') {
					t.setValue(t.getValue() + nextChar);
					nextChar = readChar();
				}
				return t;
			case '=':
				// Check for ==
				nextChar = readChar();
				if (nextChar == '=') {
					t.setValue(t.getValue() + nextChar);
					nextChar = readChar();
				}
				return t;
			case '!':
				// Check for !=
				nextChar = readChar();
				if (nextChar == '=') {
					t.setValue(t.getValue() + nextChar);
					nextChar = readChar();
				}
				return t;
			case '|':
				// Look for ||
				nextChar = readChar();
				if (nextChar == '|') {
					t.setValue(t.getValue() + nextChar);
					nextChar = readChar();
					return t;
				} else {
					t.setType("Other");
				}
				return t;
			case '&':
				// Look for &&
				nextChar = readChar();
				if (nextChar == '&') {
					t.setValue(t.getValue() + nextChar);
					nextChar = readChar();
					return t;
				} else {
					t.setType("Other");
				}
				return t;
			default: // all other 1-character operators (+, -, *, /)
				nextChar = readChar();
				return t;
			}
		}

		// Then check for a separator
		if (isSeparator(nextChar)) {
			t.setType("Separator");
			t.setValue(t.getValue() + nextChar);
			nextChar = readChar();
			return t;
		}

		// Then check for an identifier or keyword
		if (isLetter(nextChar)) {
			t.setType("Identifier");
			while ((isLetter(nextChar) || isDigit(nextChar)) && !isEof) {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
			// now see if this is a keyword
			if (isKeyword(t.getValue())) {
				t.setType("Keyword");
			} else if (t.getValue().equals("true") || t.getValue().equals("false")) {
				t.setType("Literal");
			}
			
			return t; // Removed the isEndOfToken check here to prevent swallowing
		}

		// Check for integer literals
		if (isDigit(nextChar)) { 
			t.setType("Literal");
			while (isDigit(nextChar) && !isEof) {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
			return t;
		}

		// If we reach here, it's an unknown token (Other)
		if (isEof) {
			return t;
		}

		// Makes sure that the whole unknown token (Type: Other) is printed.
		while (!isEndOfToken(nextChar) && !isEof) {
			t.setValue(t.getValue() + nextChar);
			nextChar = readChar();
		}
		
		return t;
	}

	private char readChar() {
		int i = 0;
		if (isEof)
			return (char) 0;
		System.out.flush();
		try {
			i = input.read();
		} catch (IOException e) {
			System.exit(-1);
		}
		if (i == -1) {
			isEof = true;
			return (char) 0;
		}
		return (char) i;
	}

	private boolean isKeyword(String s) {
		// JAY Keywords (from Page 34)
		return s.equals("boolean") || s.equals("else") || s.equals("if") || 
		       s.equals("int") || s.equals("main") || s.equals("void") || s.equals("while");
	}

	private boolean isWhiteSpace(char c) {
		return (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f');
	}

	private boolean isEndOfLine(char c) {
		return (c == '\r' || c == '\n' || c == '\f');
	}

	private boolean isEndOfToken(char c) { 
		return (isWhiteSpace(nextChar) || isOperator(nextChar) || isSeparator(nextChar) || isEof);
	}

	private void skipWhiteSpace() {
		while (!isEof && isWhiteSpace(nextChar)) {
			nextChar = readChar();
		}
	}

	private boolean isSeparator(char c) {
		// JAY Separators (from Page 34)
		return c == '(' || c == ')' || c == '{' || c == '}' || c == ':' || c == '.';
	}

	private boolean isOperator(char c) {
		// JAY Operators (starts of operators, from Page 34)
		return c == '=' || c == '+' || c == '-' || c == '*' || c == '/' || 
		       c == '<' || c == '>' || c == '!' || c == '&' || c == '|';
	}

	private boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
	}

	private boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

	public boolean isEndofFile() {
		return isEof;
	}
}