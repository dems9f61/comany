package com.takeaway.authorization.integrationsupport.control;

import com.takeaway.authorization.integrationsupport.boundary.ApiResponsePage;
import com.takeaway.authorization.integrationsupport.boundary.DataView;
import com.takeaway.authorization.integrationsupport.entity.AbstractEntity;
import com.takeaway.authorization.integrationsupport.entity.ServiceException;
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
import java.util.Optional;
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
  public ApiResponsePage<ENTITY> findAll(Pageable pageable)
  {
    LOGGER.info("{}.findAll ( {} )", getClass().getSimpleName(), pageable);
    Page<ENTITY> all = repository.findAll(pageable);
    return new ApiResponsePage<>(all.getContent(), pageable, all.getTotalElements());
  }

  @Override
  public Optional<ENTITY> findById(@NotNull ID id)
  {
    LOGGER.info("{}.findOne ( {} )", this.getClass().getSimpleName(), id);
    return repository.findById(id);
  }

  @Override
  public ENTITY create(@Validated(DataView.POST.class) @NotNull ENTITY create) throws ServiceException
  {
    LOGGER.info("{}.create ( {} )", this.getClass().getSimpleName(), create);
    ENTITY beforeCreate = onBeforeCreate(create);
    validateEntity(beforeCreate, DataView.POST.class);
    ENTITY created = doCreate(beforeCreate);
    return onAfterCreate(created);
  }

  @Override
  public ENTITY update(@NotNull ID id, @NotNull ENTITY update, Class<? extends DataView> validationGroup) throws ServiceException
  {
    LOGGER.info("{}.update ( {}, {} )", this.getClass().getSimpleName(), id, update);
    validateEntity(update, validationGroup);
    ENTITY loaded = findById(id)
            .orElseThrow(() -> new ServiceException(ServiceException.Reason.RESOURCE_NOT_FOUND, String.format("Could not find an entity by the specified id [%s]!", id)));
    BeanTool.copyNonNullProperties(onBeforeUpdate(loaded, update), loaded, "id", "createdAt", "createdBy", "lastUpdatedAt", "lastUpdatedBy");
    return onAfterUpdate(doUpdate(loaded));
  }

  @Override
  public void delete(ID id) throws ServiceException
  {
    LOGGER.info("{}.delete ( {} )", this.getClass().getSimpleName(), id);
    ENTITY loaded = findById(id)
            .orElseThrow(() -> new ServiceException(ServiceException.Reason.RESOURCE_NOT_FOUND, String.format("Could not find an entity by the specified id [%s]!", id)));
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
   * Extension Point for before create actions. Modify ENTITY typed Entity to create. Default implementation returns parameter create unmodified.
   *
   * @param create The ENTITY typed Entity to create
   * @return The before create actions modified Entity to create. Default implementation returns the input parameter create.
   */
  protected ENTITY onBeforeCreate(ENTITY create) throws ServiceException
  {
    return create;
  }

  protected ENTITY doCreate(ENTITY beforeCreate) throws ServiceException
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
   * @param update The ENTITY typed Entity containing values to update
   * @return The before update actions modified Entity to update. Default implementation returns the input parameter update.
   */
  protected ENTITY onBeforeUpdate(ENTITY existing, ENTITY update) throws ServiceException
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
   * @param id The ID typed Entity id
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
