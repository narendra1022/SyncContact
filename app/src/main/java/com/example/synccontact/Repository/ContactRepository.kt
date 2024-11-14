package com.example.synccontact.Repository

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.example.synccontact.Model.Contact
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {

    private val contentResolver = context.contentResolver

    // Function to fetch today's contacts using coroutines
    suspend fun getTodayContacts(): List<Contact> {
        return try {
            val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val query = firestore.collection("users").whereEqualTo("addedDate", todayDate)
            val snapshot = query.get().await() // Await Firestore's response
            snapshot.toObjects(Contact::class.java) // Convert snapshot to a list of contacts
        } catch (e: Exception) {
            Log.e("ContactSync", "Error fetching contacts: ${e.message}")
            emptyList()
        }
    }

    // Suspend function to sync contacts with phone's contacts
    suspend fun syncContactsWithPhone(contacts: List<Contact>) {
        try {

            withContext(Dispatchers.IO) {
                val operations = ArrayList<ContentProviderOperation>()
                contacts.forEach { contact ->
                    // Insert a new raw contact entry for each contact
                    val rawContactOperation =
                        ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                            .build()

                    operations.add(rawContactOperation)

                    // Get the newly created raw contact ID (which will be automatically assigned)
                    val rawContactId = operations.size - 1

                    // Insert the name of the contact
                    operations.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(
                                ContactsContract.Data.RAW_CONTACT_ID,
                                rawContactId
                            )
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                contact.name
                            )
                            .build()
                    )

                    // Insert the phone number of the contact
                    operations.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(
                                ContactsContract.Data.RAW_CONTACT_ID,
                                rawContactId
                            )
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                contact.phoneNumber
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                            )
                            .build()
                    )
                }

                // Apply the batch insert operation
                contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
            }
        } catch (e: Exception) {
            Log.e("ContactSync", "Error syncing contacts: ${e.message}")
        }
    }
}
