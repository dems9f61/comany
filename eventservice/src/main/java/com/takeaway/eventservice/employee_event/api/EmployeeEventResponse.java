package com.takeaway.eventservice.employee_event.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.takeaway.eventservice.employee_event.service.PersistentEmployeeEvent;
import com.takeaway.eventservice.messaging.dto.EventType;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 20:15
 * <p/>
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

    @ApiModelProperty(example = "67")
    private long id;

    private EventType eventType;

    @ApiModelProperty(example = "6a225af8-e783-4e60-a5d0-418830330eab")
    private String uuid;

    @ApiModelProperty(example = "stephan.minko@nba.com")
    private String emailAddress;

    @ApiModelProperty(example = "Stéphan")
    private String firstName;

    @ApiModelProperty(example = "Minko")
    private String lastName;

    @ApiModelProperty(example = "1980-03-23")
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @ApiModelProperty(example = "Java Development")
    private String departmentName;

    // ============================  Constructors  ===========================

    EmployeeEventResponse(PersistentEmployeeEvent employeeEvent)
    {
        this.id = employeeEvent.getId();
        this.eventType = employeeEvent.getEventType();
        this.uuid = employeeEvent.getUuid();
        this.emailAddress = employeeEvent.getEmailAddress();
        this.firstName = employeeEvent.getFirstName();
        this.lastName = employeeEvent.getLastName();
        this.birthday = employeeEvent.getBirthday();
        this.departmentName = employeeEvent.getDepartmentName();
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
