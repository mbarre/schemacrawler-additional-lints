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

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.tools.executable.SchemaCrawlerExecutable;
import schemacrawler.tools.lint.executable.LintOptionsBuilder;
import schemacrawler.tools.options.OutputOptions;
import schemacrawler.tools.options.OutputOptionsBuilder;
import schemacrawler.tools.options.TextOutputFormat;

/**
 * Created by barmi83 on 24/12/15.
 */
public abstract class BaseLintTest {

    private static final Logger LOGGER = Logger.getLogger(BaseLintTest.class.getName());


    protected List<LintWrapper> executeToJsonAndConvertToLintList(String linterName, SchemaCrawlerOptions options, Connection connection)  throws Exception{

        final SchemaCrawlerExecutable executable = new SchemaCrawlerExecutable("lint");

        final Path linterConfigsFile = FileSystems.getDefault().getPath("", this.getClass().getClassLoader().getResource(linterName + "/schemacrawler-linter-configs-test.xml").getPath());
        final LintOptionsBuilder optionsBuilder = LintOptionsBuilder.builder();
        optionsBuilder.withLinterConfigs(linterConfigsFile.toString());
        executable.setAdditionalConfiguration(optionsBuilder.toConfig());

        Path out = Paths.get("target/test_"+this.getClass().getSimpleName()+".json");
        OutputOptions outputOptions = OutputOptionsBuilder.builder().newOutputOptions(TextOutputFormat.json, out);

        executable.setOutputOptions(outputOptions);
        executable.setSchemaCrawlerOptions(options);
        executable.setConnection(connection);
        executable.execute();

        File output = new File(out.toString());
        String data = IOUtils.toString(new FileInputStream(output));
        Assert.assertNotNull(data);
		JSONObject json = new JSONObject(data.substring(1, data.length() - 2));

		List<LintWrapper>lints = new ArrayList<>();

        try {
            if (json.get("table_lints") instanceof JSONObject) {

                Assert.assertNotNull(json.getJSONObject("table_lints"));
                JSONArray jsonLints = json.getJSONObject("table_lints").getJSONArray("lints");
                Assert.assertNotNull(jsonLints);

                if (options.getTableNamePattern() != null && !options.getTableNamePattern().isEmpty())
                    Assert.assertEquals(options.getTableNamePattern(), json.getJSONObject("table_lints").getString("name"));

                String tableName;
                for (int i = 0; i < jsonLints.length(); i++) {
                    tableName = json.getJSONObject("table_lints").getString("name");
                    if (!Objects.equals("databasechangelog", tableName) && !Objects.equals("databasechangeloglock", tableName))
                        lints.add(createLintWrapper(json.getJSONObject("table_lints").getString("name"), jsonLints.getJSONObject(i)));
                }
            } else {
                Assert.assertNotNull(json.getJSONArray("table_lints"));
                JSONArray jsonTableLints = json.getJSONArray("table_lints");

                for (int i = 0; i < jsonTableLints.length(); i++) {
                    JSONArray jsonLints = jsonTableLints.getJSONObject(i).getJSONArray("lints");
                    Assert.assertNotNull(jsonLints);

                    if (options.getTableNamePattern() != null && !options.getTableNamePattern().isEmpty())
                        Assert.assertEquals(options.getTableNamePattern(), json.getJSONObject("table_lints").getString("name"));

                    String tableName;
                    for (int j = 0; j < jsonLints.length(); j++) {
                        tableName = jsonTableLints.getJSONObject(i).getString("name");
                        if (!Objects.equals("databasechangelog", tableName) && !Objects.equals("databasechangeloglock", tableName))
                            lints.add(createLintWrapper(jsonTableLints.getJSONObject(i).getString("name"), jsonLints.getJSONObject(j)));
                    }
                }
            }
        }catch(JSONException e){
            LOGGER.log(Level.WARNING, "No lint detected.");
        }

        return lints;
    }

    private LintWrapper createLintWrapper(String tableName, JSONObject jsonLint){

        LintWrapper lint = new LintWrapper();
        lint.setId(jsonLint.getString("id"));
        Assert.assertNotNull(lint.getId());
        lint.setValue(jsonLint.getString("value").trim());
        Assert.assertNotNull(lint.getValue());
        lint.setDescription(jsonLint.getString("description").trim());
        Assert.assertNotNull(lint.getDescription());
        lint.setSeverity(jsonLint.getString("severity").trim());
        Assert.assertNotNull(lint.getSeverity());
        lint.setTableName(tableName);

        return lint;
    }
}
