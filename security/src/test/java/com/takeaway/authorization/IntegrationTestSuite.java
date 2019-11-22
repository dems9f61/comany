package com.takeaway.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.takeaway.authorization.oauth.boundary.AccessTokenParameter;
import com.takeaway.authorization.oauth.entity.OAuthClientTestFactory;
import com.takeaway.authorization.permission.entity.PermissionTestFactory;
import com.takeaway.authorization.role.entity.RoleTestFactory;
import com.takeaway.authorization.user.entity.UserTestFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * User: StMinko Date: 14.10.2019 Time: 15:07
 *
 * <p>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,
        classes = { AuthorizationServiceApplication.class })
@ActiveProfiles("INTEGRATION")
@AutoConfigureMockMvc
public abstract class IntegrationTestSuite
{
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                        MediaType.APPLICATION_JSON.getSubtype(),
                                                                        StandardCharsets.UTF_8);

  @Autowired
  protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

  @Autowired
  protected RoleTestFactory roleTestFactory;

  @Autowired
  protected PermissionTestFactory permissionTestFactory;

  @Autowired
  protected UserTestFactory userTestFactory;

    @Autowired
    protected OAuthClientTestFactory oAuthClientTestFactory;

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

    protected String obtainAccessToken() throws Exception
    {
        AccessTokenParameter accessTokenParameter = AccessTokenParameter.builder()
                                                                        .clientId("client")
                                                                        .clientSecret("secret")
                                                                        .scopes("read,write")
                                                                        .userName("admin")
                                                                        .userPassword("admin")
                                                                        .build();
        return obtainAccessToken(accessTokenParameter);
    }

    protected String obtainAccessToken(AccessTokenParameter accessTokenParameter) throws Exception
    {
        String basicAuthorizationHeader = new ClientSecretBasic(new ClientID(accessTokenParameter.getClientId()),
                                                                new Secret(accessTokenParameter.getClientSecret())).toHTTPAuthorizationHeader();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", accessTokenParameter.getGrantType());
        params.add("client_id", accessTokenParameter.getClientId());
        params.add("username", accessTokenParameter.getUserName());
        params.add("password", accessTokenParameter.getUserPassword());
        String scopes = accessTokenParameter.getScopes();
        if (StringUtils.isNotBlank(scopes))
        {
            params.add("scopes", scopes);
        }

        String resultString = mockMvc.perform(post("/oauth/token").params(params)
                                                                  .header(HttpHeaders.AUTHORIZATION, basicAuthorizationHeader)
                                                                  .accept(APPLICATION_JSON_UTF8))
                                     .andReturn()
                                     .getResponse()
                                     .getContentAsString();

        if (StringUtils.isBlank(resultString))
        {
            return resultString;
        }
        else
        {
            JacksonJsonParser jsonParser = new JacksonJsonParser();
            Map<String, Object> stringObjectMap = jsonParser.parseMap(resultString);

            String accessTokenKey = "access_token";
            Object accessTokenValue = stringObjectMap.get(accessTokenKey);
            return Objects.nonNull(accessTokenValue) ?
                    accessTokenValue.toString() :
                    stringObjectMap.get("error_description")
                                   .toString();
        }
    }
}
