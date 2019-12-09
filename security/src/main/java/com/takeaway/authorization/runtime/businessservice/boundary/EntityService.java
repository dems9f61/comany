package com.takeaway.authorization.runtime.businessservice.boundary;

import com.takeaway.authorization.runtime.errorhandling.entity.ResourceNotFoundException;
import com.takeaway.authorization.runtime.rest.DataView;
import com.takeaway.authorization.runtime.rest.ResponsePage;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * User: StMinko Date: 17.09.2019 Time: 12:57
 *
 * <p>
 */
@Validated
@Transactional(propagation = Propagation.REQUIRED)
public interface EntityService<ENTITY, ID>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    @Transactional(propagation = Propagation.SUPPORTS)
    ResponsePage<ENTITY> findAll(Pageable pageable);

    /**
     * Find an Entity by its Id. Raise a ResourceNotFoundException if the entity cannot be found by its Id.
     *
     * @param id The Id to fetch an entity for
     * @return The entity for the provided id
     * @throws ResourceNotFoundException if the requested element cannot be found.
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    ENTITY findById(@NotNull ID id);

    /**
     * Find an Entity by its Id. Raise an Exception of the prodided Exception Type if the entity cannot be found by its Id.
     *
     * @param id The Id to fetch an entity for
     * @param exceptionClass The Exception type to raise if the requested Entity cannot be found by its id
     * @return The entity for the provided id
     * @throws RuntimeException of the specified type if the Element requested cannot be found. Fallback is a plain RuntimeException if the provided Exception Type cannot be
     *     instantiated. Instantiation ofthe target Exception happens by means of the Exception constructor taking only a Message string.
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    ENTITY findByIdOrElseThrow(@NotNull ID id, @NotNull Class<? extends RuntimeException> exceptionClass);

    ENTITY create(@NotNull ENTITY entity);

    ENTITY update(@NotNull ID id, @NotNull ENTITY entity, Class<? extends DataView> validationGroup);

    void delete(@NotNull ID id);

    void deleteAll();

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
