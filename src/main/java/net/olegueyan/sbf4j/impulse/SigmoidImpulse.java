package net.olegueyan.sbf4j.impulse;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

public class SigmoidImpulse extends AbstractImpulse
{
    @Override
    public String reference()
    {
        return "sigmoid";
    }

    @Override
    public void forward(@NotNull MDArray array)
    {
        array.map(value -> 1 / (1 + Math.exp(-value)));
    }

    @Override
    public void backward(@NotNull MDArray array)
    {
        array.map(value -> value * (1 - value));
    }
}