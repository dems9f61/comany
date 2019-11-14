package com.takeaway.authorization.json.boundary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * User: StMinko Date: 13.05.2019 Time: 16:00
 *
 * <p>
 */
public class ResponsePage<T> extends PageImpl<T>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public ResponsePage(@JsonProperty("content") List<T> content,
                      @JsonProperty("number") int number,
                      @JsonProperty("size") int size,
                      @JsonProperty("totalElements") Long totalElements,
                      @JsonProperty("pageable") JsonNode pageable,
                      @JsonProperty("last") boolean last,
                      @JsonProperty("totalPages") int totalPages,
                      @JsonProperty("sort") JsonNode sort,
                      @JsonProperty("first") boolean first,
                      @JsonProperty("numberOfElements") int numberOfElements)
  {
    super(content, PageRequest.of(number, size), totalElements);
  }

    public ResponsePage(List<T> content, Pageable pageable, long total)
  {
    super(content, pageable, total);
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
