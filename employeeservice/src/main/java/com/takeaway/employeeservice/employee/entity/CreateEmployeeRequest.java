package com.takeaway.employeeservice.employee.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
public class CreateEmployeeRequest implements EmployeeRequest
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
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    @DateTimeFormat(pattern = UsableDateFormat.Constants.DEFAULT_DATE_FORMAT)
    private final Date birthday;

    @ApiModelProperty(example = "Java Development")
    private final String departmentName;

    // ============================  Constructors  ===========================

    @JsonCreator
    public CreateEmployeeRequest(@JsonProperty(value = "emailAddress") String emailAddress,
                                 @JsonProperty(value = "firstName") String firstName,
                                 @JsonProperty(value = "lastName") String lastName,
                                 @JsonProperty(value = "birthday") Date birthday,
                                 @JsonProperty(value = "departmentName",
                                         required = true) String departmentName)
    {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.departmentName = departmentName;
    }

    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
