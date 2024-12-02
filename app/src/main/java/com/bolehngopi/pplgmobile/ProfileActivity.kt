package com.bolehngopi.pplgmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val profileText = findViewById<TextView>(R.id.profileText)
        val profileImage = findViewById<ImageView>(R.id.profileImage)

        profileText.text = "Cerita singkat kelas Fase F PPLG: Kami adalah kelas terbaik dengan semangat tinggi untuk belajar dan berkarya."
        profileImage.setImageResource(R.drawable.class_image_background) // Add an image in res/drawable
    }
}
