package com.fad.fad.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.fad.fad.domain.SignatureProcesses;

public interface SignatureProcessesDao extends CrudRepository<SignatureProcesses, UUID> {
    List<SignatureProcesses> findAll();

    // @Query("Select * from signature_processes")
    // SignatureProcesses findByRequisitionId();
}