package strength.history.data.provider;

import strength.history.data.service.ServiceBase;
import strength.history.data.service.SyncService;
import strength.history.data.service.SyncService.Request;
import android.content.Context;
import android.content.Intent;
import android.os.Messenger;

public class SyncProvider {
	public interface Provides {
		public void sync(Context context);
	}

	@SuppressWarnings("static-method")
	public final void sync(Context context, Messenger messenger) {
		if (context != null) {
			Intent intent = new Intent(context, SyncService.class);
			intent.putExtra(ServiceBase.REQUEST, Request.SYNC.ordinal());
			intent.putExtra(ServiceBase.MESSENGER, messenger);
			context.startService(intent);
		}
	}
}
