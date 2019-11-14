package com.takeaway.employeeservice.employee.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * User: StMinko Date: 19.03.2019 Time: 23:20
 *
 * <p>
 */
@Getter
@ToString
@EqualsAndHashCode
public class EmployeeResponse
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  @NotNull
  @ApiModelProperty(example = "6a225af8-e783-4e60-a5d0-418830330eab")
  private final UUID id;

  @ApiModelProperty(example = "stephan.minko@nba.com")
  private final String emailAddress;

  @ApiModelProperty(example = "Stéphan")
  private final String firstName;

  @ApiModelProperty(example = "Minko")
  private final String lastName;

  @ApiModelProperty(example = "1980-03-23")
  @JsonDeserialize(using = JsonDateDeSerializer.class)
  @JsonSerialize(using = JsonDateSerializer.class)
  @DateTimeFormat(pattern = UsableDateFormat.Constants.DEFAULT_DATE_FORMAT)
  private final Date birthday;

  @ApiModelProperty(example = "Java Development")
  private final String departmentName;

  // ============================  Constructors  ===========================

  @JsonCreator
  public EmployeeResponse(@JsonProperty(value = "id") UUID id,
                          @JsonProperty(value = "emailAddress") String emailAddress,
                          @JsonProperty(value = "firstName") String firstName,
                          @JsonProperty(value = "lastName") String lastName,
                          @JsonProperty(value = "birthday") Date birthday,
                          @JsonProperty(value = "departmentName",required = true) String departmentName)
  {
    this.id = id;
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
