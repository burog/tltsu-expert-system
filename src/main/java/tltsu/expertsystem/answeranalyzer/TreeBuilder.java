package tltsu.expertsystem.answeranalyzer;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.Collections;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author FADEEV
 */
public class TreeBuilder
{
    private static final Logger log = Logger.getLogger(TreeBuilder.class);
    private static final String[] FUNCTIONS = {"abs", "acos", "arg", "asin", "atan", "conj", "cos", "cosh", "exp",
            "imag", "log", "neg", "pow", "real", "sin", "sinh", "sqrt", "tan", "tanh"};
    private static final String OPERATORS = "+-*/^";
    private static final String SEPARATOR = ",";

    protected static Stack<String> parse(String expression) throws ParseException
    {
        Stack<String> stack = new Stack<String>();
        Stack<String> result = new Stack<String>();

        expression = expression.replace(" ", "").replace("°", "*" + Double.toString(Math.PI) + "/180").replace("(-",
                "(0-").replace(",-", ",0-").replace("(+", "(0+").replace(",+", ",0+");
        if (expression.charAt(0) == '-' || expression.charAt(0) == '+')
        {
            expression = "0" + expression;
        }

        StringTokenizer stringTokenizer = new StringTokenizer(expression, OPERATORS + SEPARATOR + "()", true);

        while (stringTokenizer.hasMoreTokens())
        {
            String token = stringTokenizer.nextToken();

            if (isNumber(token))
            {
                result.push(token);
            }
            else if (isFunction(token))
            {
                stack.push(token);
            }
            else if (isSeparator(token))
            {
                while (!isOpenBracket(stack.lastElement()))
                {
                    if (stack.size() == 1)
                        throw new ParseException("Open brace or separator are missed", 0);
                    result.push(stack.pop());
                }
            }
            else if (isOperator(token))
            {
                while (!stack.empty() && isOperator(stack.lastElement()) && getPrecedence(token) <= getPrecedence(
                        stack.lastElement()))
                {
                    result.push(stack.pop());
                }
                stack.push(token);
            }
            else if (isOpenBracket(token))
            {
                stack.push(token);
            }
            else if (isCloseBracket(token))
            {
                while (!stack.empty() && !isOpenBracket(stack.lastElement()))
                {
                    result.push(stack.pop());
                }
                if (stack.empty())
                    throw new ParseException("Error parsing, input expression missed bracket", 0);
                stack.pop();
                if (!stack.empty() && isFunction(stack.lastElement()))
                {
                    result.push(stack.pop());
                }
            }
            else
            {
                log.error("Unrecognized token");
                throw new ParseException("Unrecognized token", 0);
            }
        }
        while (!stack.empty())
        {
            String operation = stack.pop();
            if (isOpenBracket(operation))
            {
                log.error("Error parsing, input expression contains not closed bracket");
                throw new ParseException("Error parsing, input expression contains not closed bracket", 0);
            }
            else
                result.push(operation);
        }

        return result;
    }

    public static FormulaTree build(String expr) throws Exception
    {
        Stack<String> infix;
        try
        {
            infix = parse(expr);
        }
        catch (ParseException e)
        {
            log.error("Error while parsing");
            throw new Exception("Parsing Exception. \nError occured while parsing \"" + expr + "\"", e);
        }
        Collections.reverse(infix);

        Stack<FormulaTree> stack = new Stack<FormulaTree>();
        try
        {
            while (!infix.empty())
            {
                String token = infix.pop();
                if (isNumber(token))
                {
                    stack.push(new FormulaTree(token));
                }
                else if (isFunction(token) || isOperator(token))
                {
                    int argumentCount = argumentCount(token);
                    if (stack.size() < argumentCount)
                    {
                        log.error("Build Exception. Too few arguments for " + token);
                        throw new Exception("Build Exception. Too few arguments for " + token);
                    }

                    FormulaTree operation = new FormulaTree(token);
                    for (int i = 0; i < argumentCount; i++)
                        operation.addVertex(stack.pop());
                    stack.push(operation);
                }

            }
            if (stack.size() == 1)
            {
                return stack.pop();
            }
            else
            {
                log.error("Build Exception. Wrong input expression " + expr + "\"");
                throw new Exception("Build Exception. Wrong input expression \"" + expr + "\"");
            }

        }
        catch (Exception e)
        {
            log.error("Erorr while parsing");
            throw new Exception("Build Exception. \"" + expr + "\", bad parsing or input expression", e);
        }
    }

    protected static boolean isNumber(String token)
    {
        if ("x".equalsIgnoreCase(token))
            return true;
        if ("y".equalsIgnoreCase(token))
            return true;
        try
        {
            Double.parseDouble(token);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    protected static boolean isFunction(String token)
    {
        for (String item : FUNCTIONS)
        {
            if (item.equals(token))
            {
                return true;
            }
        }
        return false;
    }

    protected static boolean isSeparator(String token)
    {
        return SEPARATOR.equals(token);
    }

    protected static boolean isOpenBracket(String token)
    {
        return "(".equals(token);
    }

    protected static boolean isCloseBracket(String token)
    {
        return ")".equals(token);
    }

    static boolean isOperator(String token)
    {
        return OPERATORS.contains(token);
    }

    static byte getPrecedence(String token)
    {
        if ("+".equals(token) || "-".equals(token))
            return 1;
        else if ("^".equals(token))
            return 3;
        else
            return 2;
    }

    static byte argumentCount(String operator) throws Exception
    {
        if (isOperator(operator))
            return 2;
        else if ("!".equals(operator))
            return 1;
        else if ("pow".equals(operator))
            return 2;
        else if (isFunction(operator))
            return 1;
        log.error("no such operator \"" + operator + "\"");
        throw new Exception("no such operator \"" + operator + "\"");
    }

}
