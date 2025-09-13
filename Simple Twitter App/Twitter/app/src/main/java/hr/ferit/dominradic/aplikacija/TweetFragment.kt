package hr.ferit.dominradic.aplikacija

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TweetFragment : Fragment() {

    private val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {val view = inflater.inflate(R.layout.fragment_tweet, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        val loggedInEmail = firebaseAuth.currentUser?.email
        val nextButton = view.findViewById<Button>(R.id.buttonTweet)
        val tweet = view.findViewById<TextView>(R.id.tweetText)
        val profilePhoto : ImageView= view.findViewById(R.id.profile_photo)


        db.collection("users")
            .whereEqualTo("email", loggedInEmail)
            .get()
            .addOnSuccessListener { result ->
                for (data in result.documents) {
                    val user = data.toObject<User>()
                    Glide.with(view.context).load(user?.picture).into(profilePhoto)
                }
            }

        nextButton.setOnClickListener {

            db.collection("users")
                .whereEqualTo("email",loggedInEmail)
                .get()
                .addOnSuccessListener {result ->
                    for (data in result.documents) {
                        val user = data.toObject<User>()
                        Glide.with(view.context).load(user?.picture).into(profilePhoto)
                        val post = Post(
                            tweet = tweet.text.toString(),
                            userEmail = user?.email.toString(),
                            name= user?.firstName.toString(),
                            username = user?.username.toString(),
                            userPhoto = user?.picture.toString(),
                            datePost = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm:ss")).toString())
                        db.collection("posts")
                            .add(post)
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.frameLayout, HomeFragment())?.commit()
                    }
                }
        }
        return view
    }
}

