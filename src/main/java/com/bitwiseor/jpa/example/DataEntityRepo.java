package com.bitwiseor.jpa.example;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataEntityRepo extends JpaRepository<DataEntity, Long> {
}
