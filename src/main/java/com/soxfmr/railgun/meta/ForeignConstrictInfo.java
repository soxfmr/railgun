package com.soxfmr.railgun.meta;

public class ForeignConstrictInfo {
    private String references;
    private String relateTable;

    public ForeignConstrictInfo(String references, String relateTable) {
        this.references = references;
        this.relateTable = relateTable;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getRelateTable() {
        return relateTable;
    }

    public void setRelateTable(String relateTable) {
        this.relateTable = relateTable;
    }
}
