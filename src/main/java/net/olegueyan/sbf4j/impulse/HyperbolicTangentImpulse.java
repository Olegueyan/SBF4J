package net.olegueyan.sbf4j.impulse;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

public class HyperbolicTangentImpulse extends AbstractImpulse
{
    @Override
    public String reference()
    {
        return "tanh";
    }

    @Override
    public void forward(@NotNull MDArray array)
    {
        array.map(Math::tanh);
    }

    @Override
    public void backward(@NotNull MDArray array)
    {
        array.map(value -> 1 - Math.pow(value, 2));
    }
}