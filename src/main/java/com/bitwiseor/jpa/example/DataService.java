package com.bitwiseor.jpa.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
