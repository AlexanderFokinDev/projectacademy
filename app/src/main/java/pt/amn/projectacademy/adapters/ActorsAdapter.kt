package pt.amn.projectacademy.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pt.amn.projectacademy.databinding.ViewHolderActorBinding
import pt.amn.projectacademy.models.Actor

class ActorsAdapter() : RecyclerView.Adapter<ActorsAdapter.ActorsViewHolder>() {

    private var actors: List<Actor> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        return ActorsViewHolder(ViewHolderActorBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false))
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.onBind(actors[position])
    }

    override fun getItemCount(): Int {
        return actors.size
    }

    fun bindActors(newActors : List<Actor>) {
        actors = newActors
    }

    class ActorsViewHolder(private val binding: ViewHolderActorBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun onBind(actor: Actor) {
            binding.run {
                tvName.text = actor.name

                Glide.with(binding.root)
                    .load(actor.picture)
                    .into(ivActor)
            }
        }

    }

}