package hr.ferit.dominradic.aplikacija

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hr.ferit.dominradic.aplikacija.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.signInButton.setOnClickListener {
            val email = binding.emailReg.text.toString()
            val password = binding.passTextReg.text.toString()
            val confirmPassword = binding.passTextRetype.text.toString()
            val username = binding.usernameReg.text.toString()
            val firstName = binding.frnameText.text.toString()
            val lastName = binding.lstnameText.text.toString()
            val following= (0..100).random().toString()
            val followers=(0..1000).random().toString()
            val user = User(
                firstName,
                lastName,
                username,
                email,
                following,
                followers,
            )

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && username.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
                if (password==confirmPassword) {
                    db.collection("users")
                        .add(user)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this,"Welcome!", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val displayIntent = Intent(this, MainActivity::class.java)
                            displayIntent.putExtra("email", email)
                            //displayIntent.putExtra("username",username)
                            startActivity(displayIntent)
                        }
                        else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "Empty field is not allowed", Toast.LENGTH_SHORT).show()
            }
        }
        binding.backToLoginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}