package com.takeaway.employeeservice.employee.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.takeaway.employeeservice.employee.control.EmployeeParameter;
import com.takeaway.employeeservice.runtime.rest.DataView;
import com.takeaway.employeeservice.runtime.validation.NullOrNotBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.ZonedDateTime;

/**
 * User: StMinko Date: 19.03.2019 Time: 23:31
 *
 * <p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@EqualsAndHashCode
public class EmployeeRequest
{
    // =========================== Class Variables ===========================

    // Regex emailPattern to valid email address:
    // at the beginning only a-z, A-Z, 0-9 -_. are valid except for @
    // after an @ a-z, A-Z, 0-9 - is valid except for further @
    // after the last dot (.) only the defined characters [a-zA-Z] are valid with a minimum of 2
    // Does not support special chars any longer, besides -_.
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9\\-_.]+@[a-zA-Z0-9\\-.]+\\.[a-zA-Z]{2,}$";

    // =============================  Variables  =============================

    @JsonView({DataView.POST.class, DataView.PUT.class, DataView.PATCH.class})
    @NullOrNotBlank(groups = {DataView.PATCH.class, DataView.POST.class})
    @ApiModelProperty(example = "stephan.minko@nba.com")
    @Pattern(regexp = EMAIL_REGEX)
    private final String emailAddress;

    @JsonView({DataView.POST.class, DataView.PUT.class, DataView.PATCH.class})
    @NullOrNotBlank(groups = {DataView.PATCH.class, DataView.POST.class})
    @ApiModelProperty(example = "Stéphan")
    private final String firstName;

    @JsonView({DataView.POST.class, DataView.PUT.class, DataView.PATCH.class})
    @NullOrNotBlank(groups = {DataView.PATCH.class, DataView.POST.class})
    @ApiModelProperty(example = "Minko")
    private final String lastName;

    @JsonView({DataView.POST.class, DataView.PUT.class, DataView.PATCH.class})
    @ApiModelProperty(example = "1980-06-13")
    @NullOrNotBlank(groups = {DataView.PATCH.class, DataView.POST.class})
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    @DateTimeFormat(pattern = UsableDateFormat.Constants.DEFAULT_DATE_FORMAT)
    private final ZonedDateTime birthday;

    @JsonView({DataView.POST.class, DataView.PUT.class, DataView.PATCH.class})
    @NullOrNotBlank(groups = {DataView.PATCH.class})
    @NotBlank(groups = {DataView.POST.class, DataView.PUT.class})
    @ApiModelProperty(example = "Java Development")
    private final String departmentName;

    // ============================  Constructors  ===========================

    @JsonCreator
    public EmployeeRequest(@JsonProperty(value = "emailAddress") String emailAddress,
                @JsonProperty(value = "firstName") String firstName,
                @JsonProperty(value = "lastName") String lastName,
                @JsonProperty(value = "birthday") ZonedDateTime birthday,
                @JsonProperty(value = "departmentName") String departmentName)
    {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.departmentName = departmentName;
    }

    // ===========================  public  Methods  =========================

    public final EmployeeParameter toEmployerParameter()
    {
        return EmployeeParameter.builder()
                    .emailAddress(getEmailAddress())
                    .birthday(getBirthday())
                    .firstName(getFirstName())
                    .lastName(getLastName())
                    .departmentName(getDepartmentName())
                    .build();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
