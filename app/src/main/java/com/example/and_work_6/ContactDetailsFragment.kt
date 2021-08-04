package com.example.and_work_6

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ContactDetailsFragment() : Fragment() {

    private var contact: Contact? = null
    private lateinit var onCloseListener: OnCloseListener

    constructor(contact: Contact) : this() {
        this.contact = contact
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onCloseListener = context as OnCloseListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            contact = savedInstanceState.getParcelable(CONTACT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_contact_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView = view.findViewById<ImageView>(R.id.for_image)
        val firstNameView = view.findViewById<EditText>(R.id.first_name)
        val lastNameView = view.findViewById<EditText>(R.id.last_name)
        val phoneView = view.findViewById<EditText>(R.id.phone)

        Glide.with(view)
            .load(contact?.imageLink)
            .error(R.drawable.failed_load)
            .into(imageView)
        firstNameView?.setText(contact?.firstName)
        lastNameView?.setText(contact?.lastName)
        phoneView?.setText(contact?.phone)

        val saveButton = view.findViewById<FloatingActionButton>(R.id.save_button)
        saveButton.setOnClickListener {
            val editedFirstName = firstNameView?.text.toString()
            val editedLastName = lastNameView?.text.toString()
            val editedPhone = phoneView?.text.toString()

            contact?.firstName = editedFirstName
            contact?.lastName = editedLastName
            contact?.phone = editedPhone

            onCloseListener.onCloseDetails()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CONTACT, contact)
    }

    interface OnCloseListener {
        fun onCloseDetails()
    }

    companion object {
        const val TAG = "ContactDetailsFragment"
        const val CONTACT = "contact"

        fun newInstance(contact: Contact) = ContactDetailsFragment(contact)
    }
}