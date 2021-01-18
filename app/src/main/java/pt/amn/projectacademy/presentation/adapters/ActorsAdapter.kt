package pt.amn.projectacademy.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.amn.projectacademy.databinding.ViewHolderActorBinding
import pt.amn.projectacademy.loadImage
import pt.amn.projectacademy.domain.domain.Actor
import pt.amn.projectacademy.utils.BASE_URL_ACTOR_IMAGE

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
                ivActor.loadImage(binding.root, BASE_URL_ACTOR_IMAGE + actor.picture)
            }
        }

    }

}