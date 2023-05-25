package net.olegueyan.sbf4j.normalizer;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

public class MinMaxNormalizer extends AbstractNormalizer
{
    @Override
    public String reference()
    {
        return "min-max-n";
    }

    @Override
    public MDArray normalize(@NotNull MDArray mdArray)
    {
        return null;
    }
}