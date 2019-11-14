package com.takeaway.employeeservice.employee.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.takeaway.employeeservice.validation.boundary.NullOrNotBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import java.time.ZonedDateTime;

/**
 * User: StMinko Date: 10.04.2019 Time: 22:58
 *
 * <p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@EqualsAndHashCode
public class UpdateEmployeeRequest implements EmployeeRequest
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  @NullOrNotBlank
  @Email
  @ApiModelProperty(example = "hugues.minko@nba.com")
  private final String emailAddress;

  @NullOrNotBlank
  @ApiModelProperty(example = "Hugues")
  private final String firstName;

  @NullOrNotBlank
  @ApiModelProperty(example = "Minko")
  private final String lastName;

  @ApiModelProperty(example = "1980-06-13")
  @JsonDeserialize(using = JsonDateDeSerializer.class)
  @JsonSerialize(using = JsonDateSerializer.class)
  @DateTimeFormat(pattern = UsableDateFormat.Constants.DEFAULT_DATE_FORMAT)
  private final ZonedDateTime birthday;

  @NullOrNotBlank
  @ApiModelProperty(example = "Java Development")
  private final String departmentName;

  // ============================  Constructors  ===========================

  @JsonCreator
  public UpdateEmployeeRequest(@JsonProperty(value = "emailAddress") String emailAddress,
                               @JsonProperty(value = "firstName") String firstName,
                               @JsonProperty(value = "lastName") String lastName, @JsonProperty(value = "birthday") ZonedDateTime birthday,
                               @JsonProperty(value = "departmentName") String departmentName)
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
