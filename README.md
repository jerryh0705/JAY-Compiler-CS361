Here is a professional `README.md` file you can create in your `Syntax` folder. It explains the project, how the testing was conducted based on the Word document, and includes the expected outputs for both the Scanner (Lexical Analysis) and the Parser (Syntactic Analysis).

You can copy and paste the markdown below into a new file named **`README.md`**.

***

# JAY Language Compiler (CS 361)
This project implements the **Lexical Analysis (Scanner)** and **Syntactic Analysis (Parser)** phases of a compiler for a subset of the JAY programming language.

## Project Structure
*   **`ScannerDemo.java`**: Runs the lexical analyzer and prints the token stream.
*   **`TokenStream.java`**: Implements the scanner (lexer) to break the source code into valid JAY tokens.
*   **`ConcreteSyntax.java`**: Implements the recursive descent parser to build an Abstract Syntax Tree (AST).
*   **`ParserDemo.java`**: Runs the parser on a valid JAY program and prints the AST.
*   **`Program.java`**: Defines the abstract syntax tree node classes (provided).
*   **`COMPILER/`**: Contains the `.jay` input files used for testing.

---

## Phase 1: Testing the Scanner (Lexical Analysis)

The scanner was tested using a comprehensive JAY file containing keywords, separators, operators, identifiers, literals, and deliberate lexical errors.

### Test Input (`COMPILER/prog1.jay`)
```java
void main(){
    int i;
    i = 7;
}
```

### How to Run
```bash
javac *.java
java ScannerDemo
```

### Expected Output
The scanner successfully skips comments and whitespace, reads multi-character operators (like `<=`, `!=`), and correctly classifies tokens. For the test file above, the output is:

```text
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
```

### Explanation of Scanner Behavior
*   **Comments**: The scanner successfully ignores `// comment` lines, preventing them from being tokenized.
*   **Keywords vs. Identifiers**: `do`, `True`, and `X3` are correctly identified as `Identifier`, while `while` and `int` are identified as `Keyword`. JAY is case-sensitive.
*   **Lexical Errors**: Invalid tokens (like `@`, `&`, or `3aaa2`) are grouped into the `Type: Other` category to prevent the scanner from crashing, allowing the parser to handle syntax errors gracefully.

---

## Phase 2: Testing the Parser (Syntactic Analysis)

The parser takes the token stream from the scanner and attempts to build an Abstract Syntax Tree (AST) based on the JAY Concrete Syntax grammar rules.

### Test Input (`COMPILER/test.jay`)
*Note: The parser requires a syntactically valid JAY program to test the AST generation.*
```java
void main(){
    int i;
    i = 7;
}
```

### How to Run
```bash
javac *.java
java ParserDemo
```

### Expected Output
If the program is syntactically correct, the parser outputs the AST:

```text
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
```

### Explanation of Parser Behavior
*   **AST Structure**: The root node is a `Program`. It contains a `Declarations` node and a `Block` (body) node.
*   **Declarations**: The parser correctly identifies `int i;` and stores it as a declaration with variable `i` and type `int`.
*   **Assignments**: Inside the block, `i = 7;` is parsed as an `Assignment` statement. The target is a `Variable` (`i`) and the source is a `Value` (`7`).
*   **Syntax Errors**: If an invalid statement is provided (e.g., missing a semicolon or using an undeclared type), the parser will catch it and print a message like: `Syntax error - Expecting: ; But saw: Separator = }`.

---
*Created for CS 361. Test cases derived from the official JAY Scanner Testing document.*
