package com.ssafy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);
	
	@Bean
	public Docket postsApi() {
		logger.debug("Swagger ready..........준비중");
		
		return new Docket(DocumentationType.SWAGGER_2)
					.groupName("public-api")
					.apiInfo(apiInfo())
					.select().paths(PathSelectors.ant("/admin/**")) 
					.build();
	}
	
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				   .title("SSAFY QnA API")
				   .description("<h3>도희와 지윤이의 QnA 만들기~</h3>")
				   .license("SSAFY Licences")
				   .licenseUrl("http://ssafy.com")
				   .version("1.0")
				   .build();
	}
}