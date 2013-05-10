package strength.history.data;

import strength.history.data.service.ServiceBase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Handles messages for the data provider
 */
public class MessageHandler extends Handler {
	DataProvider dataProvider;

	/**
	 * Constructs a new handler
	 * 
	 * @param dataProvider
	 */
	public MessageHandler(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	@Override
	public void handleMessage(Message msg) {
		ServiceBase.Service service = ServiceBase.Service.parse(msg.arg1);
		ServiceBase.Request request = ServiceBase.Request.parse(msg.arg2);
		boolean ok = msg.what == 1;
		Log.d("MessageHandler", "service=" + service + " request=" + request
				+ " ok=" + ok);
		switch (service) {
		case EXERCISE:
			// TODO
			break;
		case WEIGHT:
			dataProvider.getWeightProvider().handleCallback(request, msg.obj,
					ok, dataProvider.getDataListener());
			break;
		}
	}
}
