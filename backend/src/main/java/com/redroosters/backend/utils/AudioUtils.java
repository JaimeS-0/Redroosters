package com.redroosters.backend.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

// Obtiene la duración de una cancion con ffprobe y formatea en minutos y segundos

public final class AudioUtils {

    private AudioUtils() {}

    public static int getAudioDuracionSegundos(Path filePath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                filePath.toAbsolutePath().toString()
        );
        Process p = pb.start();
        try (var reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String out = reader.readLine(); 
            p.waitFor();
            double seconds = Double.parseDouble(out);
            return (int) Math.round(seconds);
        }
    }

    public static String formatMmSs(int totalSeconds) {
        int m = totalSeconds / 60;
        int s = totalSeconds % 60;
        return String.format("%02d:%02d", m, s);
    }
}

