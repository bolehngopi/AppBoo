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
        val nameForm = findViewById<EditText>(R.id.editName)
        val studentList = findViewById<ListView>(R.id.studentList)

        // Insert example
        addButton.setOnClickListener {
            val selectedGender = findViewById<RadioButton>(genderGroup.checkedRadioButtonId)?.text
            val selectedHobbies = hobbies.filter { it.isChecked }.joinToString(", ") { it.text }
            val selectedName = nameForm.text.toString() // Get the name from the EditText

            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("name", selectedName)  // Add the name value here
                put("gender", selectedGender.toString())
                put("hobbies", selectedHobbies)
            }

            db.insert("students", null, values)
            Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show()
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

                studentInfo.text = "Name: ${student["name"]},Gender: ${student["gender"]}, Hobbies: ${student["hobbies"]}"

                updateButton.setOnClickListener {
                    // Get current student data
                    val studentId = student["id"] as Int
                    val currentName = student["name"] as String
                    val currentGender = student["gender"] as String
                    val currentHobbies = student["hobbies"] as String

                    // Create an AlertDialog to allow the user to update gender and hobbies
                    val builder = AlertDialog.Builder(this@StudentManagementActivity)
                    builder.setTitle("Update Student Data")

                    // Create a layout for the dialog
                    val dialogLayout = layoutInflater.inflate(R.layout.dialog_update_student, null)

                    // Get references to the input fields
                    val nameEditText = dialogLayout.findViewById<EditText>(R.id.editName)
                    val genderEditText = dialogLayout.findViewById<EditText>(R.id.editGender)
                    val hobbiesEditText = dialogLayout.findViewById<EditText>(R.id.editHobbies)

                    // Pre-fill the fields with current data
                    nameEditText.setText(currentName)
                    genderEditText.setText(currentGender)
                    hobbiesEditText.setText(currentHobbies)

                    // Set the dialog view
                    builder.setView(dialogLayout)

                    builder.setPositiveButton("Update") { _, _ ->
                        // Get the new input from the dialog
                        val newName = nameEditText.text.toString()
                        val newGender = genderEditText.text.toString()
                        val newHobbies = hobbiesEditText.text.toString()

                        // Update the student data in the database
                        dbHelper.updateStudent(studentId, newName, newGender, newHobbies)
                        loadStudents(listView)  // Refresh the list
                        Toast.makeText(this@StudentManagementActivity, "Data Updated", Toast.LENGTH_SHORT).show()
                    }

                    builder.setNegativeButton("Cancel", null)

                    builder.create().show()
                }


                deleteButton.setOnClickListener {
                    // Delete logic here
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
