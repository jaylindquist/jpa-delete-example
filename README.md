# jpa-delete-example
Using Spring Data to merge entities and delete conflicting entities

When attempting to merge two database entities together with constraints,
you will get a unique constraint error when attempting the following:

```java
@Override
@Transactional
public void run(String... args) throws Exception {
    log.info("Finding all entities");
    List<DataEntity> values = repo.findAll();
  
    DataEntity toCombine = values.get(0);
    DataEntity toDelete = values.get(1);

    toCombine.setFirst(toCombine.getFirst());
    toCombine.setLast(toDelete.getLast());
    toCombine.setMeta("Merged");

    log.info("Delete - the following will fail");
    repo.delete(toDelete);

    log.info("Update");
    repo.save(toCombine);
}
```

If `first` and `last` are contrained to be unique in the database, then the call to `repo.delete(...)` here will fail.

Spring Data / Hibernate / JPA will attempt to update the `toCombine` object before deleting `toDelete` dispite what the 
code appears to be doing. This will fail the unique contraint as `toCombine.last` has the same value as `toDelete.last`.

To fix this, we need to create a new service, force a new transaction, call delete, flush all entities, then update.

```java
@Override
@Transactional
public void run(String... args) throws Exception {
    log.info("Finding all entities");
    List<DataEntity> values = repo.findAll();

    DataEntity toCombine = values.get(0);
    DataEntity toDelete = values.get(1);
    
    toCombine.setFirst(toCombine.getFirst());
    toCombine.setLast(toDelete.getLast());
    toCombine.setMeta("Merged");

    service.commit(toCombine, toDelete);
}

@Service
public class DataService {
    private static final Logger log = LoggerFactory.getLogger(DataService.class);

    @Autowired
    private DataEntityRepo repo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void commit(DataEntity toUpdate, DataEntity toDelete) {
        log.info("Delete");
        repo.delete(toDelete);

        repo.flush();

        log.info("Update");
        repo.save(toUpdate);
    }
}
```
