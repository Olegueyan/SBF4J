package net.olegueyan.sbf4j.minimizer;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

public class MeanSquaredErrorMinimizer extends AbstractMinimizer
{
    @Override
    public String reference()
    {
        return "mean-squared-error";
    }

    @Override
    public double loss(@NotNull MDArray predicated, @NotNull MDArray truth)
    {
        MDArray loss = truth.sub(predicated).pow(2);
        return loss.mean();
    }

    @Override
    public MDArray minimize(@NotNull MDArray predicated, @NotNull MDArray truth)
    {
        return predicated.sub(truth).mul(2).div(predicated.getSize());
    }
}