package com.takeaway.eventservice.resttest;

import com.takeaway.eventservice.ApiVersions;
import com.takeaway.eventservice.RestTestSuite;
import com.takeaway.eventservice.employee_event.api.EmployeeEventResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 21:14
 * <p/>
 */
@DisplayName("Rest tests for the employee event service")
class EmployeeEventRestTest extends RestTestSuite
{
    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("GET: 'http://.../events/{uuid}' returns OK and an asc sorted list ")
        void givenEmployeeVents_whenFindByUuid_thenStatus200AndAscSortedList()
        {
            // Arrange
            String uuid = UUID.randomUUID()
                              .toString();
            publishRandomEventsFor(uuid);

            String uri = String.format("%s/events", ApiVersions.V1);

            // Act
            ResponseEntity<List<EmployeeEventResponse>> responseEntity = testRestTemplate.exchange(String.format("%s/%s", uri, uuid),
                                                                                                   HttpMethod.GET,
                                                                                                   new HttpEntity<>(defaultHttpHeaders()),
                                                                                                   new ParameterizedTypeReference<List<EmployeeEventResponse>>() {});

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            List<EmployeeEventResponse> eventResponses = responseEntity.getBody();
            assertThat(eventResponses).isNotNull()
                                      .isNotEmpty();
            long previousId = Long.MIN_VALUE;
            for (EmployeeEventResponse event : eventResponses)
            {
                long currentId = event.getId();
                assertThat(previousId).isLessThan(currentId);
                previousId = currentId;
            }
        }
    }
}
