package com.takeaway.eventservice.resttest;

import com.takeaway.eventservice.RestTestSuite;
import com.takeaway.eventservice.employee.crud_management.boundary.EmployeeEventController;
import com.takeaway.eventservice.employee.crud_management.entity.ApiResponsePage;
import com.takeaway.eventservice.employee.crud_management.entity.EmployeeEventResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: StMinko Date: 20.03.2019 Time: 21:14
 *
 * <p>
 */
@DisplayName("Rest tests for the employee event service")
class EmployeeEventRestTest extends RestTestSuite
{
  @Nested
  @DisplayName("when access")
  class WhenAccess
  {
    @Test
    @DisplayName("GET: 'http://.../events/{id}' returns OK and an asc sorted list ")
    void givenEmployeeVents_whenFindByUuid_thenStatus200AndAscSortedList()
    {
      // Arrange
        UUID id = UUID.randomUUID();
        receiveRandomMessageFor(id);

      // Act
        ResponseEntity<ApiResponsePage<EmployeeEventResponse>> responseEntity = testRestTemplate.exchange(String.format("%s/%s",
                                                                                                                        EmployeeEventController.BASE_URI,
                                                                                                                        id),
                                                                                                          HttpMethod.GET,
                                                                                                          new HttpEntity<>(defaultHttpHeaders()),
                                                                                                          new ParameterizedTypeReference<ApiResponsePage<EmployeeEventResponse>>() {});

      // Assert
      assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
      Page<EmployeeEventResponse> eventResponsePage = responseEntity.getBody();
      assertThat(eventResponsePage).isNotNull().isNotEmpty();
      Instant previous = null;
      for (EmployeeEventResponse event : eventResponsePage)
      {
        Instant current = event.getCreatedAt();
        if (previous != null)
        {
          assertThat(previous).isBefore(current);
        }
        previous = current;
      }
    }

//      @Test
//      @DisplayName("GET: 'http://.../events/{employeeId}' returns NOT FOUND for unknown id ")
//      void givenUnknownId_whenFindByUuid_thenStatus404()
//      {
//          // Arrange
//          UUID unknownId = UUID.randomUUID();
//
//          ResponseEntity<String> responseEntity = testRestTemplate.exchange(String.format("%s/%s", EmployeeEventController.BASE_URI, unknownId),
//                                                                            HttpMethod.GET,
//                                                                            new HttpEntity<>(defaultHttpHeaders()),
//                                                                            String.class);
//          assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//          assertThat(responseEntity.getBody()).contains(String.format("Could not find employee events for employee id [%s]", unknownId));
//      }
  }
}
