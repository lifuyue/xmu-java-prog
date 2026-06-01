import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class FileService {
    private FileService() {
    }

    public static String open(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    public static void save(Path path, String content) throws IOException {
        Files.writeString(
                path,
                content,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
    }

    public static File withTxtExtension(File file) {
        String name = file.getName();
        if (name.toLowerCase().endsWith(".txt")) {
            return file;
        }
        File parent = file.getParentFile();
        return new File(parent == null ? new File(".") : parent, name + ".txt");
    }
}
