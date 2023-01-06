package cucerdariancatalin.sms_messenger.receivers

import android.app.Activity
import android.content.Context
import android.content.Intent
import cucerdariancatalin.sms_messenger.helpers.refreshMessages

class MmsSentReceiver : com.klinker.android.send_message.MmsSentReceiver() {
    override fun onMessageStatusUpdated(context: Context?, intent: Intent?, resultCode: Int) {
        super.onMessageStatusUpdated(context, intent, resultCode)
        if (resultCode == Activity.RESULT_OK) {
            refreshMessages()
        }
    }
}
