package xyz.dsemikin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        final Path dirPath = Paths.get("G:\\input");
        renameImageFiles(dirPath);

    }

    private static void renameImageFiles(final Path dirPath) {
        try {
            try (final Stream<Path> fileList = Files.list(dirPath)) {
                fileList.filter(path -> {
                            final String fileName = path.getFileName().toString();
                            final String fileExtension = fileName.substring(fileName.length() - 4);
                            return ".jpg".equalsIgnoreCase(fileExtension);
                        })
                        .forEach(path -> {
                            final Path parent = path.getParent();
                            final Path newPath = parent.resolve(generateNewFileName(path));
                            try {
                                Files.move(path, newPath);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path generateNewFileName(final Path path) {
        final BasicFileAttributes fileAttributes;
        try {
            fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final Instant lastModifiedTime = fileAttributes.lastModifiedTime().toInstant();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd_HHmmss").withZone(ZoneId.systemDefault());
        return Paths.get("IMG_" + formatter.format(lastModifiedTime));
    }
}