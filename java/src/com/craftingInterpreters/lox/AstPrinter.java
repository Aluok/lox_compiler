package com.craftingInterpreters.lox;

import com.craftingInterpreters.lox.Expr.Binary;
import com.craftingInterpreters.lox.Expr.Grouping;
import com.craftingInterpreters.lox.Expr.Literal;
import com.craftingInterpreters.lox.Expr.Unary;

public class AstPrinter implements Expr.Visitor<String>{

	String print(Expr expr) {
		return expr.accept(this);
	}

	@Override
	public String visitBinaryExpr(Binary expr) {
		return parenthesize(expr.operator.lexeme, expr.left, expr.right);
	}

	@Override
	public String visitGroupingExpr(Grouping expr) {
		return parenthesize("group", expr.expr);
	}

	@Override
	public String visitLiteralExpr(Literal expr) {
		if (expr.value == null) return "nil";
		return expr.value.toString();
	}

	@Override
	public String visitUnaryExpr(Unary expr) {
		return parenthesize(expr.operator.lexeme, expr.right);
	}
	
	private String parenthesize(String name, Expr... exprs) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("(").append(name);
		for (Expr expr: exprs) {
			builder.append(" ")
				.append(expr.accept(this));
		}
		builder.append(")");
		
		return builder.toString();
	}
	
	public static void main(String[] args) {
		String test = "expr → expr ( \"(\" ( expr ( \",\" expr )* )? \")\")*";
		String result = "expr → IDENTIIER" +
				"expr → NUMBER" +
				"expr -> expr Point_expr" +
				"Point_expr -> \".\" IDENTIFIER " +
				"Point_expr -> \".\" IDENTIFIER Point_expr" +
				"paren_expr -> \"(\" expr  \",\" PARAMETER"
				;
		Expr expr = new Expr.Binary(
				new Unary(
						new Token(TokenType.MINUS, "-", null, 1),
						new Literal(123)
				),
				new Token(TokenType.STAR, "*", null, 1),
				new Grouping(
					new Literal(146)
				)
				);
		System.out.println(new AstPrinter().print(expr));
	}
}