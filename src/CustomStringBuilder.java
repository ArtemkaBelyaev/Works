import java.util.*;

public class CustomStringBuilder {
    private StringBuilder sb;
    private Stack<String> snapshots;

    public CustomStringBuilder() {
        this.sb = new StringBuilder();
        this.snapshots = new Stack<>();
    }

    private void saveSnapshot() {
        snapshots.push(sb.toString());
    }

    public void append(String s) {
        saveSnapshot();
        sb.append(s);
    }

    public void insert(int offset, String s) {
        saveSnapshot();
        sb.insert(offset, s);
    }

    public void delete(int start, int end) {
        saveSnapshot();
        sb.delete(start, end);
    }
    public void undo() {
        if (!snapshots.isEmpty()) {
            sb = new StringBuilder(snapshots.pop());
        }
    }
}