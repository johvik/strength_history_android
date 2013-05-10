package strength.history.data.structure;

public abstract class Sync {
	public static final int NEW = 0;
	public static final int OLD = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;

	private Sync() {
	}
}
