package com.bolehngopi.pplgmobile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.welcomeText)
        val profileButton = findViewById<Button>(R.id.profileButton)

        textView.text = "Selamat Datang di Kelas Fase F PPLG 2024"

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_profil -> {
                // Navigate to the Profile page
                startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }
            R.id.menu_struktur -> {
                // Navigate to the Structure page
                startActivity(Intent(this, StructureActivity::class.java))
                return true
            }
            R.id.menu_kelola -> {
                // Navigate to the Student Management page
                startActivity(Intent(this, StudentManagementActivity::class.java))
                return true
            }
            R.id.menu_keluar -> {
                // Currently not implemented, placeholder action
                finish() // Closes the app
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
