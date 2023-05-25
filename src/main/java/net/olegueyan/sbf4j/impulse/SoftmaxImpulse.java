package net.olegueyan.sbf4j.impulse;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

public class SoftmaxImpulse extends AbstractImpulse
{
    @Override
    public String reference()
    {
        return "softmax";
    }

    @Override
    public void forward(@NotNull MDArray array)
    {
        array.fragment(1, mdArray ->
        {

        });
    }

    @Override
    public void backward(@NotNull MDArray array)
    {

    }
}