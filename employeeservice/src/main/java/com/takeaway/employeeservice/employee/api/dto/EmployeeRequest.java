package com.takeaway.employeeservice.employee.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.takeaway.employeeservice.employee.service.EmployeeParameter;
import com.takeaway.employeeservice.utils.LocalDateDeserializer;
import com.takeaway.employeeservice.utils.LocalDateSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 23:31
 * <p/>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@EqualsAndHashCode
public class EmployeeRequest
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @ApiModelProperty(example = "stephan.minko@nba.com")
    private final String emailAddress;

    @ApiModelProperty(example = "Stéphan")
    private final String firstName;

    @ApiModelProperty(example = "Minko")
    private final String lastName;

    @ApiModelProperty(example = "1980-06-13")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthday;

    @ApiModelProperty(example = "Java Development")
    private final String departmentName;

    // ============================  Constructors  ===========================

    @JsonCreator
    EmployeeRequest(@JsonProperty(value = "emailAddress", required = true) String emailAddress,
                    @JsonProperty(value = "firstName", required = true) String firstName,
                    @JsonProperty(value = "lastName", required = true) String lastName,
                    @JsonProperty(value = "birthday", required = true) LocalDate birthday,
                    @JsonProperty(value = "departmentName", required = true) String departmentName)
    {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.departmentName = departmentName;
    }

    public EmployeeParameter toEmployerParameter()
    {
        return EmployeeParameter.builder()
                                .emailAddress(emailAddress)
                                .birthday(birthday)
                                .firstName(firstName)
                                .departmentName(departmentName)
                                .lastName(lastName)
                                .build();
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
