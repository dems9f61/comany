package com.takeaway.authorization.runtime.businessservice.boundary;

import com.takeaway.authorization.runtime.businessservice.control.BeanTool;
import com.takeaway.authorization.runtime.errorhandling.entity.ResourceNotFoundException;
import com.takeaway.authorization.runtime.persistence.AbstractEntity;
import com.takeaway.authorization.runtime.persistence.JpaSpecificationRepository;
import com.takeaway.authorization.runtime.rest.DataView;
import com.takeaway.authorization.runtime.rest.ResponsePage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

/**
 * User: StMinko Date: 14.10.2019 Time: 13:57
 *
 * <p>
 */
@Validated
@Slf4j
public abstract class AbstractDefaultEntityService<REPOSITORY extends JpaSpecificationRepository<ENTITY, ID>, ENTITY extends AbstractEntity<ID>, ID extends Serializable>
        implements EntityService<ENTITY, ID>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Getter
    private final REPOSITORY repository;

    private final Validator validator;

    // ============================  Constructors  ===========================

    public AbstractDefaultEntityService(REPOSITORY repository, Validator validator)
    {
        this.repository = repository;
        this.validator = validator;
    }

    // ===========================  public  Methods  =========================

    @Override
    public ResponsePage<ENTITY> findAll(Pageable pageable)
    {
        LOGGER.info("{}.findAll ( {} )", getClass().getSimpleName(), pageable);
        Page<ENTITY> all = repository.findAll(pageable);
        return new ResponsePage<>(all.getContent(), pageable, all.getTotalElements());
    }

    @Override
    public ENTITY findById(@NotNull ID id)
    {
        LOGGER.info("{}.findOne ( {} )",
                    this.getClass()
                        .getSimpleName(),
                    id);
        return findByIdOrElseThrow(id, ResourceNotFoundException.class);
    }

    @Override
    public ENTITY findByIdOrElseThrow(@NotNull ID id, @NotNull Class<? extends RuntimeException> exceptionClass)
    {
        LOGGER.info("{}.findByIdOrElseThrow ( {} , {} )",
                    this.getClass()
                        .getSimpleName(),
                    id,
                    exceptionClass);
        return repository.findById(id)
                         .orElseThrow(() -> {
                             String message = String.format("Could not find [%s] for Id [%s]!", getEntityClass().getSimpleName(), id);
                             try
                             {
                                 return exceptionClass.getConstructor(String.class)
                                                      .newInstance(message);
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

    @Override
    public ENTITY create(@Validated(DataView.POST.class) @NotNull ENTITY create)
    {
        LOGGER.info("{}.create ( {} )",
                    this.getClass()
                        .getSimpleName(),
                    create);
        ENTITY beforeCreate = onBeforeCreate(create);
        validateEntity(beforeCreate, DataView.POST.class);
        ENTITY created = doCreate(beforeCreate);
        return onAfterCreate(created);
    }

    @Override
    public ENTITY update(@NotNull ID id, @NotNull ENTITY update, Class<? extends DataView> validationGroup)
    {
        LOGGER.info("{}.update ( {}, {} )",
                    this.getClass()
                        .getSimpleName(),
                    id,
                    update);
        validateEntity(update, validationGroup);
        ENTITY loaded = findById(id);
        BeanTool.copyNonNullProperties(onBeforeUpdate(loaded, update), loaded, "id", "createdAt", "createdBy", "lastUpdatedAt", "lastUpdatedBy");
        return onAfterUpdate(doUpdate(loaded));
    }

    @Override
    public void delete(ID id)
    {
        LOGGER.info("{}.delete ( {} )",
                    this.getClass()
                        .getSimpleName(),
                    id);
        ENTITY loaded = findById(id);
        repository.delete(onBeforeDelete(id, loaded));
        onAfterDelete(id, loaded);
    }

    @Override
    public void deleteAll()
    {
        LOGGER.info("{}.deleteAll ()", getClass().getSimpleName());
        repository.deleteAll();
    }

    // =================  protected/package local  Methods ===================

    /**
     * Retrieve the classes Instance Generics information for the Generic Type "ENTITY" of this class
     *
     * @return The {@link Class} of type ? extends ENTITY as compiled for this class or its derivations
     */
    protected final Class<? extends ENTITY> getEntityClass()
    {
        return (Class<? extends ENTITY>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * Extension Point for before create actions. Modify ENTITY typed Entity to create. Default implementation returns parameter create unmodified.
     *
     * @param create The ENTITY typed Entity to create
     * @return The before create actions modified Entity to create. Default implementation returns the input parameter create.
     */
    protected ENTITY onBeforeCreate(ENTITY create)
    {
        return create;
    }

    protected ENTITY doCreate(ENTITY beforeCreate)
    {
        return repository.save(beforeCreate);
    }

    /**
     * Extension Point for after create actions. Modify ENTITY typed Entity that was created. Default implementation returns parameter created unmodified.
     *
     * @param created The ENTITY typed Entity to create
     * @return The after create actions modified Entity that was created. Default implementation returns the input parameter created.
     */
    protected ENTITY onAfterCreate(ENTITY created)
    {
        return created;
    }

    /**
     * Extension Point for before update actions. Modify ENTITY typed Entity to update. Default implementation returns parameter update unmodified.
     *
     * @param existing The ENTITY typed Entity that exists
     * @param update   The ENTITY typed Entity containing values to update
     * @return The before update actions modified Entity to update. Default implementation returns the input parameter update.
     */
    protected ENTITY onBeforeUpdate(ENTITY existing, ENTITY update)
    {
        return update;
    }

    protected ENTITY doUpdate(ENTITY loaded)
    {
        return repository.save(loaded);
    }

    /**
     * Extension Point for after update actions. Modify ENTITY typed Entity that was updated. Default implementation returns parameter updated unmodified.
     *
     * @param updated The ENTITY typed Entity that was updated
     * @return The after update actions modified Entity to update. Default implementation returns the input parameter update.
     */
    protected ENTITY onAfterUpdate(ENTITY updated)
    {
        return updated;
    }

    /**
     * Extension Point for before delete actions. Modify id to delete Entity for. Default implementation returns parameter id unmodified.
     *
     * @param id The ID typed Entity id
     * @return The before delete actions modified id to delete Entity for. Default implementation returns the input parameter id.
     */
    protected ENTITY onBeforeDelete(ID id, ENTITY entity)
    {
        return entity;
    }

    /**
     * Extension Point for after delete actions. Perform after delete actions on the deleted Entity and its id. Default Implemetnation does nothing
     *
     * @param id      The ID typed Entity id
     * @param deleted The Entity as it was when it was deleted
     */
    protected void onAfterDelete(ID id, ENTITY deleted) {}

    // ===========================  private  Methods  ========================

    private void validateEntity(ENTITY beforeCreate, Class<? extends DataView> validationGroup)
    {
        Set<ConstraintViolation<ENTITY>> cvs = this.validator.validate(beforeCreate, validationGroup);
        if (cvs.size() > 0)
        {
            throw new ConstraintViolationException(cvs);
        }
    }

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
