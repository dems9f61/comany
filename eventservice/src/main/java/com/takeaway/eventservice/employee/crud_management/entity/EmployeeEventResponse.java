package com.takeaway.eventservice.employee.crud_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.takeaway.eventservice.employee.messaging.entity.EventType;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

import static java.time.ZoneId.systemDefault;
import static java.time.ZonedDateTime.ofInstant;

/**
 * User: StMinko Date: 20.03.2019 Time: 20:15
 *
 * <p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class EmployeeEventResponse
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @ApiModelProperty(example = "EMPLOYEE_CREATED")
    private EventType eventType;

    @ApiModelProperty(example = "6a225af8-e783-4e60-a5d0-418830330eab")
    private UUID employeeId;

    @ApiModelProperty(example = "stephan.minko@nba.com")
    private String emailAddress;

    @ApiModelProperty(example = "Stéphan")
    private String firstName;

    @ApiModelProperty(example = "Minko")
    private String lastName;

    @ApiModelProperty(example = "1980-03-23")
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    @DateTimeFormat(pattern = UsableDateFormat.Constants.DEFAULT_DATE_FORMAT)
    private ZonedDateTime birthday;

    @ApiModelProperty(example = "Java Development")
    private String departmentName;

    @ApiModelProperty(example = "2019-05-09T12:27:05.549Z")
    private Instant createdAt;
    // ============================  Constructors  ===========================

    public EmployeeEventResponse(PersistentEmployeeEvent employeeEvent)
    {
        this.eventType = employeeEvent.getEventType();
        this.employeeId = employeeEvent.getEmployeeId();
        this.emailAddress = employeeEvent.getEmailAddress();
        this.firstName = employeeEvent.getFirstName();
        this.lastName = employeeEvent.getLastName();
        this.birthday = ofInstant(employeeEvent.getBirthday().toInstant(), systemDefault());
        this.departmentName = employeeEvent.getDepartmentName();
        this.createdAt = employeeEvent.getCreatedAt();
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
