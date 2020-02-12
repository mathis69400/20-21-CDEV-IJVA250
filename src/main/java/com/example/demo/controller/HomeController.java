package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.service.impl.ClientServiceImpl;
import com.example.demo.service.ArticleService;
import com.example.demo.service.FactureService;
import org.apache.http.HttpResponse;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.openqa.selenium.remote.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    @GetMapping("/articles/csv")
    public void ArticlesCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition","attachment; filename=\"export-articles.csv\"");
        PrintWriter writer = response.getWriter();
        List<Article> lstArticles = articleService.findAll();
        writer.println("Article"+";"+"Prix");
        for(int i=0;i<lstArticles.size();i++)
        {
            writer.println(lstArticles.get(i).getLibelle()+";"+lstArticles.get(i).getPrix());
        }
    }

    @GetMapping("/clients/csv")
    public void ClientsCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition","attachment; filename=\"export-clients.csv\"");
        PrintWriter writer = response.getWriter();
        List<Client> lstClients = clientServiceImpl.findAllClients();
        LocalDate Now = LocalDate.now();
        writer.println("Nom"+";"+"Prénom"+";"+"Age");

        for(int i=0;i<lstClients.size();i++)
        {
            Period period = Period.between(lstClients.get(i).getDateNaissance(),LocalDate.now());
            writer.println(lstClients.get(i).getNom()+";"+lstClients.get(i).getPrenom()+";"+period.getYears());
        }
    }

    @GetMapping("/articles/xlsx")
    public void ArticlesXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"articles.xlsx\"");

        List<Article> lstArticles = articleService.findAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Articles");

        int rownum = 0;
        Cell cell;
        Row row;

        row = sheet.createRow(rownum);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Articles");

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Prix");

        for(int i=0;i<lstArticles.size();i++)
        {
            rownum++;
            row = sheet.createRow(rownum);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(lstArticles.get(i).getLibelle());

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(lstArticles.get(i).getPrix());
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/clients/xlsx")
    public void ClientsXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.xlsx\"");

        List<Client> lstClients = clientServiceImpl.findAllClients();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Clients");

        int rownum = 0;
        Cell cell;
        Row row;

        row = sheet.createRow(rownum);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Nom");

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Prénom");

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Âge");

        for(int i=0;i<lstClients.size();i++)
        {
            rownum++;
            row = sheet.createRow(rownum);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(lstClients.get(i).getNom());

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(lstClients.get(i).getPrenom());

            Period period = Period.between(lstClients.get(i).getDateNaissance(),LocalDate.now());
            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(String.valueOf(period.getYears()));
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/factures/xlsx")
    public void FacturesXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"factures.xlsx\"");

        List<Client> lstClients = clientServiceImpl.findAllClients();

        XSSFWorkbook workbook = new XSSFWorkbook();

        for (int i = 0; i < lstClients.size(); i++) {
            List<Facture> lstFactures = factureService.findAllFactures();
            XSSFSheet sheet = workbook.createSheet(lstClients.get(i).getNom() + " " + lstClients.get(i).getPrenom());

            int rownum = 0;
            Cell cell;
            Row row;

            row = sheet.createRow(rownum);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Nom :");

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("Prénom");
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
