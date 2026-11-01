# CS 361 - JAY Compiler Project

**Author:** Jerry, Nick
**Course:** CS 361 - Concepts of Programming Languages

## Project Overview

For this assignment, we were tasked with completing the Lexical Analyzer (Scanner) and the Syntactic Analyzer (Parser) for a subset of the JAY programming language.

*   **Lexical Analysis (Scanner):** Takes the raw text from a `.jay` file and breaks it down into a stream of valid tokens (Keywords, Identifiers, Literals, Operators, Separators).
*   **Syntactic Analysis (Parser):** Takes the token stream and uses a Recursive Descent algorithm to build an Abstract Syntax Tree (AST) to ensure the code follows the grammar rules.

## Files Included

*   **`ScannerDemo.java`** - The main driver file to run the scanner.
*   **`TokenStream.java`** - The completed scanner logic (handles comments, whitespace, multi-character operators).
*   **`ParserDemo.java`** - The main driver file to run the parser.
*   **`ConcreteSyntax.java`** - The completed recursive descent parser (builds the AST).
*   **`Program.java`** - The provided AST node classes.
*   **`COMPILER/`** - Folder containing the test files.

## Testing the Scanner

To test the scanner, I created a comprehensive file based on the provided Word document that included valid keywords, identifiers, long integers, and deliberate lexical errors.

Expected Output (Snippet)
When running the scanner on the test file, the output correctly identifies the type and value of every token while skipping over comments.


Token 1 - Type: Keyword - Value: void
Token 2 - Type: Keyword - Value: main
Token 3 - Type: Separator - Value: (
Token 4 - Type: Separator - Value: )
Token 5 - Type: Separator - Value: {
Token 6 - Type: Keyword - Value: int
Token 7 - Type: Identifier - Value: i
Token 8 - Type: Separator - Value: ;
Token 9 - Type: Identifier - Value: i
Token 10 - Type: Operator - Value: =
Token 11 - Type: Literal - Value: 7
Token 12 - Type: Separator - Value: ;
Token 13 - Type: Separator - Value: }

Explanation: The scanner works perfectly. Things like True and do are correctly classified as Identifier (since JAY is case-sensitive and True isn't a keyword), and bad tokens like @ or & are safely categorized as Type: Other instead of crashing the program.

** Testing the Parser
After confirming the scanner works, I tested the parser using a syntactically valid program.

How to Run

javac *.java
java ParserDemo

Expected Output
The parser successfully reads the tokens and generates the Abstract Syntax Tree (AST).


Abstract syntax of the JAY Program: 
  Declarations: 
    Declarations = {<i, int>}
  Block: 
    Assignment: 
      Variable: 
        i
      Value: 
        7
Parsing successful!
Explanation: The parser correctly interprets the int i; declaration and the i = 7; assignment. It builds the tree node by node using the grammar rules we learned in class, proving that the token stream is syntactically correct for JAY.

