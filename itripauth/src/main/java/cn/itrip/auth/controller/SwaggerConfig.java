package cn.itrip.auth.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
         return new Docket(DocumentationType.SWAGGER_2)
                 .apiInfo(apiInfo())
                 .select()
                 .apis(RequestHandlerSelectors.any())//扫描com路径下的api文档
                 .paths(PathSelectors.any())//路径判断
                 .build();
    }
     private ApiInfo apiInfo() {
         return new ApiInfoBuilder()
                 .title("爱旅行-用户认证模块API")//标题
                 .description("http://yang-dong.club:8080//auth")//描述
                 .termsOfServiceUrl("http://yang-dong.club:8080//auth")
                 .contact("itrip项目组")//作者信息
                 .version("1.0")//版本号
                .build();
     }
 }