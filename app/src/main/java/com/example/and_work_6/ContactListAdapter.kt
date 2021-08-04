package com.example.and_work_6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ContactListAdapter(
    private val onClick: (Contact) -> Unit,
    private val onLongClick: (Contact) -> Unit
) :
    RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {

    var contactList: MutableList<Contact> = mutableListOf()
        set(value) {
            val contactDiffUtilCallback = ContactDiffUtil(field, value)
            val contactDiffResult = DiffUtil.calculateDiff(contactDiffUtilCallback, false)
            field = value
            contactDiffResult.dispatchUpdatesTo(this)
        }

    class ContactViewHolder(
        itemView: View,
        val onClick: (Contact) -> Unit,
        val onLongClick: (Contact) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val nameView: TextView = itemView.findViewById(R.id.contact_name)
        private val phoneView: TextView = itemView.findViewById(R.id.contact_phone)
        private val imageView: ImageView = itemView.findViewById(R.id.contact_image)

        private var currentContact: Contact? = null

        init {
            itemView.setOnClickListener {
                currentContact?.let {
                    onClick(it)
                }
            }
            itemView.setOnLongClickListener {
                currentContact?.let {
                    onLongClick(it)
                }
                true
            }
        }

        fun bind(contact: Contact) {
            currentContact = contact
            "${contact.firstName} ${contact.lastName}".also { nameView.text = it }
            phoneView.text = contact.phone

            Glide.with(itemView)
                .load(contact.imageLink)
                .error(R.drawable.failed_load)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.bind(contact)
    }

    override fun getItemCount() = contactList.size
}