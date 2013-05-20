package tltsu.expertsystem.answeranalyzer;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FADEEV
 */
public class FormulaTree
{
    private static final Logger log = Logger.getLogger(FormulaTree.class);

    private String value;
    private ArrayList<FormulaTree> children;
    private FormulaTree parent ;

    public FormulaTree(String vertexValue)
    {
        value = vertexValue;
        parent = null;
        children = new ArrayList<FormulaTree>();
    }

    public void addVertex(String value)
    {
        FormulaTree child = new FormulaTree(value);
        this.children.add(child);
        child.parent = this;
    }

    public void addVertex(FormulaTree value)
    {
        value.parent = this;
        this.children.add(value);
    }

    String getValue()
    {
        return value;
    }

    public String toString ()
    {
        return getValue();
    }

    public List<FormulaTree> getChild()
    {
        return children;
    }

    public static void printTree (FormulaTree tree)
    {

        for (int i =0; i<tree.getLevel();i++)
            System.out.print("  ");
        System.out.print(tree);
        for (int i =0; i<tree.getLevel();i++)
            System.out.print("  ");
        System.out.println(tree.parent !=null ? "  ("+tree.parent+") " :"     (root)");

        for (FormulaTree child : tree.getChild())
            printTree(child);
    }
    //
    int getLevel ()
    {
        int result = 0 ;
        FormulaTree tree=this;

        while (tree.parent!=null)
        {
            tree= tree.parent;
            result++;
        }
        return result;
    }

    public FormulaTree right()
    {
        if(children.size()>=2)
            return children.get(1);
        else
            return null;
    }

    public FormulaTree left()
    {
        if(children.size()>=1)
            return children.get(0);
        else
            return null;
    }

    void swap(FormulaTree tree)
    {
        log.debug("Swap "+tree + " and "+this);
        //        FormulaTree buff = this;
        int a = tree.parent.children.indexOf(tree);
        int b = this.parent.children.indexOf(this);
        tree.parent.children.set(a, this);
        this.parent.children.set(b, tree);
        FormulaTree buffParent = tree.parent;
        tree.parent = this.parent ;
        this.parent = buffParent;
    }

    public FormulaTree getParent()
    {
        return parent;
    }
}
