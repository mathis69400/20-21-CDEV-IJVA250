package com.example.demo.service;

import com.example.demo.entity.Facture;
import com.example.demo.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service contenant les actions métiers liées aux factures.
 */
@Service
@Transactional
public class FactureService {

    private FactureRepository factureRepository;

    public FactureService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    public List<Facture> findAllFactures() {
        return factureRepository.findAll();
    }
}
