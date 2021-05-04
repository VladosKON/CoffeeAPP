package com.example.coffeeapp.ui.addeditnote

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.coffeeapp.R
import com.example.coffeeapp.data.Tag
import com.example.coffeeapp.databinding.NoteEditingViewBinding
import com.example.coffeeapp.databinding.NotePrefabBinding
import com.example.coffeeapp.util.exhaustive
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

//Фрагмент добавления и редактирования записи
@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.note_editing_view) {

    private val addEditNoteViewModel: AddEditNoteViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = NoteEditingViewBinding.bind(view)

        binding.apply {
            noteTitleMain.setText(addEditNoteViewModel.noteTitle)
            roastingDate.setText(addEditNoteViewModel.noteRoastDate)
            roasterInfo.setText(addEditNoteViewModel.noteRoastInfo)
            dryAroma.setText(addEditNoteViewModel.noteDryAroma)
            dryAromaSeekBar.progress = addEditNoteViewModel.noteDryAromaSB
            wetAroma.setText(addEditNoteViewModel.noteWetAroma)
            wetAromaSeekBar.progress = addEditNoteViewModel.noteWetAromaSB
            sweetness.setText(addEditNoteViewModel.noteSweetness)
            sweetnessSeekBar.progress = addEditNoteViewModel.noteSweetnessSB
            acidity.setText(addEditNoteViewModel.noteAcidity)
            aciditySeekBar.progress = addEditNoteViewModel.noteAciditySB
            body.setText(addEditNoteViewModel.noteBody)
            bodySeekBar.progress = addEditNoteViewModel.noteBodySB
            balanceSeekBar.progress = addEditNoteViewModel.noteBalanceSB
            flavour.setText(addEditNoteViewModel.noteFlavour)

            noteDateMain.isVisible = addEditNoteViewModel.note != null
            noteDateMain.text = addEditNoteViewModel.note?.createdDateFormatted
            addEditNoteViewModel.tags?.forEach { item ->
                addChipToGroup(TagsChipGroup, item)
            }

            noteTitleMain.addTextChangedListener {
                addEditNoteViewModel.noteTitle = it.toString()
            }

            roastingDate.addTextChangedListener {
                addEditNoteViewModel.noteRoastDate = it.toString()
            }

            roasterInfo.addTextChangedListener {
                addEditNoteViewModel.noteRoastInfo = it.toString()
            }

            dryAroma.addTextChangedListener {
                addEditNoteViewModel.noteDryAroma = it.toString()
            }

            wetAroma.addTextChangedListener {
                addEditNoteViewModel.noteWetAroma = it.toString()
            }

            sweetness.addTextChangedListener {
                addEditNoteViewModel.noteSweetness = it.toString()
            }

            acidity.addTextChangedListener {
                addEditNoteViewModel.noteAcidity = it.toString()
            }

            body.addTextChangedListener {
                addEditNoteViewModel.noteBody = it.toString()
            }

            flavour.addTextChangedListener {
                addEditNoteViewModel.noteFlavour = it.toString()
            }

            dryAromaSeekBar.progress = addEditNoteViewModel.note?.dryArSB ?: 1
            dryAromaSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    addEditNoteViewModel.noteDryAromaSB = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            wetAromaSeekBar.progress = addEditNoteViewModel.note?.wetArSB ?: 1
            wetAromaSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    addEditNoteViewModel.noteWetAromaSB = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })

            sweetnessSeekBar.progress = addEditNoteViewModel.note?.sweetSB ?: 1
            sweetnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    addEditNoteViewModel.noteSweetnessSB = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })

            aciditySeekBar.progress = addEditNoteViewModel.note?.acidSB ?: 1
            aciditySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    addEditNoteViewModel.noteAciditySB = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })

            bodySeekBar.progress = addEditNoteViewModel.note?.bodySB ?: 1
            bodySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    addEditNoteViewModel.noteBodySB = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })

            balanceSeekBar.progress = addEditNoteViewModel.note?.balanceSB ?: 1
            balanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    addEditNoteViewModel.noteBalanceSB = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })

            noteTagsMain.addTextChangedListener {
                if (it.toString().lastIndex != -1)
                    if (it.toString()[it.toString().lastIndex] == ' ') {
                        addEditNoteViewModel.onTagAdd(it.toString().trim())
                        noteTagsMain.setText("")
                    }
            }

            fabAddNote.setOnClickListener {
                addEditNoteViewModel.onAddClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            addEditNoteViewModel.addEditNoteEvent.collect { event ->
                when (event) {
                    is AddEditNoteViewModel.AddEditNoteEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditNoteViewModel.AddEditNoteEvent.NavigateBackWithResult -> {
                        binding.noteTitleMain.clearFocus()
                        binding.roastingDate.clearFocus()
                        binding.roasterInfo.clearFocus()
                        binding.noteDateMain.clearFocus()
                        binding.dryAroma.clearFocus()
                        binding.dryAromaSeekBar.clearFocus()
                        binding.wetAroma.clearFocus()
                        binding.wetAromaSeekBar.clearFocus()
                        binding.sweetness.clearFocus()
                        binding.sweetnessSeekBar.clearFocus()
                        binding.acidity.clearFocus()
                        binding.aciditySeekBar.clearFocus()
                        binding.body.clearFocus()
                        binding.bodySeekBar.clearFocus()
                        binding.balanceSeekBar.clearFocus()
                        binding.flavour.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditNoteViewModel.AddEditNoteEvent.CreateNewChipForTag -> {
                        addChipToGroup(binding.TagsChipGroup, event.tag)
                    }
                }.exhaustive
            }
        }
    }

    private fun addChipToGroup(chipGroup: ChipGroup, tag:   Tag) {
        val chip = Chip(context)
        chip.text = tag.name

        chip.isClickable = true
        chip.isCheckable = false
        chip.isCloseIconVisible = true

        chipGroup.addView(chip, chipGroup.childCount - 1)

        chip.setOnCloseIconClickListener {
            chipGroup.removeView(chip)
            addEditNoteViewModel.onTagDelete(tag)
        }
    }

}