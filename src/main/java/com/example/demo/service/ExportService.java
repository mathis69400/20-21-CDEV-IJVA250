package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Writer;

/**
 * Service contenant les actions métiers liées aux exports.
 */
@Service
@Transactional
public class ExportService {

    private ClientService clientService;
    private FactureService factureService;

    public ExportService(ClientService clientService, FactureService factureService) {
        this.clientService = clientService;
        this.factureService = factureService;
    }

    public void clientsCSV(Writer writer) {
        //TODO
    }

}
