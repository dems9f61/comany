package com.takeaway.authentication.integrationsupport.boundary;

import com.fasterxml.jackson.annotation.JsonView;
import com.takeaway.authentication.integrationsupport.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User: StMinko Date: 15.10.2019 Time: 11:04
 *
 * <p>
 */
@Validated
public interface DefaultEntityController<ENTITY extends AbstractEntity<ID>, ID extends Serializable>
{
  // =========================== Class Variables ===========================
  // ==============================  Methods  ==============================

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Page<ENTITY> findAll(@NotNull Pageable pageable);

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @JsonView(DataView.GET.class)
  ENTITY findById(@PathVariable @NotNull ID id);

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @JsonView(DataView.GET.class)
  ResponseEntity<ENTITY> create(@RequestBody @JsonView(DataView.POST.class) @NotNull ENTITY create);

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  @JsonView(DataView.GET.class)
  ENTITY doFullUpdate(@PathVariable @NotNull ID id, @RequestBody @JsonView(DataView.PUT.class) ENTITY update);

  @PatchMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  @JsonView(DataView.GET.class)
  ENTITY doPartialUpdate(@PathVariable @NotNull ID id, @RequestBody @JsonView(DataView.PATCH.class) ENTITY update);

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  void delete(@PathVariable @NotNull ID id);

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
