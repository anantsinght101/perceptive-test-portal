package com.beproject.perceptivetestportal.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GenerateQuestionService {

    // ✅ Configuration: Update these based on your environment
    private final String ML_PROJECT_DIR = "C:\\Users\\prath\\OneDrive\\Desktop\\FInal_Year_Project\\perceptive-portal-ml";
    private final String OUTPUT_PATH = ML_PROJECT_DIR + "\\outputs\\mcqs.json";
    private final String PYTHON_EXE = "python"; // or "python3" depending on your PATH

    /**
     * Triggers the Python AI Pipeline to generate MCQs from a PDF.
     */
    public String generateFromPdf(MultipartFile pdf, String subject, int num, String mode) throws Exception {
    File tempPdf = File.createTempFile("ai_input_", ".pdf");
    
    try {
        Files.copy(pdf.getInputStream(), tempPdf.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // 1. Map frontend modes (easy/medium/hard) to Python modes (none/fast/full)
        String pythonMode = "fast"; // default
        if ("easy".equalsIgnoreCase(mode)) pythonMode = "none";
        else if ("medium".equalsIgnoreCase(mode)) pythonMode = "fast";
        else if ("hard".equalsIgnoreCase(mode)) pythonMode = "full";

        // 2. Generate a unique doc_id (Python requires this)
        String docId = "DOC_" + System.currentTimeMillis();

        // 3. Build the command exactly as Python expects it
        ProcessBuilder pb = new ProcessBuilder(
                PYTHON_EXE,
                "qg_cli.py",
                "--pdf", tempPdf.getAbsolutePath(),
                "--doc_id", docId,             // ✅ Added Required Argument
                "--num", String.valueOf(num),
                "--mode", pythonMode,          // ✅ Corrected Choice Mapping
                "--out", "outputs/mcqs.json"
        );

        pb.directory(new File(ML_PROJECT_DIR));
        pb.redirectErrorStream(true);

        Process process = pb.start();
        
        // Log the output to IntelliJ console so you can see the "GPU CHECK" print statements
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[PYTHON AI]: " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Python AI Script failed with exit code: " + exitCode);
        }

        File resultFile = new File(OUTPUT_PATH);
        return Files.readString(resultFile.toPath());

    } finally {
        if (tempPdf.exists()) tempPdf.delete();
    }
}
}