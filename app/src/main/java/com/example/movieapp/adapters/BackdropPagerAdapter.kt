import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.data.Backdrop
import com.example.movieapp.databinding.BackdropItemBinding

class BackdropPagerAdapter(private val backdrops: List<Backdrop>) :
    RecyclerView.Adapter<BackdropPagerAdapter.BackdropViewHolder>() {

    inner class BackdropViewHolder(val binding: BackdropItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackdropViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BackdropItemBinding.inflate(inflater, parent, false)
        return BackdropViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BackdropViewHolder, position: Int) {
        val backdrop = backdrops[position]
        val imageUrl = "https://image.tmdb.org/t/p/w780${backdrop.file_path}"

        Glide.with(holder.itemView)
            .load(imageUrl)
            .into(holder.binding.backdropImageView)
    }

    override fun getItemCount(): Int = backdrops.size
}
