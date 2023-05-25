package net.olegueyan.sbf4j.minimizer;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

public abstract class AbstractMinimizer implements Serializable
{
    @Serial
    private static final long serialVersionUID = 349L;

    public abstract String reference();
    public abstract double loss(@NotNull MDArray predicated, @NotNull MDArray truth);
    public abstract MDArray minimize(@NotNull MDArray predicated, @NotNull MDArray truth);
}