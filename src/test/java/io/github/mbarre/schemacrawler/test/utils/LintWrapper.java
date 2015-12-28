package io.github.mbarre.schemacrawler.test.utils;

/**
 * Created by barmi83 on 24/12/15.
 */
public class LintWrapper {

    private String id;
    private String value;
    private String description;
    private String severity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
