package com.fad.fad.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fad.fad.domain.SignatureProcesses;

@Service
public interface SignatureProcessesService {
    public SignatureProcesses save(SignatureProcesses signatureProcesses);

    public List<SignatureProcesses> findAll();

    public SignatureProcesses findByRequisition_id(String requisitionId);
}
