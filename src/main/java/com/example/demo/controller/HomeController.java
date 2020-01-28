package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.service.impl.ClientServiceImpl;
import com.example.demo.service.ArticleService;
import com.example.demo.service.FactureService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Controller principale pour affichage des clients / factures sur la page d'acceuil.
 */
@Controller
public class HomeController {

    private ArticleService articleService;
    private ClientServiceImpl clientServiceImpl;
    private FactureService factureService;

    public HomeController(ArticleService articleService, ClientServiceImpl clientService, FactureService factureService) {
        this.articleService = articleService;
        this.clientServiceImpl = clientService;
        this.factureService = factureService;
    }

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");

        List<Article> articles = articleService.findAll();
        modelAndView.addObject("articles", articles);

        List<Client> toto = clientServiceImpl.findAllClients();
        modelAndView.addObject("clients", toto);

        List<Facture> factures = factureService.findAllFactures();
        modelAndView.addObject("factures", factures);

        return modelAndView;
    }
}
