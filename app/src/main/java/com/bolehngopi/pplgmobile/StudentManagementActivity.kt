package com.bolehngopi.pplgmobile

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class StudentManagementActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_management)

        dbHelper = DBHelper(this)

        val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
        val hobbies = listOf(
            findViewById<CheckBox>(R.id.hobby1),
            findViewById<CheckBox>(R.id.hobby2),
            findViewById<CheckBox>(R.id.hobby3)
        )
        val addButton = findViewById<Button>(R.id.addButton)
        val nameForm = findViewById<EditText>(R.id.nameEdit)
        val studentList = findViewById<ListView>(R.id.studentList)

        // Insert example
        addButton.setOnClickListener {
            val selectedGender = findViewById<RadioButton>(genderGroup.checkedRadioButtonId)?.text
            val selectedHobbies = hobbies.filter { it.isChecked }.joinToString(", ") { it.text }
            val selectedName = nameForm.text.toString().trim()

            if (selectedName.isEmpty() || selectedGender == null) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("name", selectedName)
                put("gender", selectedGender.toString())
                put("hobbies", selectedHobbies)
            }

            db.insert("students", null, values)
            Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show()
            loadStudents(studentList)  // Refresh list after adding
        }

        // Load existing students
        loadStudents(studentList)
    }

    private fun loadStudents(listView: ListView) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM students", null)
        val students = mutableListOf<Map<String, Any>>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"))
            val hobbies = cursor.getString(cursor.getColumnIndexOrThrow("hobbies"))
            students.add(mapOf("id" to id, "name" to name, "gender" to gender, "hobbies" to hobbies))
        }
        cursor.close()

        val adapter = object : BaseAdapter() {
            override fun getCount(): Int = students.size
            override fun getItem(position: Int): Any = students[position]
            override fun getItemId(position: Int): Long = position.toLong()
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = convertView ?: layoutInflater.inflate(R.layout.list_item_student, parent, false)
                val student = students[position]

                val studentInfo = view.findViewById<TextView>(R.id.studentInfo)
                val updateButton = view.findViewById<Button>(R.id.updateButton)
                val deleteButton = view.findViewById<Button>(R.id.deleteButton)

                studentInfo.text = "Name: ${student["name"]}, Gender: ${student["gender"]}, Hobbies: ${student["hobbies"]}"

                updateButton.setOnClickListener {
                    val studentId = student["id"] as Int
                    val currentName = student["name"] as String
                    val currentGender = student["gender"] as String
                    val currentHobbies = student["hobbies"] as String

                    val builder = AlertDialog.Builder(this@StudentManagementActivity)
                    builder.setTitle("Update Student Data")

                    val dialogLayout = layoutInflater.inflate(R.layout.dialog_update_student, null)
                    val nameEditText = dialogLayout.findViewById<EditText>(R.id.editName)
                    val genderEditText = dialogLayout.findViewById<EditText>(R.id.editGender)
                    val hobbiesEditText = dialogLayout.findViewById<EditText>(R.id.editHobbies)

                    nameEditText.setText(currentName)
                    genderEditText.setText(currentGender)
                    hobbiesEditText.setText(currentHobbies)

                    builder.setView(dialogLayout)

                    builder.setPositiveButton("Update") { _, _ ->
                        val newName = nameEditText.text.toString()
                        val newGender = genderEditText.text.toString()
                        val newHobbies = hobbiesEditText.text.toString()

                        dbHelper.updateStudent(studentId, newName, newGender, newHobbies)
                        loadStudents(listView)
                        Toast.makeText(this@StudentManagementActivity, "Data Updated", Toast.LENGTH_SHORT).show()
                    }

                    builder.setNegativeButton("Cancel", null)
                    builder.create().show()
                }

                deleteButton.setOnClickListener {
                    val studentId = student["id"] as Int
                    dbHelper.deleteStudent(studentId)
                    loadStudents(listView)
                    Toast.makeText(this@StudentManagementActivity, "Data Deleted", Toast.LENGTH_SHORT).show()
                }

                return view
            }
        }

        listView.adapter = adapter
    }
}
