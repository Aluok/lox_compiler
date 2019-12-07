package com.craftingInterpreters.lox;

import com.craftingInterpreters.lox.Expr.Binary;
import com.craftingInterpreters.lox.Expr.Grouping;
import com.craftingInterpreters.lox.Expr.Literal;
import com.craftingInterpreters.lox.Expr.Unary;
import com.craftingInterpreters.lox.Expr.Visitor;
import com.craftingInterpreters.lox.TokenType.*;

public class Interpreter implements Visitor<Object> {

	public void interpret(Expr expression) {
		try {
			Object value = evaluate(expression);
			System.out.println(stringify(value));
		} catch (RuntimeError err) {
			Lox.runtimeError(err);
		}
	}
	
	@Override
	public Object visitBinaryExpr(Binary expr) {
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);
		
		switch(expr.operator.type) {
		case MINUS:
			checkNumberOperands(expr.operator, left, right);
			return (double)left - (double)right;
		case PLUS:
			if(left instanceof Double && right instanceof Double) {
				return (double)left + (double)right;
			}
			if(left instanceof String && right instanceof String) {
				return (String)left + (String)right;
			}

			throw new RuntimeError(expr.operator, "Operands must be two numbers or two Strings");
		case STAR:
			checkNumberOperands(expr.operator, left, right);
			return (double)left * (double)right;
		case SLASH:
			checkNumberOperands(expr.operator, left, right);
			return (double)left / (double)right;
		case GREATER:
			checkNumberOperands(expr.operator, left, right);
			return (double)left > (double)right;
		case GREATER_EQUAL:
			checkNumberOperands(expr.operator, left, right);
			return (double)left >= (double)right;
		case LESS:
			checkNumberOperands(expr.operator, left, right);
			return (double)left < (double)right;
		case LESS_EQUAL:
			checkNumberOperands(expr.operator, left, right);
			return (double)left <= (double)right;
		case EQUAL_EQUAL:
			return isEqual(left, right);
		case BANG_EQUAL:
			return !isEqual(left, right);
		}
		return null;
	}

	@Override
	public Object visitGroupingExpr(Grouping expr) {
		// TODO Auto-generated method stub
		return evaluate(expr.expr);
	}

	@Override
	public Object visitLiteralExpr(Literal expr) {
		return expr.value;
	}

	@Override
	public Object visitUnaryExpr(Unary expr) {
		Object right = evaluate(expr.right);
		
		switch(expr.operator.type) {
		case MINUS:
			checkNumberOperand(expr.operator, right);
			return -(double)right;
		case BANG:
			return !isTruthy(right);
		}
		//Unreachable
		return null;
	}
	
	private Object evaluate(Expr expr) {
		return expr.accept(this);
	}
	
	private boolean isTruthy(Object obj) {
		if(obj == null) return false;
		if(obj instanceof Boolean) return (Boolean)obj;
		return true;
	}
	
	private boolean isEqual(Object obj1, Object obj2) {
		if(obj1 == null && obj2 == null) return true;
		if(obj1 == null ) return false;
		
		return obj1.equals(obj2);//We know it not to be null, and java follow the same rules as Lox.
	}

	private void checkNumberOperand(Token operator, Object operand) {
		if(operand instanceof Double) return;
		throw new RuntimeError(operator, "Operand must be a number");
	}
	private void checkNumberOperands(Token operator, Object left, Object right) {
		if(left instanceof Double && right instanceof Double) return;
		throw new RuntimeError(operator, "Operands must be numbers");
	}
	
	private String stringify(Object value) {
		if(value == null) return "nil";
		if(value instanceof Double) {
			String text = value.toString();
			if(text.endsWith(".0")) {
				text = text.substring(0, text.length() - 2);
			}
			return text;
		}
		return value.toString();
	}
}
