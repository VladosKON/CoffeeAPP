package com.example.coffeeapp.ui.note

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.coffeeapp.data.*
import com.example.coffeeapp.data.*
import com.example.coffeeapp.ui.ADD_TASK_RESULT_OK
import com.example.coffeeapp.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

//ViewModel краткой карточки
class NoteViewModel @ViewModelInject constructor(
    private val noteDao: NoteDao,
    private val tagDao: TagDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val noteEventChannel = Channel<NoteEvent>()
    val noteEvent = noteEventChannel.receiveAsFlow()

    val noteList = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { sQ, pF ->
        Pair(sQ, pF)
    }.flatMapLatest { (sQ, pF) ->
        noteDao.getNotes(sQ, pF.sortOrder)
    }

    val allLists = combine(
        noteList,
        tagDao.getTagList()
    ){ notes, tags ->
        Pair(notes, tags)
    }.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onNoteSelected(note: Note, tags: Array<Tag>) = viewModelScope.launch {
        noteEventChannel.send(
            NoteEvent.NavigateToEditNoteScreen(
                note,
                tags
            )
        )
    }

    fun onNoteSwiped(note: Note) = viewModelScope.launch {
        noteDao.delete(note)
        noteEventChannel.send(
            NoteEvent.ShowUndoDeleteNoteMessage(
                note
            )
        )
    }

    fun onUndoDeleteClick(note: Note) = viewModelScope.launch  {
        noteDao.insert(note)
    }

    fun onAddNewNoteClick() = viewModelScope.launch {
        noteEventChannel.send(NoteEvent.NavigateToAddNoteScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showNoteSavedConfirmationMassege("Запись добавлена")
            EDIT_TASK_RESULT_OK -> showNoteSavedConfirmationMassege("Запись обновлена")
        }
    }

    private fun showNoteSavedConfirmationMassege(text: String) = viewModelScope.launch {
        noteEventChannel.send(
            NoteEvent.ShowNoteSavedConfirmationMassege(
                text
            )
        )
    }

    sealed class NoteEvent {
        object NavigateToAddNoteScreen : NoteEvent()
        data class NavigateToEditNoteScreen(val note: Note, val tags: Array<Tag>) : NoteEvent() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as NavigateToEditNoteScreen

                if (!tags.contentEquals(other.tags)) return false

                return true
            }

            override fun hashCode(): Int {
                return tags.contentHashCode()
            }
        }
        data class ShowUndoDeleteNoteMessage(val note: Note) : NoteEvent()
        data class ShowNoteSavedConfirmationMassege(val msg: String) : NoteEvent()
    }
}

