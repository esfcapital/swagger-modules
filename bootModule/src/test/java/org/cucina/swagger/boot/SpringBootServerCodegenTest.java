package org.cucina.swagger.boot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.codegen.SwaggerCodegen;

import static org.junit.Assert.assertTrue;

/**
 * Tests {@link SpringBootServerCodegen}.
 */
public class SpringBootServerCodegenTest {

    private static final String CODEGEN_NAME = new org.cucina.swagger.boot.SpringBootServerCodegen()
            .getName();

    private static final String UTF8 = "UTF-8";

    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tempFolder = new org.junit.rules.TemporaryFolder();

    /**
     * Tests that CODEGEN_NAME is listed in the Available Code Generation
     * Languages.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testLanguage() throws java.io.IOException {
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
             java.io.PrintStream ps = new java.io.PrintStream(baos, true, UTF8)) {
            System.setOut(ps);
            SwaggerCodegen.main(new String[]{"langs"});
            assertTrue(baos.toString(UTF8).contains(CODEGEN_NAME));
            System.setOut(null);
        }
    }

    @org.junit.Test
    public void testConfigHelp() throws java.io.IOException {
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
             java.io.PrintStream ps = new java.io.PrintStream(baos, true, UTF8)) {
            System.setOut(ps);
            SwaggerCodegen.main(new String[]{"config-help", "-l", CODEGEN_NAME});
            final String output = baos.toString(UTF8);
            assertTrue(
                    output.contains(org.cucina.swagger.boot.SpringBootServerCodegen.BASE_PACKAGE));
            assertTrue(output
                    .contains(org.cucina.swagger.boot.SpringBootServerCodegen.RESOURCE_FOLDER));
            System.setOut(null);
        }
    }

    @org.junit.Test
    public void testGetHelp() {
        org.junit.Assert.assertEquals(
                "Generates a Java Spring Boot Server application using the SpringFox integration.",
                new org.cucina.swagger.boot.SpringBootServerCodegen().getHelp());
    }

    @org.junit.Test
    public void testSimpleGen() throws java.io.IOException {
        final java.io.File root = tempFolder.getRoot();

//		final String expectedBasePackage = packageToPath("io.swagger");
//		final String expectedApiPackage = packageToPath("io.swagger.api");
//		final String expectedConfigPackage = packageToPath("io.swagger.configuration");
        final String expectedModelPackage = packageToPath("io.swagger.model");
//		final String expectedResourceFolder = packageToPath("src.main.resources");

        SwaggerCodegen.main(new String[]{"generate", "-l", CODEGEN_NAME, "-i", "generator.yaml",
                "-o", root.getAbsolutePath()});

        final List<String> files = java.nio.file.Files
                .walk(java.nio.file.Paths.get(root.getAbsolutePath()))
                .filter(java.nio.file.Files::isRegularFile).map(p -> {
                    String s = p.toAbsolutePath().toString();

                    return (s.indexOf("src") > -1) ? s.substring(s.indexOf("src")) : s;
                }).collect(Collectors.toList());

        System.err.println(files);
        System.err.println(expectedModelPackage);

        // assertTrue(files.contains(expectedBasePackage));
        // assertTrue(files.contains(expectedApiPackage));
        // assertTrue(files.contains(expectedConfigPackage));
        assertTrue(files.contains(expectedModelPackage + "/Scope.java"));
        // assertTrue(files.contains(expectedResourceFolder));


        Files.walk(Paths.get(root.getAbsolutePath())).filter(Files::
                isRegularFile).forEach(p -> {
            try {
                Files.copy(p,
                        Paths.get("/home/viktor/tmp").resolve(p.getFileName()),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @org.junit.Test
    public void testComplexGen() throws java.io.IOException {
        final java.io.File root = tempFolder.getRoot();

        final String expectedBasePackage = packageToPath("com.company.project");
        final String expectedApiPackage = packageToPath("com.company.project.api");
        final String expectedConfigPackage = packageToPath("com.company.project.api.configuration");
        final String expectedModelPackage = packageToPath("com.company.project.model");
        final String expectedResourceFolder = packageToPath("src.test.resources");

        SwaggerCodegen.main(new String[]{"generate", "-l", CODEGEN_NAME, "-i", "generator.yaml",
                "-o", root.getAbsolutePath(), "--api-package", "com.company.project.api",
                "--model-package", "com.company.project.model", "--additional-properties",
                "configPackage=com.company.project.api.configuration,basePackage=com.company.project,resourceFolder=src.test.resources"});
        final List<String> files = java.nio.file.Files
                .walk(java.nio.file.Paths.get(root.getAbsolutePath()))
                .filter(java.nio.file.Files::isRegularFile).map(p -> {
                    String s = p.toAbsolutePath().toString();

                    return (s.indexOf("src") > -1) ? s.substring(s.indexOf("src")) : s;
                }).collect(Collectors.toList());

//		assertTrue(files.contains(expectedBasePackage));
//		assertTrue(files.contains(expectedApiPackage));
//		assertTrue(files.contains(expectedConfigPackage));
        assertTrue(files.contains(expectedModelPackage + "/Scope.java"));
//		assertTrue(files.contains(expectedResourceFolder));
    }

    private String packageToPath(final String pkg) {
        return "src/main/java/" + java.nio.file.Paths.get(pkg.replaceAll("\\.", "/")).toString();
    }
}
