package com.takeaway.authorization.runtime.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.runtime.businessservice.boundary.EntityService;
import com.takeaway.authorization.runtime.persistence.AbstractEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User: StMinko Date: 15.10.2019 Time: 10:32
 *
 * <p>
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultEntityController<SERVICE extends EntityService<ENTITY, ID>, ENTITY extends AbstractEntity<ID>, ID extends Serializable>
        implements DefaultEntityControllerCapable<ENTITY, ID>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Getter
    protected final SERVICE service;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public final ResponsePage<ENTITY> findAll(@NotNull Pageable pageable)
    {
        LOGGER.info("Calling {}.findAll",
                    this.getClass()
                        .getSimpleName());
        return getService().findAll(pageable);
    }

    @Override
    public final ENTITY findById(@NotNull ID id)
    {
        LOGGER.info("Calling {}.findById( {} )",
                    this.getClass()
                        .getSimpleName(),
                    id);
        return getService().findById(id);
    }

    @Override
    public final ResponseEntity<ENTITY> create(@RequestBody @JsonView(DataView.POST.class) ENTITY create)
    {
        LOGGER.info("{}.create ( {} )",
                    this.getClass()
                        .getSimpleName(),
                    create);
        create = onBeforeCreate(create);
        ENTITY created = getService().create(create);
        HttpHeaders headers = new HttpHeaders();
        created = onAfterCreate(created, headers);
        headers.add(HttpHeaders.LOCATION,
                    ServletUriComponentsBuilder.fromCurrentRequestUri()
                                               .path("/{id}")
                                               .buildAndExpand(created.getId())
                                               .toUri()
                                               .toASCIIString());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .headers(headers)
                             .body(created);
    }

    @Override
    public final ENTITY doFullUpdate(ID id, ENTITY update)
    {
        LOGGER.info("{}.doFullUpdate ( {}, {} )",
                    this.getClass()
                        .getSimpleName(),
                    id,
                    update);
        return update(id, update, DataView.PUT.class);
    }

    @Override
    public final ENTITY doPartialUpdate(ID id, ENTITY update)
    {
        LOGGER.info("{}.doPartialUpdate ( {}, {} )",
                    this.getClass()
                        .getSimpleName(),
                    id,
                    update);
        return update(id, update, DataView.PATCH.class);
    }

    public final void delete(ID id)
    {
        LOGGER.info("{}.delete ( {} )",
                    this.getClass()
                        .getSimpleName(),
                    id);
        getService().delete(onBeforeDelete(id));
        onAfterDelete(id);
        onAfterDelete(id);
    }

    // =================  protected/package local  Methods ===================

    /**
     * Extension Point for before create actions. Should return the ENTITY to create. Default implementation returns the input parameter create.
     *
     * @param create The ENTITY typed Entity to create
     * @return The Entity values to create, modified by "before create actions". Default implementation returns the input parameter create.
     */
    protected ENTITY onBeforeCreate(ENTITY create)
    {
        return create;
    }

    /**
     * Extension Point for after create actions. Should return the ENTITY to create. Default implementation returns the input parameter create. Only called if an Entity could
     * be created.
     *
     * @param created The ENTITY typed Entity to create
     * @return The after create actions modified Entity that was created. Default implementation returns the input parameter created.
     */
    protected ENTITY onAfterCreate(ENTITY created, HttpHeaders responseHeaders)
    {
        return created;
    }

    // ===========================  private  Methods  ========================

    private ENTITY update(ID id, ENTITY update, Class<? extends DataView> validationGroup)
    {
        update = onBeforeUpdate(id, update);
        ENTITY updated = getService().update(id, update, validationGroup);
        return onAfterUpdate(id, updated);
    }

    /**
     * Extension Point for before update actions. Should return the ENTITY containing values to update for Entity identified by id. Default implementation returns the input
     * parameter create.
     *
     * @param id     The ID typed id of the Entity to perform an update on
     * @param update The ENTITY typed Entity containing values to update on the Entity identified by id
     * @return The Entity values to update, modified by "before update actions". Default implementation returns the input parameter update.
     */
    protected ENTITY onBeforeUpdate(ID id, ENTITY update)
    {
        return update;
    }

    /**
     * Extension Point for after update actions. Should return the ENTITY that was updated. Default implementation returns the input parameter create. Only called if an Entity
     * could be found for id and updated.
     *
     * @param id      The ID typed id of the Entity to perform an update on
     * @param updated The ENTITY typed Entity to create
     * @return The after create actions modified Entity that was created. Default implementation returns the input parameter updated.
     */
    protected ENTITY onAfterUpdate(ID id, ENTITY updated)
    {
        return updated;
    }

    /**
     * Extension Point for before delete actions. Modify id if necessary. Default implementation returns the input parameter od.
     *
     * @param id The ID typed id of the Entity to perform an update on
     * @return The before delete actions modified ID typed id that an Entity should be deleted for. Default implementation returns the input parameter id.
     */
    protected ID onBeforeDelete(ID id)
    {
        return id;
    }

    /**
     * Extension Point for before delete actions. Modify id if necessary. Default implementation returns the input parameter id. Only called if an Entity could be found for
     * id.
     *
     * @param id The ID typed id of the Entity that was deleted
     */
    protected void onAfterDelete(ID id) {}

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
