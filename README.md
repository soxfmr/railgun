## RailGun
An ORM Library implements for Android. It support the SQLite database now and can be expand to support for more database.

### Getting Start

Define your modules which are extends from *com.soxfmr.railgun.Model* in a specific package, suppose there're under the *com.example.model* package:
```java
@DatabaseTable(tableName = "notebooks", priority = 99)
public class Notebook extends Model {

    @DatabaseField(field = "name", type = SQLiteDataType.TYPE_STRING)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

```java
@DatabaseTable(tableName = "words")
public class Word extends Model {

    @DatabaseField(field = "word", type = SQLiteDataType.TYPE_STRING)
    private String word;

    @DatabaseField(field = "notebook_id", type = SQLiteDataType.TYPE_INTEGER,
        foreign = true, references = PRIMARY_KEY)
    private Notebook notebook;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNotebook(Model notebook) {
        this.notebook = (Notebook) notebook;
    }

    public Notebook getNotebook() {
        return notebook;
    }
}
```

### Initialize the database and tables
```java
SQLiteScheme sqLiteScheme = new SQLiteScheme();
SQLiteDatabase sqLiteDb = sqLiteScheme.init(getApplicationContext(),
            "railgun.db", 1, "com.example.model");

SQLiteDaoImpl<Notebook> notebookDao = new SQLiteDaoImpl<>(Notebook.class);
Notebook notebook = new Notebook();
notebook.setName("Demo");
notebookDao.insert(notebook);
```
