package com.takeaway.employeeservice.employee.control;

import com.takeaway.employeeservice.employee.entity.Employee;
import com.takeaway.employeeservice.runtime.errorhandling.entity.ResourceNotFoundException;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * User: StMinko Date: 19.03.2019 Time: 09:52
 *
 * <p>
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface EmployeeServiceCapable
{
    // =========================== Class Variables ===========================

    Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceCapable.class);

    // ==============================  Methods  ==============================

    EmployeeRepository getRepository();

    Employee create(@NonNull EmployeeParameter creationParameter);

    void update(@NonNull UUID id, @NonNull EmployeeParameter updateParameter);

    @Transactional(propagation = Propagation.SUPPORTS)
    default Employee findById(@NonNull UUID id)
    {
        LOGGER.info("Finding employee by name [{}", id);
        return findByIdOrElseThrow(id, ResourceNotFoundException.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    default Employee findByIdOrElseThrow(@NotNull UUID id, @NotNull Class<? extends RuntimeException> exceptionClass)
    {
        LOGGER.info("Finding employee by name [{}] or throw [{}]", id, exceptionClass.getSimpleName());
        return getRepository()
                .findById(id)
                .orElseThrow(() -> {
                            String message = String.format("Could not find employee for Id [%s]!", id);
                            try
                            {
                                return exceptionClass.getConstructor(String.class).newInstance(message);
                            }
                            catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e)
                            {
                                LOGGER.warn("Could not instantiate Exception of Type [{}] on {}.findByIdOrElseThrow ( {} ,  {}.class )",
                                        exceptionClass.getName(),
                                        getClass().getSimpleName(),
                                        id,
                                        exceptionClass.getSimpleName());
                                return new RuntimeException(message);
                            }
                        });
    }

    void deleteById(@NonNull UUID id);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
