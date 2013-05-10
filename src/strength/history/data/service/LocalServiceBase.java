package strength.history.data.service;

import strength.history.data.db.DBHelperBase;
import strength.history.data.structure.Base;

/**
 * Local base class for the service framework
 * 
 * @param <E>
 *            Structure type it provides
 * @param <D>
 *            DBHelper class
 */
public abstract class LocalServiceBase<E extends Base<E>, D extends DBHelperBase<E>>
		extends ServiceBase<E> {
	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public LocalServiceBase(String name) {
		super(name);
	}

	protected abstract D getDB();
}
