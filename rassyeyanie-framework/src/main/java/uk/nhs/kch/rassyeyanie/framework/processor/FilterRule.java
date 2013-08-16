package uk.nhs.kch.rassyeyanie.framework.processor;

import java.util.List;

public class FilterRule
{
    public static boolean isRejected(String value,
                                     List<String> list,
                                     boolean verifyMode)
    {
        return list.size() > 0 && verifyMode ^ list.contains(value);
    }
    
}
