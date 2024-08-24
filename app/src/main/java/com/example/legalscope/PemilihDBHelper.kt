package com.example.legalscope

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class PemilihDBHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    //SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "LegalScope.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "Pemilih"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NIK = "nik"
        private const val COLUMN_NAMA = "nama"
        private const val COLUMN_NO_HP = "noHp"
        private const val COLUMN_JENIS_KELAMIN = "jenisKelamin"
        private const val COLUMN_TANGGAL = "tanggal"
        private const val COLUMN_ALAMAT = "alamat"
        private const val COLUMN_GAMBAR = "gambar"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NIK TEXT,
                $COLUMN_NAMA TEXT,
                $COLUMN_NO_HP TEXT,
                $COLUMN_JENIS_KELAMIN TEXT,
                $COLUMN_TANGGAL TEXT,
                $COLUMN_ALAMAT TEXT,
                $COLUMN_GAMBAR TEXT
            )
        """
        db!!.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db!!.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun addPemilih(pemilih: Pemilih){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NIK, pemilih.nik)
            put(COLUMN_NAMA, pemilih.nama)
            put(COLUMN_NO_HP, pemilih.noHp)
            put(COLUMN_JENIS_KELAMIN, pemilih.jenisKelamin)
            put(COLUMN_TANGGAL, pemilih.tanggal)
            put(COLUMN_ALAMAT, pemilih.alamat)
            put(COLUMN_GAMBAR, pemilih.gambar.toString())
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun viewPemilih(): List<Pemilih>{
        val db = readableDatabase
        val listPemilih = mutableListOf<Pemilih>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val nik = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIK))
            val nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA))
            val noHp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NO_HP))
            val jk = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JENIS_KELAMIN))
            val tgl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TANGGAL))
            val alamat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALAMAT))
            val img = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAMBAR))

            val imgUri = Uri.parse(img)

            val pemilih = Pemilih(id, nik, nama, noHp, jk, tgl, alamat, imgUri)
            listPemilih.add(pemilih)

        }

        cursor.close()
        db.close()
        return listPemilih


    }
}