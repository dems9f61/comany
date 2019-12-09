package com.takeaway.authorization.runtime.businessservice.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: StMinko Date: 14.10.2019 Time: 14:59
 *
 * <p>
 */
@Slf4j
public final class BeanTool
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    private BeanTool()
    {
        throw new AssertionError("This is not meant to be instantiated !!!");
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================

    public static void copyNonNullProperties(Object source, Object target)
    {
        copyNonNullProperties(source, target, (String[]) null);
    }

    public static void copyNonNullProperties(Object source, Object target, String... ignoreProperties)
    {
        if (source == null)
        {
            LOGGER.debug("Source object is null");
            return;
        }
        if (target == null)
        {
            LOGGER.debug("Target object is null");
            return;
        }
        Set<String> ignoreAndNullValueFieldNames = new HashSet<>();
        if (ignoreProperties != null)
        {
            ignoreAndNullValueFieldNames.addAll(Arrays.asList(ignoreProperties));
        }
        ignoreAndNullValueFieldNames = getAllIgnoreAndNullFields(source, ignoreAndNullValueFieldNames, source.getClass());
        BeanUtils.copyProperties(source, target, ignoreAndNullValueFieldNames.toArray(new String[ignoreAndNullValueFieldNames.size()]));
    }

    // ===========================  private  Methods  ========================

    private static Set<String> getAllIgnoreAndNullFields(Object source, Set<String> fields, Class<?> type)
    {
        for (Field field : type.getDeclaredFields())
        {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            try
            {
                if (field.get(source) == null)
                {
                    fields.add(field.getName());
                }
            }
            catch (IllegalAccessException caught)
            {
                LOGGER.debug("IllegalAccessException when reading value of Field: {} on Object: {}. Error was: {}",
                             field.getName(),
                             source,
                             caught.getMessage(),
                             caught);
            }
            finally
            {
                field.setAccessible(accessible);
            }
        }
        if (type.getSuperclass() != null)
        {
            fields.addAll(getAllIgnoreAndNullFields(source, fields, type.getSuperclass()));
        }
        return fields;
    }

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
