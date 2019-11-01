package com.takeaway.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.takeaway.authorization.permission.entity.PermissionTestFactory;
import com.takeaway.authorization.role.entity.RoleTestFactory;
import com.takeaway.authorization.user.entity.UserTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.Charset;
import java.util.TimeZone;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * User: StMinko Date: 14.10.2019 Time: 15:07
 *
 * <p>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,
        classes = { AuthorizationServiceApplication.class })
@ActiveProfiles("INTEGRATION")
public abstract class IntegrationTestSuite
{
  public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected RoleTestFactory roleTestFactory;

  @Autowired
  protected PermissionTestFactory permissionTestFactory;

  @Autowired
  protected UserTestFactory userTestFactory;

  @Autowired
  private DatabaseCleaner databaseCleaner;

  protected String transformRequestToJSON(Object object, Class<?> serializationView) throws Exception
  {
    ObjectWriter objectWriter = objectMapper.writerWithView(serializationView).withDefaultPrettyPrinter();
    String result = objectWriter.writeValueAsString(object);
    objectMapper.writer().withDefaultPrettyPrinter();
    return result;
  }

  protected String transformRequestToJSON(Object object) throws Exception
  {
    ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    return objectWriter.writeValueAsString(object);
  }

  @BeforeAll
  static void setUpForAll()
  {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @AfterEach
  void tearDown()
  {
    databaseCleaner.cleanDatabases();
  }
}
