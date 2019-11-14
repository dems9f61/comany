package com.takeaway.employeeservice.rest.boundary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.LinkedList;
import java.util.List;

/**
 * User: StMinko Date: 18.03.2019 Time: 23:48
 *
 * <p>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig
{
  // =========================== Class Variables ===========================

  private static final String SOURCE_PACKAGE = "com.takeaway";

  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @Bean
  public Docket api()
  {
    return new Docket(DocumentationType.SWAGGER_2)
        .useDefaultResponseMessages(true)
        .enableUrlTemplating(true)
        .select()
        .apis(RequestHandlerSelectors.basePackage(SOURCE_PACKAGE))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================

  private ApiInfo apiInfo()
  {
    List<VendorExtension> vendorExtensions = new LinkedList<>();
    vendorExtensions.add(new VendorExtension()
        {
          @Override
          public String getName()
          {
            return "Minko";
          }

          @Override
          public Object getValue()
          {
            return "https://www.lieferando.de";
          }
        });

    return new ApiInfo("REST API of " + applicationName(),
        description(),
        version(),
        "Terms of service",
        new Contact("Stéphan Minko", "https://www.lieferando.de", "stephan.minko@lieferando.com"),
        null,
        null,
        vendorExtensions);
  }

  private String applicationName()
  {
    return "employee-service";
  }

  private String description()
  {
    return "Swagger API documentation of this micro service.";
  }

  private String version()
  {
    return ApiVersions.V1;
  }

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
