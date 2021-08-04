package com.example.and_work_6

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class ContactListFragment : Fragment() {

    private lateinit var onContactItemClickListener: OnContactItemClickListener
    private lateinit var adapter: ContactListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onContactItemClickListener = context as OnContactItemClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ContactListAdapter(
            onContactItemClickListener::onContactListItemClick,
            this::showDeleteDialog
        )
        adapter.contactList = Contact.CONTACTS
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_contact_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contactRecyclerView = view.findViewById<RecyclerView>(R.id.contact_list)
        contactRecyclerView.adapter = adapter
        val decorator =
            ContactItemDecorator(resources.getDimension(R.dimen.item_margin), R.drawable.divider)
        contactRecyclerView.addItemDecoration(decorator)

        val searchClearContentBtn = view.findViewById<ImageView>(R.id.search_clear_content_btn)
        val searchEditText = view.findViewById<EditText>(R.id.search_text)
        searchEditText.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().lowercase(Locale.getDefault())
            searchClearContentBtn.visibility = if (query == "") {
                View.GONE
            } else {
                View.VISIBLE
            }
            filterWithQuery(query)
        }
        searchClearContentBtn.setOnClickListener { searchEditText.setText("") }
    }

    private fun filterWithQuery(query: String) {
        if (query.isNotEmpty()) {
            val filteredList = mutableListOf<Contact>()
            for (contact in Contact.CONTACTS) {
                if ("${contact.firstName} ${contact.lastName}"
                        .lowercase(Locale.getDefault()).contains(query)
                ) {
                    filteredList.add(contact)
                }
            }
            adapter.contactList = filteredList
        } else if (query.isEmpty()) {
            adapter.contactList = Contact.CONTACTS
        }
    }

    private fun showDeleteDialog(contact: Contact) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.remove_contact_title))
                .setMessage(getString(R.string.remove_contact_message))
                .setPositiveButton(getString(R.string.remove_contact_yes)) { _, _ ->
                    run {
                        val newContactList = adapter.contactList.minus(contact).toMutableList()
                        adapter.contactList = newContactList
                        Contact.CONTACTS.remove(contact)
                    }
                }
                .setNegativeButton(getString(R.string.remove_contact_no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    interface OnContactItemClickListener {
        fun onContactListItemClick(contact: Contact)
    }

    companion object {
        const val TAG = "ContactListFragment"

        fun newInstance() = ContactListFragment()
    }
}