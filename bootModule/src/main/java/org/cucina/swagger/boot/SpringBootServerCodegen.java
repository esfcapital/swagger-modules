package org.cucina.swagger.boot;

import io.swagger.codegen.CliOption;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.SpringMVCServerCodegen;
import io.swagger.models.properties.AbstractNumericProperty;
import io.swagger.models.properties.Property;

/**
 * Swagger Code Generator to generate required Java Source Code for a Spring
 * Boot Server.
 */
public class SpringBootServerCodegen extends SpringMVCServerCodegen {

    /**
     * Base package for generated Spring Boot Application.
     */
    public static final String BASE_PACKAGE = "basePackage";

    /**
     * Resource folder for generated resources.
     */
    public static final String RESOURCE_FOLDER = "resourceFolder";

    private static final String LANGUAGE = "springBoot";

    private String resourceFolder;

    private String basePackage;

    /**
     * Default Spring Boot Server Codegen Constructor.
     */
    public SpringBootServerCodegen() {
        super();
        title = "Spring Boot Server";
        embeddedTemplateDir = templateDir = getName();
        artifactId = "swagger-spring-boot-server";
        basePackage = "io.swagger";
        resourceFolder = "src.main.resources";

        cliOptions.add(new CliOption(RESOURCE_FOLDER, "resource folder for generated resources"));
        cliOptions.add(new CliOption(BASE_PACKAGE, "base package for generated Spring Boot Application"));
    }

    /**
     * Sets the Base Package.
     *
     * @param basePackage Base Package
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * Sets the Resource Folder.
     *
     * @param resourceFolder
     */
    public void setResourceFolder(String resourceFolder) {
        this.resourceFolder = resourceFolder;
    }

    @Override
    public String getName() {
        return LANGUAGE;
    }

    @Override
    public String getHelp() {
        return "Generates a Java Spring Boot Server application using the SpringFox integration.";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        if (additionalProperties.containsKey(RESOURCE_FOLDER)) {
            this.setResourceFolder((String) additionalProperties.get(RESOURCE_FOLDER));
        }

        if (additionalProperties.containsKey(BASE_PACKAGE)) {
            this.setBasePackage((String) additionalProperties.get(BASE_PACKAGE));
        }

        supportingFiles.clear();
        supportingFiles.add(new SupportingFile("pom.mustache", "", "pom.xml"));
        supportingFiles.add(new SupportingFile("apiOriginFilter.mustache",
                (sourceFolder + java.io.File.separator + apiPackage).replace(".", java.io.File.separator),
                "ApiOriginFilter.java"));
        supportingFiles.add(new SupportingFile("apiResponseMessage.mustache",
                (sourceFolder + java.io.File.separator + apiPackage).replace(".", java.io.File.separator),
                "ApiResponseMessage.java"));

        supportingFiles.add(new SupportingFile("swaggerConfig.mustache",
                (sourceFolder + java.io.File.separator + configPackage).replace(".", java.io.File.separator),
                "SwaggerConfig.java"));
        supportingFiles.add(new SupportingFile("webApplication.mustache",
                (sourceFolder + java.io.File.separator + configPackage).replace(".", java.io.File.separator),
                "WebApplication.java"));
        supportingFiles.add(new SupportingFile("webMvcConfiguration.mustache",
                (sourceFolder + java.io.File.separator + configPackage).replace(".", java.io.File.separator),
                "WebMvcConfiguration.java"));
        supportingFiles.add(new SupportingFile("swaggerUiConfiguration.mustache",
                (sourceFolder + java.io.File.separator + configPackage).replace(".", java.io.File.separator),
                "SwaggerUiConfiguration.java"));

        supportingFiles.add(new SupportingFile("swagger.properties",
                resourceFolder.replace(".", java.io.File.separator), "swagger.properties"));
        supportingFiles.add(new SupportingFile("application.mustache",
                (sourceFolder + java.io.File.separator + basePackage).replace(".", java.io.File.separator),
                "Application.java"));
        supportingFiles.add(new SupportingFile("package-info.mustache",
                (sourceFolder + java.io.File.separator + modelPackage).replace(".", java.io.File.separator),
                "package-info.java"));

    }

    @Override
    public CodegenProperty fromProperty(String name, Property p) {
        CodegenProperty cp = super.fromProperty(name, p);
        if (p instanceof AbstractNumericProperty) {
            AbstractNumericProperty bip = (AbstractNumericProperty) p;
            if (bip.getMinimum() != null) {
                String min = String.format("%.0f", bip.getMinimum());
                cp.min = min;
                // cp.allowableValues.put("min", min);
            }
            if (bip.getMaximum() != null) {
                String max = String.format("%.0f", bip.getMaximum());
                cp.max = max;
                // cp.allowableValues.put("max", max);
            }
        }

        return cp;
    }
}
