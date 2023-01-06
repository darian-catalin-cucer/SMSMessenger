package cucerdariancatalin.sms_messenger.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.simplemobiletools.commons.extensions.notificationManager
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import cucerdariancatalin.sms_messenger.extensions.conversationsDB
import cucerdariancatalin.sms_messenger.extensions.markThreadMessagesRead
import cucerdariancatalin.sms_messenger.extensions.updateUnreadCountBadge
import cucerdariancatalin.sms_messenger.helpers.MARK_AS_READ
import cucerdariancatalin.sms_messenger.helpers.THREAD_ID
import cucerdariancatalin.sms_messenger.helpers.refreshMessages

class MarkAsReadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            MARK_AS_READ -> {
                val threadId = intent.getLongExtra(THREAD_ID, 0L)
                context.notificationManager.cancel(threadId.hashCode())
                ensureBackgroundThread {
                    context.markThreadMessagesRead(threadId)
                    context.conversationsDB.markRead(threadId)
                    context.updateUnreadCountBadge(context.conversationsDB.getUnreadConversations())
                    refreshMessages()
                }
            }
        }
    }
}
