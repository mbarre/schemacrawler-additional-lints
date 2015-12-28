package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.tools.executable.Executable;
import schemacrawler.tools.executable.SchemaCrawlerExecutable;
import schemacrawler.tools.lint.executable.LintOptionsBuilder;
import schemacrawler.tools.options.OutputOptions;
import schemacrawler.tools.options.TextOutputFormat;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by barmi83 on 24/12/15.
 */
public abstract class BaseLintTest {

    protected List<LintWrapper> executeToJsonAndConvertToLintList(SchemaCrawlerOptions options, Connection connection) throws Exception {

        final Executable executable = new SchemaCrawlerExecutable("lint");
        final Path linterConfigsFile = FileSystems.getDefault().getPath("", this.getClass().getClassLoader().getResource("schemacrawler-linter-configs-test.xml").getPath());
        final LintOptionsBuilder optionsBuilder = new LintOptionsBuilder();
        optionsBuilder.withLinterConfigs(linterConfigsFile.toString());
        executable.setAdditionalConfiguration(optionsBuilder.toConfig());

        Path out = Paths.get("target/test_"+this.getClass().getSimpleName()+".json");
        OutputOptions outputOptions = new OutputOptions(TextOutputFormat.json, out);
        outputOptions.setOutputFile(Paths.get("target/test_"+this.getClass().getSimpleName()+".json"));

        executable.setOutputOptions(outputOptions);
        executable.setSchemaCrawlerOptions(options);
        executable.execute(connection);

        File output = new File(out.toString());
        String data = IOUtils.toString(new FileInputStream(output));
        Assert.assertNotNull(data);
        JSONObject json = new JSONObject(data.toString().substring(1, data.toString().length() - 2));

        Assert.assertNotNull(json.getJSONObject("table_lints"));
        JSONArray jsonLints = json.getJSONObject("table_lints").getJSONArray("lints");
        Assert.assertNotNull(jsonLints);

        if(options.getTableNamePattern() != null  && ! options.getTableNamePattern().isEmpty())
            Assert.assertEquals(options.getTableNamePattern(), json.getJSONObject("table_lints").getString("name"));

        List<LintWrapper>lints = new ArrayList<LintWrapper>();
        LintWrapper lint = null;
        for (int i = 0; i < jsonLints.length(); i++) {
            lint = new LintWrapper();
            lint.setId(jsonLints.getJSONObject(i).getString("id"));
            Assert.assertNotNull(lint.getId());
            lint.setValue(jsonLints.getJSONObject(i).getString("value").trim());
            Assert.assertNotNull(lint.getValue());
            lint.setDescription(jsonLints.getJSONObject(i).getString("description").trim());
            Assert.assertNotNull(lint.getDescription());
            lint.setSeverity(jsonLints.getJSONObject(i).getString("severity").trim());
            Assert.assertNotNull(lint.getSeverity());

            lints.add(lint);
        }
        return lints;
    }
}
