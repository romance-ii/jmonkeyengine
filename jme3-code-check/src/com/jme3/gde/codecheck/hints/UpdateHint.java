package com.jme3.gde.codecheck.hints;

import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.TreePath;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.modules.java.hints.spi.AbstractHint;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.Fix;

public class UpdateHint extends AbstractHint {

    //This hint does not enable the IDE to fix the problem:
    private static final List<Fix> NO_FIXES = Collections.<Fix>emptyList();
    //This hint applies to method invocations:
    private static final Set<Tree.Kind> TREE_KINDS =
            EnumSet.<Tree.Kind>of(Tree.Kind.METHOD_INVOCATION);

    public UpdateHint() {
        super(true, true, AbstractHint.HintSeverity.WARNING);
    }

    //Specify the kind of code that the hint applies to, in this case,
    //the hint applies to method invocations:
    @Override
    public Set<Kind> getTreeKinds() {
        return TREE_KINDS;
    }

    @Override
    public List<ErrorDescription> run(CompilationInfo info, TreePath treePath) {

        Tree t = treePath.getLeaf();

        Element el = info.getTrees().getElement(treePath);
        String name = el.getSimpleName().toString();

        //This is where it all happens: if the method invocation is 'showMessageDialog',
        //then the hint infrastructure kicks into action:
        if (name.equals("updateGeometricState") || name.equals("updateLogicalState") || name.equals("updateModelBound")) {
            return Collections.<ErrorDescription>singletonList(
                    ErrorDescriptionFactory.createErrorDescription(
                    getSeverity().toEditorSeverity(),
                    getDisplayName(),
                    NO_FIXES,
                    info.getFileObject(),
                    (int) info.getTrees().getSourcePositions().getStartPosition(info.getCompilationUnit(), t),
                    (int) info.getTrees().getSourcePositions().getEndPosition(info.getCompilationUnit(), t)));

        }

        return null;

    }

    //This is called if/when the hint processing is cancelled:
    @Override
    public void cancel() {
    }

    //Message that the user sees in the left sidebar:
    @Override
    public String getDisplayName() {
        return "Updating is not needed in jME3, if you need to call this check your update order!";
    }

    //Name of the hint in the Options window:
    @Override
    public String getId() {
        return "Update States / Bound";
    }

    //Description of the hint in the Options window:
    @Override
    public String getDescription() {
        return "Checks for calls to updateGeometricState(), updateLogicalState() and updateModelBound().";
    }
}
