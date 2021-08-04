package com.example.and_work_6

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

data class Contact(
    var firstName: String,
    var lastName: String,
    var phone: String,
    var imageLink: String
) : Parcelable {

    constructor(parcel: Parcel) : this("", "", "", "") {
        val data: MutableList<String> = mutableListOf()
        parcel.readStringList(data)
        firstName = data[0]
        lastName = data[1]
        phone = data[2]
        imageLink = data[3]
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeStringArray(arrayOf(firstName, lastName, phone, imageLink))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Contact> = object : Parcelable.Creator<Contact> {
            override fun createFromParcel(parcel: Parcel): Contact {
                return Contact(parcel)
            }

            override fun newArray(size: Int): Array<Contact?> {
                return arrayOfNulls(size)
            }
        }
        const val TAG = "Contact"
        const val CONTACT_LINK_1 = "https://picsum.photos/v2/list?page=1&limit=100"
        const val CONTACT_LINK_2 = "https://picsum.photos/v2/list?page=2&limit=100"
        private const val ERROR_RECEIVING = "Error occurred during JSON receiving"
        private const val ERROR_PARSING = "Error occurred during JSON parsing"
        private const val AUTHOR_TAG = "author"
        private const val IMAGE_URL_TAG = "download_url"
        private const val LAST_NAME_STUB = ""
        private const val FULL_NAME_DELIMETER = " "
        var CONTACTS: MutableList<Contact> = mutableListOf()

        fun createContactsFromLink(link: String): MutableList<Contact> {
            val jsonStr = getJSONFromLink(link)
            return parseJsonToContacts(CONTACTS, jsonStr)
        }

        private fun getJSONFromLink(link: String): String {
            val sb = StringBuilder()

            var inputStream: InputStream? = null
            var br: BufferedReader? = null
            try {
                inputStream = URL(link).openConnection().getInputStream()
                br = BufferedReader(InputStreamReader(inputStream))
                var s: String? = br.readLine()
                while (s != null) {
                    sb.append(s).append("\n")
                    s = br.readLine()
                }
            } catch (e: IOException) {
                Log.w(TAG, ERROR_RECEIVING)
            } finally {
                inputStream?.close()
                br?.close()
            }

            return sb.toString()
        }

        private fun parseJsonToContacts(contactList: MutableList<Contact>, jsonStr: String):
                MutableList<Contact> {

            try {
                val jsonArray = JSONArray(jsonStr)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)

                    val fullName = jsonObject.getString(AUTHOR_TAG).split(FULL_NAME_DELIMETER)
                    val firstName = fullName[0]
                    val lastName = if (fullName.size == 2) {
                        fullName[1]
                    } else {
                        LAST_NAME_STUB
                    }
                    val imageUrl = jsonObject.getString(IMAGE_URL_TAG)

                    val contact = Contact(
                        firstName, lastName, "+${generatePhone()}", imageUrl
                    )

                    contactList.add(contact)
                }
            } catch (e: JSONException) {
                Log.w(TAG, ERROR_PARSING)
            }

            return contactList
        }

        private fun generatePhone(): String {
            var phone = ""
            repeat(4) {
                phone += (100..999).random().toString()
            }
            return phone
        }
    }
}
