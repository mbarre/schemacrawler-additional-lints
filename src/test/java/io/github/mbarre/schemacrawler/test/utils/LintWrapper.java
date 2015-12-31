package io.github.mbarre.schemacrawler.test.utils;

/**
 * Created by barmi83 on 24/12/15.
 */
public class LintWrapper {

    private String id;
    private String value;
    private String description;
    private String severity;
    private String tableName;

    public LintWrapper(){
        super();
    }

    public LintWrapper(String id, String value, String description, String severity, String tableName) {
        this.id = id;
        this.value = value;
        this.description = description;
        this.severity = severity;
        this.tableName = tableName;
    }

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "LintWrapper[id="+id+", value="+value+", description="+description+", severity="+severity+", table="+tableName+"]";
    }
}
