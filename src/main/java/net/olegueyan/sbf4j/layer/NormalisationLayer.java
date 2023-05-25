package net.olegueyan.sbf4j.layer;

import net.olegueyan.sbf4j.math.MDArray;
import net.olegueyan.sbf4j.normalizer.AbstractNormalizer;
import net.olegueyan.sbf4j.optimizer.AbstractOptimizer;
import org.jetbrains.annotations.NotNull;

public class NormalisationLayer implements Layer
{
    private final AbstractNormalizer normalizer;

    public NormalisationLayer(AbstractNormalizer normalizer)
    {
        this.normalizer = normalizer;
    }

    @Override
    public MDArray forward(@NotNull MDArray entry)
    {
        return null;
    }

    @Override
    public MDArray backward(@NotNull MDArray gradient, AbstractOptimizer optimizer, double learningRate)
    {
        return gradient;
    }
}