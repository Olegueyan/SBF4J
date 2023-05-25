package net.olegueyan.sbf4j.layer;

import net.olegueyan.sbf4j.math.MDArray;
import net.olegueyan.sbf4j.optimizer.AbstractOptimizer;
import org.jetbrains.annotations.NotNull;

public interface Layer
{
    MDArray forward(@NotNull MDArray entry);
    MDArray backward(@NotNull MDArray gradient, AbstractOptimizer optimizer, double learningRate);
}