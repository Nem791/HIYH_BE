package com.example.demo.services;

import com.example.demo.exceptions.PdfProcessingException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
public class PdfService {

    public String extractTextFromPdf(MultipartFile file) {
        if (file.isEmpty() || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(".pdf")) {
            throw new PdfProcessingException("Please upload a PDF file.");
        }
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            return text.replace("\r\n", "\n").replace("\r", "\n").replace("\n", "\\n");
        } catch (IOException e) {
            throw new PdfProcessingException("Could not process PDF", e);
        }
    }
}
