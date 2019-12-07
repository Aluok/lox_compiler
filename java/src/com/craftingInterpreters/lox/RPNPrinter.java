package com.craftingInterpreters.lox;

import com.craftingInterpreters.lox.Expr.Binary;
import com.craftingInterpreters.lox.Expr.Grouping;
import com.craftingInterpreters.lox.Expr.Literal;
import com.craftingInterpreters.lox.Expr.Unary;

public class RPNPrinter implements Expr.Visitor<String>{

	public String print(Expr expr) {
		return expr.accept(this);
	}

	@Override
	public String visitBinaryExpr(Binary expr) {
		return print(expr.left) + " " + print(expr.right) + " " + expr.operator.lexeme;
	}

	@Override
	public String visitGroupingExpr(Grouping expr) {
		return print(expr.expr);
	}

	@Override
	public String visitLiteralExpr(Literal expr) {
		if(expr.value == null) return "nil";
		return expr.value.toString();
	}

	@Override
	public String visitUnaryExpr(Unary expr) {
		return print(expr.right) + " " + expr.operator.lexeme;
	}
	

	
	public static void main(String[] args) {
		Expr expr = new Expr.Binary(
				new Binary(
						new Literal(1),
						new Token(TokenType.PLUS, "+", null, 1),
						new Literal(2)
				),
				new Token(TokenType.STAR, "*", null, 1),
				new Binary(
						new Literal(4),
						new Token(TokenType.PLUS, "-", null, 1),
						new Literal(3)
						)
				);
		System.out.println(new RPNPrinter().print(expr));
	}

}
