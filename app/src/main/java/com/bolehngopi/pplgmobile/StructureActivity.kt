package com.bolehngopi.pplgmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class StructureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_structure)

        val structureText = findViewById<TextView>(R.id.structureText)
        structureText.text = "Struktur organisasi kelas Fase F PPLG:\n1. Ketua Kelas: John Doe\n2. Wakil Ketua: Jane Doe\n3. Sekretaris: Alex Smith"
    }
}
