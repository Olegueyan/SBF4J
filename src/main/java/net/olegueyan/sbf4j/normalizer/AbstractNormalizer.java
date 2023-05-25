package net.olegueyan.sbf4j.normalizer;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

public abstract class AbstractNormalizer implements Serializable
{
    @Serial
    private static final long serialVersionUID = 231L;

    public abstract String reference();
    public abstract MDArray normalize(@NotNull MDArray mdArray);
}