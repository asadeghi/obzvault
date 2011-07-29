package obzvault;

/**
 *
 * @author duncan
 */
public class OBZVaultFileTypeException extends Exception {

    private Integer _nExpectedVersion;
    private Integer _nActualVersion;

    public Integer getExpectedVersion() {
        return _nExpectedVersion;
    }

    public Integer getActualVersion() {
        return _nActualVersion;
    }

    public OBZVaultFileTypeException(Integer expectedVersion, Integer actualVersion) {
        _nExpectedVersion = expectedVersion;
        _nActualVersion = actualVersion;
    }
}
