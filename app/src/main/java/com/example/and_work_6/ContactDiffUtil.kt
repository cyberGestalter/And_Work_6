package com.example.and_work_6

import androidx.recyclerview.widget.DiffUtil

class ContactDiffUtil(
    private val oldList: MutableList<Contact>,
    private val newList: MutableList<Contact>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact = oldList[oldItemPosition]
        val newContact = newList[newItemPosition]
        return oldContact.firstName == newContact.firstName &&
                oldContact.lastName == newContact.lastName &&
                oldContact.phone == newContact.phone &&
                oldContact.imageLink == newContact.imageLink
    }
}