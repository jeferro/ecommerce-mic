package com.jeferro.plugins.api_first_generator;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.compile.JavaCompile;
import org.openapitools.generator.gradle.plugin.OpenApiGeneratorPlugin;
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApiFirstGeneratorPlugin implements Plugin<Project> {

  @Override
  public void apply(Project target) {
	ApiFirstGeneratorExtension extension = target.getExtensions()
		.create("apiFirstGenerator", ApiFirstGeneratorExtension.class);

	target.getPlugins().apply(OpenApiGeneratorPlugin.class);

	target.afterEvaluate(project -> {

	  configureSourceSets(project, extension);

	  configureOpenapiTask(project, extension);
	});
  }

  private void configureSourceSets(Project project, ApiFirstGeneratorExtension extension) {
	extension.specs.forEach(spec -> {
	  project.getExtensions()
		  .getByType(SourceSetContainer.class).stream()
		  .filter(sourceSet -> sourceSet.getName().equals("main"))
		  .forEach(sourceSet -> {
			var targetDir = createTargetDir(extension, spec);

			sourceSet.getResources().srcDir(spec.specFile.getParentFile());

			var targetDirJava = new File(targetDir, "src/main/java");
			sourceSet.getJava().srcDir(targetDirJava);
		  });
	});

  }

  private void configureOpenapiTask(Project project, ApiFirstGeneratorExtension extension) {
	var providers = new ArrayList<>();

	extension.specs.forEach(spec -> {
	  var taskName = "openapi-" + spec.name;
	  var targetDir = createTargetDir(extension, spec);

	  var provider = project.getTasks().register(taskName, GenerateTask.class, task -> {
		task.getGeneratorName().set("spring");
		task.getInputSpec().set(spec.specFile.getAbsolutePath());
		task.getOutputDir().set(targetDir.getAbsolutePath());

		task.getPackageName().set(spec.basePackage);
		task.getApiPackage().set(spec.basePackage);
		task.getModelPackage().set(spec.basePackage + ".dtos");
		task.getModelNameSuffix().set("RestDTO");
		task.getApiNameSuffix().set("Api");

		task.getGenerateApiTests().set(false);

		Map<String, String> options = new HashMap<>();
		options.put("useTags", "true");
		options.put("gradleBuildFile", "false");
		options.put("useSpringBoot3", "true");
		options.put("documentationProvider", "none");
		options.put("interfaceOnly", "true");
		options.put("useResponseEntity", "false");
		task.getConfigOptions().set(options);
	  });

	  providers.add(provider);

	  project.getTasks().withType(JavaCompile.class)
		  .configureEach(javaCompile -> javaCompile.dependsOn(taskName));
	});

	project.getTasks().named("build")
		.configure(buildTask -> buildTask.dependsOn(providers.toArray()));
  }

  private File createTargetDir(ApiFirstGeneratorExtension extension, ApiFirstGeneratorSpec spec) {
	return new File(extension.buildDir, "openapi-" + spec.name);
  }
}
