package hr.ferit.dominradic.aplikacija

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PostRecyclerAdapter(
    val items: ArrayList<Post>,
    val listener: ContentListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tweetlayout_item,
                parent, false)
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> {
                holder.bind(position, listener, items[position])
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, items.size)
    }

    class PostViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val postText = view.findViewById<TextView>(R.id.tv_tweet_text)
        private val postUsername = view.findViewById<TextView>(R.id.tv_username)
        private val postName = view.findViewById<TextView>(R.id.tv_name)
        private val postDate = view.findViewById<TextView>(R.id.dateText)
        private val deleteBtn = view.findViewById<ImageButton>(R.id.deleteButton)
        private val userPhoto=view.findViewById<ImageView>(R.id.profile_photo)
        private val db = Firebase.firestore
        private val firebaseAuth = FirebaseAuth.getInstance()

        fun bind(
            index: Int,
            listener: ContentListener,
            post: Post
        ) {
            postDate.text=post.datePost
            postText.text=post.tweet
            postName.text=post.name.toString().uppercase()
            postUsername.text="@" + post.username
            Glide.with(view.context).load(post.userPhoto).into(userPhoto)
            deleteBtn.setOnClickListener {
                listener.onItemButtonClick(index, post)
            }
        }
    }
    interface ContentListener {
        fun onItemButtonClick(index: Int, post: Post)
    }
}