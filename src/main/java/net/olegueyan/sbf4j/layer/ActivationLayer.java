package net.olegueyan.sbf4j.layer;

import net.olegueyan.sbf4j.impulse.AbstractImpulse;
import net.olegueyan.sbf4j.math.MDArray;
import net.olegueyan.sbf4j.optimizer.AbstractOptimizer;
import org.jetbrains.annotations.NotNull;

public class ActivationLayer implements Layer
{
    private final AbstractImpulse impulse;
    private MDArray input;

    public ActivationLayer(AbstractImpulse impulse)
    {
        this.impulse = impulse;
    }

    @Override
    public MDArray forward(@NotNull MDArray entry)
    {
        impulse.forward(entry);
        this.input = entry;
        return this.input;
    }

    @Override
    public MDArray backward(@NotNull MDArray gradient, AbstractOptimizer optimizer, double learningRate)
    {
        this.impulse.backward(this.input);
        return this.input.mul(gradient);
    }
}