package com.gabyperez.divisaserver.others

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.gabyperez.divisaserver.database.MonedaDB

private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
    addURI("com.gabyperez.divisaserver", "monedas", 1)
    addURI("com.gabyperez.divisaserver", "monedas/#", 2)
    addURI("com.gabyperez.divisaserver", "monedas/*", 3)
}

class Provider : ContentProvider() {
    lateinit var repository: MonedaRepository
    lateinit var db: MonedaDB

    override fun onCreate(): Boolean {
        //TODO("Not yet implemented")
        repository =  (context as MyApp).repositoryMoneda
        db =  (context as MyApp).database
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        //TODO("Not yet implemented")
        var cursor: Cursor? = null

        when( sUriMatcher.match(p0)){
            //query / insert
            1 -> {
                cursor = db.MonedaDAO().getAllCursor()

            }
            //query
            2 -> {

            }
            //query / update  /  delete
            3 -> {

            }
            else -> {

            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }
}