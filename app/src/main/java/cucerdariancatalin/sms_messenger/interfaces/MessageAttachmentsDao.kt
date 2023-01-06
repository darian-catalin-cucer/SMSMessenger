package cucerdariancatalin.sms_messenger.interfaces

import androidx.room.Dao
import androidx.room.Query
import cucerdariancatalin.sms_messenger.models.MessageAttachment

@Dao
interface MessageAttachmentsDao {
    @Query("SELECT * FROM message_attachments")
    fun getAll(): List<MessageAttachment>
}
