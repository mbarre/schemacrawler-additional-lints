package io.github.mbarre.schemacrawler.tool.linter;

/*
 * #%L
 * Additional SchemaCrawler Lints
 * %%
 * Copyright (C) 2015 - 2016 github
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.gson.*;
import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.tools.executable.SchemaCrawlerExecutable;
import schemacrawler.tools.lint.executable.LintOptionsBuilder;
import schemacrawler.tools.lint.executable.LintReportOutputFormat;
import schemacrawler.tools.options.OutputOptions;
import schemacrawler.tools.options.OutputOptionsBuilder;
import schemacrawler.tools.options.TextOutputFormat;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Created by barmi83 on 24/12/15.
 */
@SuppressWarnings({ "static-access", "deprecation" })
public abstract class BaseLintTest {

    private static final Logger LOGGER = Logger.getLogger(BaseLintTest.class.getName());


    protected List<LintWrapper> executeToJsonAndConvertToLintList(String linterName, SchemaCrawlerOptions options, Connection connection)  throws Exception{

        final SchemaCrawlerExecutable executable = new SchemaCrawlerExecutable("lint");

        final Path linterConfigsFile = FileSystems.getDefault().getPath("", this.getClass().getClassLoader().getResource(linterName + "/schemacrawler-linter-configs-test.xml").getPath());
        final LintOptionsBuilder optionsBuilder = LintOptionsBuilder.builder();
        optionsBuilder.withLinterConfigs(linterConfigsFile.toString());
        executable.setAdditionalConfiguration(optionsBuilder.toConfig());

        Path out = Paths.get("target/test_"+this.getClass().getSimpleName()+".json");
        OutputOptions outputOptions = OutputOptionsBuilder.builder().newOutputOptions(LintReportOutputFormat.json, out);

        executable.setOutputOptions(outputOptions);
        executable.setSchemaCrawlerOptions(options);
        executable.setConnection(connection);
        executable.execute();

        File output = new File(out.toString());
        String data = IOUtils.toString(new FileInputStream(output));
        Assert.assertNotNull(data);
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(data.substring(0, data.length()), JsonObject.class);
        List<LintWrapper>lints = new ArrayList<>();
        JsonObject tableLintObject;
        if (json.get("lints") instanceof JsonObject) {

            Assert.assertNotNull(json.getAsJsonObject("lints"));
            JsonArray jsonLints = json.getAsJsonObject("lints").getAsJsonArray("lints");
            Assert.assertNotNull(jsonLints);

            if (options.getLimitOptions().getTableNamePattern() != null && !options.getLimitOptions().getTableNamePattern().isEmpty())
                Assert.assertEquals(options.getLimitOptions().getTableNamePattern(), json.getAsJsonObject("table_lints").get("name").getAsString());

            for (JsonElement lint : jsonLints) {
                String tableName = json.getAsJsonObject("table_lints").get("name").getAsString();
                if (!Objects.equals("databasechangelog", tableName) && !Objects.equals("databasechangeloglock", tableName))
                    lints.add(createLintWrapper(json.getAsJsonObject("table_lints").get("name").getAsString(), lint.getAsJsonObject()));
            }
        } else {
            Assert.assertNotNull(json.getAsJsonArray("lints"));
            JsonArray jsonTableLints = json.getAsJsonArray("lints");

            for (JsonElement tableLint : jsonTableLints) {
                tableLintObject = tableLint.getAsJsonObject();

                if (options.getLimitOptions().getTableNamePattern() != null && !options.getLimitOptions().getTableNamePattern().isEmpty())
                    Assert.assertTrue(tableLintObject.get("object-name").getAsString().contains(options.getLimitOptions().getTableNamePattern()));

                String tableName = tableLintObject.get("object-name").getAsString();
                if (!Objects.equals("databasechangelog", tableName) && !Objects.equals("databasechangeloglock", tableName))
                    lints.add(createLintWrapper(tableLintObject.get("object-name").getAsString(), tableLintObject.getAsJsonObject()));
                LOGGER.info("lints added:"+ lints);
            }
        }

        return lints;
    }

    private LintWrapper createLintWrapper(String tableName, JsonObject jsonLint){

        LintWrapper lint = new LintWrapper();
        lint.setId(jsonLint.get("linter-id").getAsString());
        Assert.assertNotNull(lint.getId());
        lint.setValue(jsonLint.get("value").getAsString().trim());
        Assert.assertNotNull(lint.getValue());
        lint.setDescription(jsonLint.get("message").getAsString().trim());
        Assert.assertNotNull(lint.getDescription());
        lint.setSeverity(jsonLint.get("severity").getAsString().trim());
        Assert.assertNotNull(lint.getSeverity());
        lint.setTableName(tableName);

        return lint;
    }
}
