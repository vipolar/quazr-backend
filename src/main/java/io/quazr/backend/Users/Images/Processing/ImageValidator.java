package io.quazr.backend.Users.Images.Processing;

import java.io.InputStream;
import java.io.IOException;

public class ImageValidator {
    public static String validateExtension(InputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            throw new IllegalArgumentException("InputStream must support mark/reset");
        }

        try {
            byte[] header = new byte[12];
            inputStream.mark(12);
            int bytesRead = inputStream.read(header);

            if (bytesRead < 3) {
                throw new IllegalArgumentException("Uploaded file is too small to determine format");
            }

            // Check PNG (8 bytes: 89 50 4E 47 0D 0A 1A 0A)
            if (bytesRead >= 8 && header[0] == (byte) 0x89 && header[1] == 0x50 && header[2] == 0x4E && header[3] == 0x47 &&header[4] == 0x0D && header[5] == 0x0A && header[6] == 0x1A && header[7] == 0x0A) {
                return "png";
            }

            // Check JPEG variants (FF D8 FF + one of: DB, E0, E1, EE)
            if (bytesRead >= 4 && header[0] == (byte) 0xFF && header[1] == (byte) 0xD8 && header[2] == (byte) 0xFF) {
                if (header[3] == (byte) 0xDB || header[3] == (byte) 0xE0 || header[3] == (byte) 0xE1 || header[3] == (byte) 0xEE) {
                    return "jpeg";
                }
            }

            // Check HEIC/HEIF (ftyp format with heic or heix at bytes 4-7)
            if (bytesRead >= 12 && header[4] == 0x66 && header[5] == 0x74 && header[6] == 0x79 && header[7] == 0x70) {
                if (header[8] == 0x68 && header[9] == 0x65 && header[10] == 0x69 && (header[11] == 0x63 || header[11] == 0x78)) {
                    return "heic";
                }
            }

            // Check for WebP (RIFF at bytes 0-4 and WEBP at bytes 8-11)
            if (bytesRead >= 12 && header[0] == 0x52 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x46) {
                if (header[8] == 0x57 && header[9] == 0x45 && header[10] == 0x42 && header[11] == 0x50) {
                    return "webp";
                }
            }

            return null; //TODO: add logging and log the invalid format attempts
        } catch (Exception e) {
            throw new IOException("Failed to read image header for validation", e);
        } finally {
            try {
                inputStream.reset();
            } catch (IOException e) {
                throw new IOException("Failed to reset InputStream after validation", e);
            }
        }
    }
}