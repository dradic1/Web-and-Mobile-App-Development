package hr.ferit.dominradic.aplikacija


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(),
    PostRecyclerAdapter.ContentListener {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: PostRecyclerAdapter
    val firebaseAuth = FirebaseAuth.getInstance()
    val userSignEmail = firebaseAuth.currentUser?.email
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val postList = ArrayList<Post>()


        db.collection("posts")
            //.whereEqualTo("userEmail", userSignEmail)
            .orderBy("datePost", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (data in result.documents) {
                    val post = data.toObject(Post::class.java)
                    if (post != null) {
                        postList.add(post)
                    }
                }
                recyclerAdapter = PostRecyclerAdapter(
                    postList,
                    this@HomeFragment
                )

                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = recyclerAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w(
                    "MainActivity", "Error getting documents.",
                    exception
                )
            }

        return view
    }

    override fun onItemButtonClick(index: Int, post: Post) {
        if (post.userEmail == userSignEmail) {
            recyclerAdapter.removeItem(index)
            db.collection("posts")
                .whereEqualTo("tweet", post.tweet)
                .get()
                .addOnSuccessListener {
                    for (data in it.documents) {
                        val postID = data.id
                        db.collection("posts")
                            .document(postID)
                            .delete()
                    }
                }
        }
        else
        {
            Toast.makeText(this@HomeFragment.requireActivity(),"No user permission!", Toast.LENGTH_SHORT).show()
        }
    }
}
