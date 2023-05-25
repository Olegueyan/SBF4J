package net.olegueyan.sbf4j.weight;

import net.olegueyan.sbf4j.math.MDArray;

public class XavierNormalWeightInit extends AbstractWeightInit
{
    @Override
    public String reference()
    {
        return "xavier-n";
    }

    @Override
    public MDArray initiate(int fanIn, int fanOut)
    {
        double a = 0;
        double b = 2.0 / (fanIn + fanOut);
        return MDArray.random(a, b, fanIn, fanOut);
    }
}