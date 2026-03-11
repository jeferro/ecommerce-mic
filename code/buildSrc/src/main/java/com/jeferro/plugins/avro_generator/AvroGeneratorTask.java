package com.jeferro.plugins.avro_generator;

import org.apache.avro.Schema;
import org.apache.avro.compiler.specific.SpecificCompiler;
import org.apache.avro.generic.GenericData.StringType;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.compile.JavaCompile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AvroGeneratorTask implements Action<Task> {

    private static final String TASK_ID = "avroGeneratorTask";

    public static final String IMPORTS_FILENAME = "imports.avsc";

    private final File schemaDir;

    private final File targetDir;

    public AvroGeneratorTask(File schemaDir, File targetDir) {
        this.schemaDir = schemaDir;
        this.targetDir = new File(targetDir, "src/main/java");
    }

    public static void register(Project project, File schemasDir, File targetDir) {
        AvroGeneratorTask avroGeneratorTask = new AvroGeneratorTask(schemasDir, targetDir);
        project.task(TASK_ID, avroGeneratorTask);

        project.getTasks().withType(JavaCompile.class)
                .configureEach(javaCompile -> javaCompile.dependsOn(TASK_ID));
    }

    @Override
    public void execute(Task task) {
        task.doLast(this::doLast);
    }

    private void doLast(Task task) {
        try {
            var avroFiles = getAvroFiles(schemaDir);
            //SpecificCompiler.compileSchema(avroFiles, targetDir);
            Schema.Parser parser = new Schema.Parser();

            for (var avroFile : avroFiles) {
                if(avroFile.getName().contains(".DS_Store")) {
                  continue;
                }

                var schema = parser.parse(avroFile);

                var compiler = new SpecificCompiler(schema);
                compiler.setStringType(StringType.String);
                compiler.compileToDestination(avroFile, targetDir);
            }
        } catch (IOException cause) {
            throw new RuntimeException(cause);
        }
    }

    private File[] getAvroFiles(File schemaDir) {
        var avroFiles = getAvroFilesRecursively(schemaDir);

        var avroFilesArray = new File[avroFiles.size()];

        return avroFiles.toArray(avroFilesArray);
    }

    private List<File> getAvroFilesRecursively(File directory) {
        List<File> schemaFiles = new ArrayList<>();

        var files = directory.listFiles();

        if (files == null) {
            return List.of();
        }

        for (File file : files) {
            if (file.isDirectory()) {
                schemaFiles.addAll(getAvroFilesRecursively(file));
            } else if (file.getName().matches(IMPORTS_FILENAME)) {
                schemaFiles.addFirst(file);
            } else {
                schemaFiles.add(file);
            }
        }

        return schemaFiles;
    }
}
