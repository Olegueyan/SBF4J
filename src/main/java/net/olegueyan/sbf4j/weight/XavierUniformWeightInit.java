package net.olegueyan.sbf4j.weight;

import net.olegueyan.sbf4j.math.MDArray;

public class XavierUniformWeightInit extends AbstractWeightInit
{
    @Override
    public String reference()
    {
        return "xavier-u";
    }

    @Override
    public MDArray initiate(int fanIn, int fanOut)
    {
        double a = - Math.sqrt(6.0 / (fanIn + fanOut));
        double b = Math.sqrt(6.0 / (fanIn + fanOut));
        return MDArray.random(a, b, fanIn, fanOut);
    }
}