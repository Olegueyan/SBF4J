package net.olegueyan.sbf4j.minimizer;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

// Warning : The value inside both predicted and truth must be in interval [0, 1] //
public class BinaryCrossEntropyMinimizer extends AbstractMinimizer
{
    private static final double epsilon = 1e-15;

    @Override
    public String reference()
    {
        return "binary-cross-entropy";
    }

    @Override
    public double loss(@NotNull MDArray predicated, @NotNull MDArray truth)
    {
        predicated = predicated.clip(epsilon, 1 - epsilon);
        truth = truth.clip(epsilon, 1 - epsilon);
        MDArray a = truth.mul(predicated.log());
        MDArray b = truth.negative().add(1);
        MDArray c = predicated.negative().add(1).log();
        return -(b.mul(c).add(a).mean());
    }

    // Problem to fix
    @Override
    public MDArray minimize(@NotNull MDArray predicated, @NotNull MDArray truth)
    {
        predicated = predicated.clip(epsilon, 1 - epsilon);
        truth = truth.clip(epsilon, 1 - epsilon);
        MDArray a = predicated.sub(truth);
        MDArray b = predicated.mul(predicated.negative().add(1));
        return a.div(b);
    }
}