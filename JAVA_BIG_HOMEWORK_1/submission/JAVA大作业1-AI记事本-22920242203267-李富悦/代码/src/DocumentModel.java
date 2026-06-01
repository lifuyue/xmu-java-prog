import java.nio.file.Path;

public class DocumentModel {
    private Path currentFile;
    private boolean modified;

    public Path getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(Path currentFile) {
        this.currentFile = currentFile;
    }

    public boolean hasCurrentFile() {
        return currentFile != null;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public void reset() {
        currentFile = null;
        modified = false;
    }

    public String getDisplayName() {
        if (currentFile == null) {
            return "无标题";
        }
        Path fileName = currentFile.getFileName();
        return fileName == null ? currentFile.toString() : fileName.toString();
    }
}
