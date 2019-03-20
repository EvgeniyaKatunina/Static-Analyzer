# Java Static Analyzer

## Usage
Take .zip or .tar from releases (https://github.com/EvgeniyaKatunina/Static-Analyzer/releases). Just run .bat or shell script without any arguments.

## Checks
1. Static final variables should be in upper camel case.
2. Other variables should be in lower camel case.
3. Block statements (do, while, for, foreach, synchronized, try, catch) should not have an empty body.
4. 'Then' and 'else' branches of if statement should not be equal.
5. Class should have a package.

## Output Example
```
Specify full path to file or directory you want to analyze.
C:\AnalyzerTests
Analyzing file: C:\AnalyzerTests\Main.java
[WARNING] Empty block at position: (line 17,col 5)-(line 18,col 2)
Analyzing file: C:\AnalyzerTests\test\Main2.java
[WARNING] Static final variable 'cat' must be in uppercase.
[WARNING] Empty block at position: (line 28,col 20)-(line 28,col 21)
[WARNING] Empty block at position: (line 26,col 5)-(line 27,col 2)
[WARNING] Empty block at position: (line 29,col 9)-(line 29,col 10)
[WARNING] Empty block at position: (line 30,col 13)-(line 30,col 14)
[WARNING] Empty block at position: (line 31,col 25)-(line 31,col 26)
[WARNING] Empty block at position: (line 32,col 4)-(line 32,col 5)
[WARNING] Empty block at position: (line 33,col 23)-(line 33,col 24)
[WARNING] Variable name 'f_k' should be in lower camel case, detected at position: (line 36,col 10)-(line 36,col 22)
[WARNING] No package declaration.
Specify full path to file or directory you want to analyze.
