/*
 * ImageServlet.java
 * Streams menu and upload images from the webapp image folder to the browser.
 */

package com.restaurant.controller; // Controller package for servlet endpoints

import jakarta.servlet.annotation.WebServlet; // Maps this class to /uploads/*
import jakarta.servlet.http.HttpServlet; // Base class for HTTP request handling
import jakarta.servlet.http.HttpServletRequest; // Incoming request with path after /uploads/
import jakarta.servlet.http.HttpServletResponse; // Outgoing image bytes and status codes

import java.io.File; // Resolve image files on disk
import java.io.IOException; // Thrown on stream or path errors
import java.io.OutputStream; // Writes image bytes to the response
import java.nio.file.Files; // Copies file and probes MIME type

// This servlet serves images from the project's images folder
@WebServlet("/uploads/*") // URL pattern: anything under /uploads/ is handled here
public class ImageServlet extends HttpServlet { // Serves static image files safely

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException { // GET only; no JSP forward

        // Get the filename from URL
        // Example: /uploads/steam-momo.jpg -> filename = steam-momo.jpg
        String filename = request.getPathInfo(); // Path segment after /uploads
        if (filename == null || filename.equals("/")) { // No file name in URL
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404 for empty path
            return;
        }

        // Remove the leading slash
        filename = filename.substring(1); // e.g. /steam-momo.jpg -> steam-momo.jpg

        // Get the path to images folder inside the project
        // This works on any computer because it uses the project location
        String uploadPath = getServletContext().getRealPath("/image"); // Physical /image directory
        File file = new File(uploadPath, filename); // Target file under that folder

        // Check if file exists
        if (!file.exists() || !file.isFile()) { // Missing or is a directory
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404 when not found
            return;
        }

        // Security check - make sure file is inside images folder
        if (!file.getCanonicalPath().startsWith(new File(uploadPath).getCanonicalPath())) { // Block path traversal
            response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403 if outside allowed dir
            return;
        }

        // Set the content type (image/jpeg, image/png, etc.)
        String contentType = Files.probeContentType(file.toPath()); // Guess MIME from file content
        if (contentType != null) { // Only set header when probe succeeds
            response.setContentType(contentType); // Tell browser how to render the bytes
        }

        // Set the file length
        response.setContentLengthLong(file.length()); // Helps clients show download progress

        // Send the image to browser
        try (OutputStream out = response.getOutputStream()) { // Auto-close stream when done
            Files.copy(file.toPath(), out); // Stream file bytes to the client
        }
    }
}
