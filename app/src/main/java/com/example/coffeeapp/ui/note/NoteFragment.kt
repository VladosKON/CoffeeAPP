package com.example.coffeeapp.ui.note

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeapp.R
import com.example.coffeeapp.data.Note
import com.example.coffeeapp.data.SortOrder
import com.example.coffeeapp.data.Tag
import com.example.coffeeapp.databinding.NoteListPrefabBinding
import com.example.coffeeapp.ui.note.NoteAdapter.OnItemClickListener
import com.example.coffeeapp.util.exhaustive
import com.example.coffeeapp.util.onQueryTextChanged
import com.example.coffeeapp.ui.note.NoteFragmentDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest

//Фрагмент краткой карточки
@AndroidEntryPoint
class NoteFragment : Fragment(R.layout.note_list_prefab), OnItemClickListener {

    private val noteViewModel: NoteViewModel by viewModels()

    private val noteAdapter: NoteAdapter =
        NoteAdapter(this)

    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = NoteListPrefabBinding.bind(view)

        binding.apply {
            noteList.apply {
                adapter = noteAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val note = noteAdapter.currentList[viewHolder.adapterPosition]
                    noteViewModel.onNoteSwiped(note)
                }
            }).attachToRecyclerView(noteList)

            fabAddNote.setOnClickListener {
                noteViewModel.onAddNewNoteClick()
            }
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            noteViewModel.onAddEditResult(result)
        }

        noteViewModel.allLists.observe(viewLifecycleOwner) {
            noteAdapter.setNotes(it.first)
            noteAdapter.setTags(it.second)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            noteViewModel.noteEvent.collect { event ->
                when (event) {
                    is NoteViewModel.NoteEvent.ShowUndoDeleteNoteMessage -> {
                        Snackbar.make(requireView(), "Запись удалена", Snackbar.LENGTH_LONG)
                            .setAction("ОТМЕНИТЬ") {
                                noteViewModel.onUndoDeleteClick(event.note)
                            }.show()
                    }
                    is NoteViewModel.NoteEvent.NavigateToAddNoteScreen -> {
                        val action =
                            NoteFragmentDirections.actionNoteFragmentToAddEditNoteFragment(
                                null,
                                null,
                                "Новая запись"
                            )
                        findNavController().navigate(action)
                    }
                    is NoteViewModel.NoteEvent.NavigateToEditNoteScreen -> {
                        val action =
                            NoteFragmentDirections.actionNoteFragmentToAddEditNoteFragment(
                                event.note,
                                event.tags,
                                "Редактирование записи"
                            )
                        findNavController().navigate(action)
                    }
                    is NoteViewModel.NoteEvent.ShowNoteSavedConfirmationMassege -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_task, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = noteViewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
            noteViewModel.searchQuery.value = it
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_tags -> {
                noteViewModel.onSortOrderSelected(SortOrder.BY_TAGS)
                true
            }
            R.id.action_sort_by_created -> {
                noteViewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_sort_by_acid -> {
                noteViewModel.onSortOrderSelected(SortOrder.BY_ACID)
                true
            }
            R.id.action_sort_by_sweetness -> {
                noteViewModel.onSortOrderSelected(SortOrder.BY_SWEET)
                true
            }
            R.id.action_sort_by_balance -> {
                noteViewModel.onSortOrderSelected(SortOrder.BY_BALANCE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(note: Note, tags: Array<Tag>) {
        noteViewModel.onNoteSelected(note, tags)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}