package tltsu.expertsystem.answeranalyzer;

import org.apache.log4j.Logger;

import java.util.Stack;

/**
 * Class allows determine error in user's formula.
 * @author FADEEV
 */
public class AnalyzeAnswer
{
    private static final Logger log = Logger.getLogger(AnalyzeAnswer.class);

    /**
     * This method build tree for two formulas and compare. Returned result with errors.
     * @param etalon correct formula
     * @param users formula that will be inspected
     * @return assessment and error is occurred errors
     * @throws Exception throws if error occurred in etalon formula
     */
    public static TestResult analyzeFormula(String etalon, String users) throws Exception
    {
        boolean teacherError = true;
        FormulaTree etalonTree;
        FormulaTree userTree;
        try
        {
            etalonTree = TreeBuilder.build(etalon);
            teacherError = false;
            userTree = TreeBuilder.build(users);
        }
        catch (Exception e)
        {
            log.error("Error while parsing user/etalon formulas", e);
            if (teacherError)
                throw e;
            else
                return new TestResult(TestResult.BAD, e.getMessage());
        }

        FormulaTree mismatch = compareFormulas(etalonTree, userTree);

        return createAnswerByMismatch(mismatch);
    }

    /**
     * Compare trees by preorder traversal.
     * @param etalon etalon tree
     * @param user user tree
     * @return node that does not match
     */
    static FormulaTree compareFormulas(FormulaTree etalon, FormulaTree user)
    {
        Stack<FormulaTree> etalonNodes = new Stack<FormulaTree>();
        Stack<FormulaTree> userNodes = new Stack<FormulaTree>();
        etalonNodes.push(etalon);
        userNodes.push(user);

        FormulaTree currentEtalon;
        FormulaTree currentUser;

        while (!etalonNodes.isEmpty())
        {
            currentEtalon = etalonNodes.pop();
            currentUser = userNodes.pop();

            if (!currentEtalon.getValue().equals(currentUser.getValue()))
                return currentUser;

            FormulaTree rightEtalon = currentEtalon.right();
            FormulaTree rightUser = currentUser.right();
            FormulaTree leftEtalon = currentEtalon.left();
            FormulaTree leftUser = currentUser.left();

            if (rightEtalon == null && rightUser != null || rightEtalon != null && rightUser == null)
                return rightUser;
            if (leftEtalon == null && leftUser != null || leftEtalon != null && leftUser == null)
                return rightUser;
            if (leftEtalon == null && leftUser == null)
                continue;
            if (rightEtalon == null && rightUser == null)
                continue;

            if (!rightEtalon.getValue().equals(rightUser.getValue()))
            {
                if (isAssociated(currentEtalon))
                {
                    if (rightEtalon.getValue().equals(leftUser.getValue()))
                    {
                        swap(rightUser, leftUser);
                        rightUser = currentUser.right();
                        leftUser = currentUser.left();
                    }
                    else
                    {
                        return rightUser;
                    }
                }
                else
                    return rightUser;
            }
            etalonNodes.push(rightEtalon);
            userNodes.push(rightUser);

            if (!leftEtalon.getValue().equals(leftUser.getValue()))
            {
                if (isAssociated(currentEtalon) && isAssociated(rightEtalon))
                {
                    Stack<FormulaTree> subRight = new Stack<FormulaTree>();
                    subRight.push(rightUser);
                    FormulaTree search;
                    while (!leftEtalon.getValue().equals(leftUser.getValue()) && !subRight.empty())
                    {
                        search = subRight.pop();
                        if (search.left().getValue().equals(leftEtalon.getValue()))
                        {
                            swap(search.left(), leftUser);
                            leftUser = currentUser.left();
                        }
                        else if (search.right().getValue().equals(leftEtalon.getValue()))
                        {
                            swap(search.right(), leftUser);
                            leftUser = currentUser.left();
                        }
                        else
                        {
                            if (isAssociated(search.right()))
                                subRight.push(search.right());
                            if (isAssociated(search.left()))
                                subRight.push(search.left());
                            if (subRight.isEmpty())
                                return leftUser;
                        }
                    }

                }
                else
                    return leftUser;
            }
            userNodes.push(leftUser);
            etalonNodes.push(leftEtalon);

        }
        if (!userNodes.empty())
            throw new RuntimeException("Etalon stack is empty but, users - not");
        return null;
    }

    /**
     * This method for check whether the operation is associative.
     * @param tree operation
     * @return true is node (operation) associative, false otherwise.
     */
    static boolean isAssociated(FormulaTree tree)
    {
        return "*".equals(tree.getValue()) || "+".equals(tree.getValue());
    }

    /**
     * Allows swap 2 nodes.
     * @param a swapped node
     * @param b swapped node
     */
    static void swap(FormulaTree a, FormulaTree b)
    {
        a.swap(b);
    }

    /**
     * This method for check whether the node is number.
     * @param operator node
     * @return true is node (operation) associative, false otherwise.
     */
    static boolean isNumber(String operator)
    {
        return TreeBuilder.isNumber(operator);
    }

    /**
     * Method create result by node. If node is null, returned excellent result.
     * @param node node that is mismatch or null
     * @return result represented as {@link TestResult}
     */
    static TestResult createAnswerByMismatch(FormulaTree node)
    {
        String answer;
        if (node == null)
            return new TestResult();
        else if (TreeBuilder.isFunction(node.getValue()))
            answer = "Wrong function '" + node + "'";
        else if (TreeBuilder.isOperator(node.getValue()))
            answer = "Wrong operator '" + node + "'";
        else if (TreeBuilder.isNumber(node.getValue()))
            answer = "Wrong argument '" + node + "' in operator " + node.getParent();
        else
            answer = "mistake are not recognized";

        return new TestResult(TestResult.BAD, "You are do mistake: " + answer);
    }
}
