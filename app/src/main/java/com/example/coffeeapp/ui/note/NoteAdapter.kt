package com.example.coffeeapp.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeapp.data.Note
import com.example.coffeeapp.data.Tag
import com.example.coffeeapp.databinding.NotePrefabBinding
import kotlinx.android.synthetic.main.note_editing_view.view.*

//Краткая карточка записи
class NoteAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Note, NoteAdapter.NotesViewHolder>(
        DiffCallback()
    ) {

    private var noteList: List<Note> = ArrayList()
    private var tagsList: MutableList<Array<Tag>> = ArrayList()

    fun setNotes(notes: List<Note>) {
        this.noteList = notes
        this.noteList.forEach {
            tagsList.add(emptyArray())
        }
        submitList(this.noteList)
    }

    fun setTags(tags: List<Tag>) {
        this.noteList.forEachIndexed { index, element ->

            var mas = emptyArray<Tag>()

            tags.forEach {
                if (element.id == it.note_id)
                    mas += it
            }

            tagsList[index] = mas
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note, tags: Array<Tag>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = NotePrefabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun getItemCount() = noteList.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(noteList[position], tagsList[position])
    }

    inner class NotesViewHolder(private val binding: NotePrefabBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {

                        val note = noteList[position]
                        val tags = tagsList[position]

                        listener.onItemClick(note, tags)
                    }
                }
            }
        }

        fun bind(note: Note, tags: Array<Tag>) = with(itemView) {
            binding.apply {
                noteTitleText.text = note.title
                noteDateText.text = note.createdDateFormatted
                noteAcidity.text = "Кислотность: " + note.acidSB.toString() + "/10"
                noteSweetness.text = "Сладость: " + note.sweetSB.toString() + "/10"
                noteBalance.text = "Баланс: " + note.balanceSB.toString() + "/10"
                var str = ""
                tags?.forEach { item ->
                    str += item.name + " "
                }
                if (str == "")
                    noteTagsText.text = "Не указана страна"
                else
                    noteTagsText.text = str
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note) =
            oldItem == newItem
    }
}

