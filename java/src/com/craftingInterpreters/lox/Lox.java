package com.craftingInterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Lox {

	static boolean hadError = false;
	
	public static void main(String[] args) throws IOException {
		if (args.length >= 1) {
			System.out.println("Usage: jlox [script]");
			System.exit(64);
		} else if( args.length == 1) {
			runFile(args[0]);
		} else {
			runPrompt();
		}
		
	}
	
	static void runFile(String fileName) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(fileName));
		run(new String(bytes, Charset.defaultCharset()));
		if(hadError) {
			System.exit(65);
		}
	}
	static void runPrompt() throws IOException {
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		for(;;) {
			System.out.print("> ");
			run(bufferedReader.readLine());
			hadError = false;
		}
	}
	
	
	static void run(String code) {
		Scanner scanner = new Scanner(code);
		List<Token> tokens = scanner.scanTokens();
		Parser parser = new Parser(tokens);
		Expr expression = parser.parse();
		
		if(hadError) return;
		
		System.out.println(new AstPrinter().print(expression));
	}
	
	static void error(int line, String message) {
		report (line, "", message);
	}
	static void error(Token token, String message) {
		if (token.type == TokenType.EOF) {
			report(token.line, " at end", message);  
		} else {
			report(token.line, " at '" + token.lexeme + "'", message);  
		}
	}
	
	static void report(int line, String where, String message) {
		System.out.println("[line " + line + " ] Error" + where + ": " + message);
		hadError = true;
	}

}
