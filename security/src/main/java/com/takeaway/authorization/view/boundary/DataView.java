package com.takeaway.authorization.view.boundary;

/**
 * User: StMinko Date: 11.10.2019 Time: 16:20
 *
 * <p>
 */
public interface DataView
{
  interface GET extends DataView {}

  interface POST extends DataView {}

  interface PUT extends DataView {}

  interface PATCH extends DataView {}
}
