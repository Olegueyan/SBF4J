package net.olegueyan.sbf4j;

import net.olegueyan.sbf4j.math.MDArray;

public class Test
{
    public static void main(String[] args)
    {
        MDArray mdArray = MDArray.random(0, 1, 4, 3);
        System.out.println(mdArray);
    }
}