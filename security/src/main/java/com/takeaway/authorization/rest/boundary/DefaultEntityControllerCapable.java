package com.takeaway.authorization.rest.boundary;

import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authorization.persistence.boundary.AbstractEntity;
import com.takeaway.authorization.view.boundary.DataView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User: StMinko Date: 15.10.2019 Time: 11:04
 *
 * <p>
 */
@Validated
public interface DefaultEntityControllerCapable<ENTITY extends AbstractEntity<ID>, ID extends Serializable>
{
  // =========================== Class Variables ===========================
  // ==============================  Methods  ==============================

  Page<ENTITY> findAll(@NotNull Pageable pageable);

  ENTITY findById(@PathVariable @NotNull ID id);

  ResponseEntity<ENTITY> create(@RequestBody @JsonView(DataView.POST.class) @NotNull ENTITY create);

  ENTITY doFullUpdate(@PathVariable @NotNull ID id, @RequestBody @JsonView(DataView.PUT.class) ENTITY update);

  ENTITY doPartialUpdate(@PathVariable @NotNull ID id, @RequestBody @JsonView(DataView.PATCH.class) ENTITY update);

  void delete(@PathVariable @NotNull ID id);

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
