package hr.ferit.dominradic.aplikacija

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.dominradic.aplikacija.databinding.ActivityLoginBinding




class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordTextLogin.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val displayIntent = Intent(this, MainActivity::class.java)
                        startActivity(displayIntent)
                        Toast.makeText(this,"Welcome back!", Toast.LENGTH_SHORT).show()

                    }
                    else {
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
                    .addOnFailureListener{
                        Toast.makeText(this,"Error occured: ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
                    }
            }
            else {
                Toast.makeText(this, "Empty field is not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}