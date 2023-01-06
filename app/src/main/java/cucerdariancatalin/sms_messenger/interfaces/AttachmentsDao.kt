package cucerdariancatalin.sms_messenger.interfaces

import androidx.room.Dao
import androidx.room.Query
import cucerdariancatalin.sms_messenger.models.Attachment

@Dao
interface AttachmentsDao {
    @Query("SELECT * FROM attachments")
    fun getAll(): List<Attachment>
}
