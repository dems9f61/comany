package com.takeaway.employeeservice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 16:43
 * <p/>
 */
public abstract class AbstractTestFactory<TYPE, BUILDER_TYPE extends AbstractTestFactory.Builder<TYPE>>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public abstract BUILDER_TYPE builder();

    public TYPE createDefault()
    {
        return builder().create();
    }

    public List<TYPE> createManyDefault(int count)
    {
        return manyBuilders(count).map(Builder::create)
                                  .collect(Collectors.toList());
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================

    private Stream<Builder<TYPE>> manyBuilders(int count)
    {
        return IntStream.range(0, count)
                        .mapToObj(i -> builder());
    }

    // ============================  Inner Classes  ==========================

    public interface Builder<TYPE>
    {
        TYPE create();
    }

    // ============================  End of class  ===========================
}
