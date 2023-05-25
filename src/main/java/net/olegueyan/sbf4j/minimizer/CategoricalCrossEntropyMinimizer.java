package net.olegueyan.sbf4j.minimizer;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

public class CategoricalCrossEntropyMinimizer extends AbstractMinimizer
{
    @Override
    public String reference()
    {
        return "categorical-cross-entropy";
    }

    @Override
    public double loss(@NotNull MDArray predicated, @NotNull MDArray truth)
    {
        return 0;
    }

    @Override
    public MDArray minimize(@NotNull MDArray predicated, @NotNull MDArray truth)
    {
        return null;
    }
}