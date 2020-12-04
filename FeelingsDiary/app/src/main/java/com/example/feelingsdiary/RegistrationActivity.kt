package com.example.feelingsdiary

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class RegistrationActivity : AppCompatActivity() {

    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var firstNameTV: EditText? = null
    private var lastNameTV: EditText? = null
    private var bdayTV: EditText? = null
    private var regBtn: Button? = null
    private var progressBar: ProgressBar? = null
    private var validator = Validators()

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase

    private val TAG = "CreateAccountActivity"
    //global variables


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        //mAuth = FirebaseAuth.getInstance()

        emailTV = findViewById(R.id.email) as EditText
        passwordTV = findViewById(R.id.password) as EditText
        firstNameTV = findViewById(R.id.firstName) as EditText
        lastNameTV = findViewById(R.id.lastName) as EditText
        bdayTV = findViewById(R.id.Bday) as EditText
        regBtn = findViewById(R.id.register) as Button
        progressBar = findViewById(R.id.progressBar)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.getReference("Users")
        mDatabaseReference.setValue("test")
        mAuth = FirebaseAuth.getInstance()


        regBtn!!.setOnClickListener { registerNewUser() }
    }



    private fun registerNewUser() {
        progressBar!!.visibility = View.VISIBLE

         val email = emailTV!!.text.toString()
         val password = passwordTV!!.text.toString()
         val firstName = firstNameTV!!.text.toString()
         val lastName = lastNameTV!!.text.toString()
         val bday = bdayTV!!.text.toString()
         val user: User = User(firstName, lastName, bday, email, password)

        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
            && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
            && !TextUtils.isEmpty(bday)) {

        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }

        if (!validator.validEmail(email)) {
            Toast.makeText(applicationContext, "Please enter a valid email...", Toast.LENGTH_LONG).show()
            return
        }
        if (!validator.validPassword(password)) {
            Toast.makeText(applicationContext, "Please enter a valid password!", Toast.LENGTH_LONG).show()
            return
        }


        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG).show()
                   // progressBar!!.visibility = View.GONE
                    Log.d(TAG, "createUserWithEmail:success")
                    val userId = mAuth!!.currentUser!!.uid
                    try {
                        throw IllegalStateException()
                        println("Hello World")
                    } catch (exc: Throwable) {
                        println("Something went wrong")
                    }
                    //Write verify email method

                    //update user profile information
                     //val currentUserDb = mDatabaseReference!!.child(userId)
                    val id = mDatabaseReference.push().key
                    val currentUserDb = mDatabaseReference.child(id!!)
                    Log.i(TAG, "test1")
                    currentUserDb.child("firstName").push()
                    Log.i(TAG, firstName)
                    currentUserDb.child("firstName").setValue(firstName)

                    currentUserDb.child("lastName").setValue(lastName)
                    Log.i(TAG, lastName)
                    currentUserDb.child("bday").setValue(bday)
                    Log.i(TAG, bday)

                    val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Registration failed! Please try again later", Toast.LENGTH_LONG).show()
                    progressBar!!.visibility = View.GONE
                }
            }
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // if the user tapped the more information item, show the more info dialog
        return super.onOptionsItemSelected(item);
    }


}
