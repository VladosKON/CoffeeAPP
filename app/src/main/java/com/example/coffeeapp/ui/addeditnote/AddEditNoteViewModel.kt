package com.example.coffeeapp.ui.addeditnote

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeeapp.data.Note
import com.example.coffeeapp.data.NoteDao
import com.example.coffeeapp.data.Tag
import com.example.coffeeapp.data.TagDao
import com.example.coffeeapp.ui.ADD_TASK_RESULT_OK
import com.example.coffeeapp.ui.EDIT_TASK_RESULT_OK
import com.example.coffeeapp.ui.note.NoteViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

//ViewModel добавления и редактирования записи
class AddEditNoteViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao,
    private val tagsDao: TagDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val note = state.get<Note>("note")
    var tags = state.get<Array<Tag>>("tags")

    private var toAddTags: MutableList<Tag> = ArrayList()
    private var toDeleteTags: MutableList<Tag> = ArrayList()

    var noteTitle = state.get<String>("noteTitle") ?: note?.title ?: ""
        set(value) {
            field = value
            state.set("noteTitle", value)
        }

    var noteRoastDate = state.get<String>("noteRoastDate") ?: note?.roastDate ?: ""
        set(value) {
            field = value
            state.set("noteRoastDate", value)
        }

    var noteRoastInfo = state.get<String>("noteRoastInfo") ?: note?.roastInfo ?: ""
        set(value) {
            field = value
            state.set("noteRoastInfo", value)
        }

    var noteDryAroma = state.get<String>("noteDryAroma") ?: note?.dryAr ?: ""
        set(value) {
            field = value
            state.set("noteDryAroma", value)
        }

    var noteDryAromaSB = state.get<Int>("noteDryAromaSB") ?: note?.dryArSB ?: 1
        set(value) {
            field = value
            state.set("noteDryAromaSB", value)
        }

    var noteWetAroma = state.get<String>("noteWetAroma") ?: note?.wetAr ?: ""
        set(value) {
            field = value
            state.set("noteWetAroma", value)
        }

    var noteWetAromaSB = state.get<Int>("noteWetAromaSB") ?: note?.wetArSB ?: 1
        set(value) {
            field = value
            state.set("noteWetAromaSB", value)
        }

    var noteSweetness = state.get<String>("noteSweetness") ?: note?.sweet ?: ""
        set(value) {
            field = value
            state.set("noteSweetness", value)
        }

    var noteSweetnessSB = state.get<Int>("noteSweetnessSB") ?: note?.sweetSB ?: 1
        set(value) {
            field = value
            state.set("noteSweetnessSB", value)
        }

    var noteAcidity = state.get<String>("noteAcidity") ?: note?.acid ?: ""
        set(value) {
            field = value
            state.set("noteAcidity", value)
        }

    var noteAciditySB = state.get<Int>("noteAciditySB") ?: note?.acidSB ?: 1
        set(value) {
            field = value
            state.set("noteAciditySB", value)
        }

    var noteBody = state.get<String>("noteBody") ?: note?.body ?: ""
        set(value) {
            field = value
            state.set("noteBody", value)
        }

    var noteBodySB = state.get<Int>("noteBodySB") ?: note?.bodySB ?: 1
        set(value) {
            field = value
            state.set("noteBodySB", value)
        }

    var noteBalanceSB = state.get<Int>("noteBalanceSB") ?: note?.balanceSB ?: 1
        set(value) {
            field = value
            state.set("noteBalanceSB", value)
        }

    var noteFlavour = state.get<String>("noteFlavour") ?: note?.flavour ?: ""
        set(value) {
            field = value
            state.set("noteFlavour", value)
        }

    private val addEditNoteEventChannel = Channel<AddEditNoteEvent>()
    val addEditNoteEvent = addEditNoteEventChannel.receiveAsFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAddClick() {
        if (noteTitle.isBlank()) {
            showInvalidInputMessage("Введите название")
            return
        }

        if (noteRoastDate.isBlank()) {
            showInvalidInputMessage("Введите дату обжарки")
            return
        }

        if (note != null) {
            val updatedNote = note.copy(
                title = noteTitle,
                roastDate = noteRoastDate,
                roastInfo = noteRoastInfo,
                dryAr = noteDryAroma,
                dryArSB = noteDryAromaSB,
                wetAr = noteWetAroma,
                wetArSB = noteWetAromaSB,
                sweet = noteSweetness,
                sweetSB = noteSweetnessSB,
                acid = noteAcidity,
                acidSB = noteAciditySB,
                body = noteBody,
                bodySB = noteBodySB,
                balanceSB = noteBalanceSB,
                flavour = noteFlavour,
                lastDate = System.currentTimeMillis()
            )

            tags?.plus(toAddTags)
            val mtags = tags?.toMutableList()
            mtags?.removeAll(toDeleteTags)
            tags = mtags?.toTypedArray()

            updateTags()

            state.set("tags", tags)
            updatedNote(updatedNote)

        } else {
            val newNote =
                Note(
                    title = noteTitle,
                    roastDate = noteRoastDate,
                    roastInfo = noteRoastInfo,
                    dryAr = noteDryAroma,
                    dryArSB = noteDryAromaSB,
                    wetAr = noteWetAroma,
                    wetArSB = noteWetAromaSB,
                    sweet = noteSweetness,
                    sweetSB = noteSweetnessSB,
                    acid = noteAcidity,
                    acidSB = noteAciditySB,
                    body = noteBody,
                    bodySB = noteBodySB,
                    balanceSB = noteBalanceSB,
                    flavour = noteFlavour
                )

            newNote.id = Math.abs(newNote.hashCode())
            toAddTags.removeAll(toDeleteTags)
            toAddTags.forEach { it.note_id = newNote.id }
            tags = toAddTags.toTypedArray()

            createNote(newNote)

            insertTags()
            state.set("tags", tags)
        }
    }

    private fun updateTags() = viewModelScope.launch {
        toAddTags?.forEach { tagsDao.insert(it) }
        toDeleteTags?.forEach { tagsDao.delete(it) }
    }

    private fun insertTags() = viewModelScope.launch {
        tags?.forEach { tagsDao.insert(it) }
    }

    private fun createNote(newNote: Note) = viewModelScope.launch {
        noteDao.insert(newNote)
        addEditNoteEventChannel.send(
            AddEditNoteEvent.NavigateBackWithResult(
                ADD_TASK_RESULT_OK
            )
        )
    }

    private fun updatedNote(updatedNote: Note) = viewModelScope.launch {
        noteDao.update(updatedNote)
        addEditNoteEventChannel.send(
            AddEditNoteEvent.NavigateBackWithResult(
                EDIT_TASK_RESULT_OK
            )
        )
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditNoteEventChannel.send(
            AddEditNoteEvent.ShowInvalidInputMessage(
                text
            )
        )
    }

    private fun createTag(newTag: Tag) = viewModelScope.launch {
        toAddTags.add(newTag)
        addEditNoteEventChannel.send(
            AddEditNoteEvent.CreateNewChipForTag(
                newTag
            )
        )
    }

    fun onTagAdd(name: String) {
        if (note != null) {
            val newTag =
                Tag(name = name, note_id = note.id)
            createTag(newTag)
        } else {
            val newTag = Tag(name = name)
            createTag(newTag)
        }
    }

    fun onTagDelete(tag: Tag) {
        toDeleteTags.add(tag)
    }

    sealed class AddEditNoteEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditNoteEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditNoteEvent()
        data class CreateNewChipForTag(val tag: Tag) : AddEditNoteEvent()
    }

}
