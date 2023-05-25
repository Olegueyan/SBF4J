package net.olegueyan.sbf4j.data;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

public interface Dataset
{
    @NotNull MDArray trainEntry();
    @NotNull MDArray trainTruth();
    @NotNull MDArray testEntry();
    @NotNull MDArray testTruth();
    int trainSize();
    int testSize();
}