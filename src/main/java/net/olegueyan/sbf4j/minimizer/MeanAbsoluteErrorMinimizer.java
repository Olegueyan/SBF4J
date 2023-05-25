package net.olegueyan.sbf4j.minimizer;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

public class MeanAbsoluteErrorMinimizer extends AbstractMinimizer
{
    @Override
    public String reference()
    {
        return "mean-absolute-error";
    }

    @Override
    public double loss(@NotNull MDArray predicated, @NotNull MDArray truth)
    {
        MDArray loss = truth.sub(predicated).absolute();
        return loss.mean();
    }

    @Override
    public MDArray minimize(@NotNull MDArray predicated, @NotNull MDArray truth)
    {
        MDArray minimized = predicated.sub(truth);
        minimized.map(Math::signum);
        return minimized.div(predicated.getSize());
    }
}