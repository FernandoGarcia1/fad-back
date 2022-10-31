package com.fad.fad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fad.fad.dao.SignatureProcessesDao;
import com.fad.fad.domain.SignatureProcesses;

@Service
public class SignatureProcessesServiceImp implements SignatureProcessesService {

    @Autowired
    SignatureProcessesDao signatureProcessesDao;

    @Override
    public SignatureProcesses save(SignatureProcesses signatureProcesses) {
        SignatureProcesses save = signatureProcessesDao.save(signatureProcesses);
        return save;

    }

    @Override
    public List<SignatureProcesses> findAll() {
        return signatureProcessesDao.findAll();
    }

    @Override
    public SignatureProcesses findByRequisition_id(String requisitionId) {
        // TODO Auto-generated method stub
        // return signatureProcessesDao.findByRequisition_id(requisitionId);
        return null;
    }

}
