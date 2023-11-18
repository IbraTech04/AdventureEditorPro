package AdventureModel;

/**
 * @param direction The direction of this connection
 * @param lock The object required to pass through this connection (null if none required)
 */
public record Connection(String direction, String lock) {
    public Connection(String direction) {
        this(direction, null);
    }
}
