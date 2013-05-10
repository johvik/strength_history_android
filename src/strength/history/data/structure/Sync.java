package strength.history.data.structure;

/**
 * Constants used for sync
 */
public interface Sync {
	/**
	 * Item is new
	 */
	public static final int NEW = 0;
	/**
	 * Item is old
	 */
	public static final int OLD = 1;
	/**
	 * Item is updated
	 */
	public static final int UPDATE = 2;
	/**
	 * Item is deleted
	 */
	public static final int DELETE = 3;
}
