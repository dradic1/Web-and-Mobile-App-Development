package hr.ferit.dominradic.aplikacija

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.random.Random


class ProfileFragment : Fragment() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val btnSO: Button = view.findViewById(R.id.signoutButton)
        val loggedInEmail = firebaseAuth.currentUser?.email
        val username: TextView = view.findViewById(R.id.usernameText)
        val firstName: TextView = view.findViewById(R.id.firstnameText)
        val lastName: TextView = view.findViewById(R.id.lastnameText)
        val welcome: TextView = view.findViewById(R.id.welcomeText)
        val following: TextView = view.findViewById(R.id.numberFollowing)
        val followers: TextView= view.findViewById(R.id.numberFollowers)
        val imageUrl : EditText=view.findViewById(R.id.imageUrlText)
        val editBtn : ImageButton=view.findViewById(R.id.editButton)
        val userPhoto : ImageView = view.findViewById(R.id.userPhoto)

        editBtn.setOnClickListener{
            db.collection("users")
                .whereEqualTo("email", loggedInEmail)
                .get()
                .addOnSuccessListener { result ->
                    for (data in result.documents) {
                        val id = data.id
                        Log.e("TAG",id.toString())
                        db.collection("users")
                            .document(id.toString())
                            .update("picture",imageUrl.text.toString())
                            .addOnSuccessListener {
                                Glide.with(view.context).load(imageUrl.text.toString()).into(userPhoto)
                            }

                    }
                    }
        }



        db.collection("users")
            .whereEqualTo("email", loggedInEmail)
            .get()
            .addOnSuccessListener { result ->
                for (data in result.documents) {
                    val user = data.toObject<User>()
                    username.text = "@" + user?.username.toString()
                    firstName.text = user?.firstName.toString()
                    lastName.text = user?.lastName.toString()
                    welcome.text = "Hello " + user?.firstName.toString() + "!"
                    following.text=user?.followingCount
                    followers.text=user?.followersCount
                    Glide.with(view.context).load(user?.picture).into(userPhoto)

                }
            }

                btnSO.setOnClickListener {
                    requireActivity().run {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }

        return view
    }
}

