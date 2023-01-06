package cucerdariancatalin.sms_messenger.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.simplemobiletools.commons.extensions.normalizePhoneNumber
import com.simplemobiletools.commons.extensions.sendEmailIntent
import com.simplemobiletools.commons.helpers.NavigationIcon
import cucerdariancatalin.sms_messenger.R
import cucerdariancatalin.sms_messenger.adapters.VCardViewerAdapter
import cucerdariancatalin.sms_messenger.extensions.dialNumber
import cucerdariancatalin.sms_messenger.helpers.EXTRA_VCARD_URI
import cucerdariancatalin.sms_messenger.helpers.parseVCardFromUri
import cucerdariancatalin.sms_messenger.models.VCardPropertyWrapper
import cucerdariancatalin.sms_messenger.models.VCardWrapper
import ezvcard.VCard
import ezvcard.property.Email
import ezvcard.property.Telephone
import kotlinx.android.synthetic.main.activity_vcard_viewer.*

class VCardViewerActivity : SimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vcard_viewer)

        updateMaterialActivityViews(vcard_viewer_coordinator, contacts_list, useTransparentNavigation = true, useTopSearchMenu = false)
        setupMaterialScrollListener(contacts_list, vcard_toolbar)

        val vCardUri = intent.getParcelableExtra(EXTRA_VCARD_URI) as? Uri
        if (vCardUri != null) {
            setupOptionsMenu(vCardUri)
            parseVCardFromUri(this, vCardUri) {
                runOnUiThread {
                    setupContactsList(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(vcard_toolbar, NavigationIcon.Arrow)
    }

    private fun setupOptionsMenu(vCardUri: Uri) {
        vcard_toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_contact -> {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        val mimetype = contentResolver.getType(vCardUri)
                        setDataAndType(vCardUri, mimetype?.lowercase())
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    startActivity(intent)
                }
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun setupContactsList(vCards: List<VCard>) {
        val items = prepareData(vCards)
        val adapter = VCardViewerAdapter(this, items.toMutableList()) { item ->
            val property = item as? VCardPropertyWrapper
            if (property != null) {
                handleClick(item)
            }
        }
        contacts_list.adapter = adapter
    }

    private fun handleClick(property: VCardPropertyWrapper) {
        when (property.property) {
            is Telephone -> dialNumber(property.value.normalizePhoneNumber())
            is Email -> sendEmailIntent(property.value)
        }
    }

    private fun prepareData(vCards: List<VCard>): List<VCardWrapper> {
        return vCards.map { vCard -> VCardWrapper.from(this, vCard) }
    }
}
