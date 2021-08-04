package com.example.and_work_6

import android.os.Bundle
import android.os.Looper
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(),
    ContactListFragment.OnContactItemClickListener,
    ContactDetailsFragment.OnCloseListener {

    private var detailsContainer: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        detailsContainer = findViewById(R.id.details_container)
        if (savedInstanceState == null) {
            Thread {
                Contact.CONTACTS = Contact.createContactsFromLink(Contact.CONTACT_LINK_1)
                Contact.CONTACTS = Contact.createContactsFromLink(Contact.CONTACT_LINK_2)
                Looper.getMainLooper().run { updateContactListFragment() }
            }.start()
        }
    }

    override fun onContactListItemClick(contact: Contact) {
        createContactDetailsFragment(contact)
    }

    override fun onCloseDetails() {
        supportFragmentManager.popBackStack()
        detailsContainer?.let { updateContactListFragment() }
    }

    private fun updateContactListFragment() {
        supportFragmentManager.beginTransaction().run {
            val contactListFragment = ContactListFragment.newInstance()
            replace(R.id.fragment_container, contactListFragment, ContactListFragment.TAG)
            commit()
        }
    }

    private fun createContactDetailsFragment(contact: Contact) {
        var containerId = R.id.fragment_container
        detailsContainer?.let { containerId = R.id.details_container }
        supportFragmentManager.beginTransaction().run {
            val contactDetailsFragment = ContactDetailsFragment.newInstance(contact)
            replace(containerId, contactDetailsFragment, ContactDetailsFragment.TAG)
            addToBackStack(ContactDetailsFragment.TAG)
            commit()
        }
    }
}