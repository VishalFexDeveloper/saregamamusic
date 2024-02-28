package com.example.entertainment.MusicFragmentChild

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.entertainment.R
import com.example.moodmusic.Adapters.playlistAdapterMaltiplview

class PlaylistsFragment : Fragment() {

    lateinit var playList: ArrayList<String>
    lateinit var customAdapter: playlistAdapterMaltiplview
    lateinit var newFolderCrate:LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layoutView = inflater.inflate(R.layout.fragment_playlists, container, false)

        val recyclerView: RecyclerView = layoutView.findViewById(R.id.playlists_RecyclerView)
        newFolderCrate = layoutView.findViewById(R.id.new_folderAdd_LinearLayout)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        playList = arrayListOf()


        customAdapter = playlistAdapterMaltiplview(requireContext(),playList)
        recyclerView.adapter = customAdapter

        newFolderCrate.setOnClickListener {
            addnewFolder()
        }

        return layoutView
    }


    private fun addnewFolder() {
        var totalText: String = ""
        val createBtnVisibility: TextView
        val createBtn: TextView
        val textcolor200: TextView

        val alertDialog = AlertDialog.Builder(requireContext()).create()
        val layout = LayoutInflater.from(context).inflate(R.layout.new_playlist_dialog, null, false)
        val cancelBtnVisibility: ImageButton = layout.findViewById(R.id.edit_query_visibility_dialog)
        val editTextQuer: EditText = layout.findViewById(R.id.edit_query_text_dailog)
        val textLength: TextView = layout.findViewById(R.id.editTextLenath_Dailog)
        val cancelBtn: TextView = layout.findViewById(R.id.cancel_Btn_Dailog)
        textcolor200 = layout.findViewById(R.id.playlist_200)
        createBtnVisibility = layout.findViewById<TextView>(R.id.create_Btn_visibility_Dailog)
        createBtn = layout.findViewById<TextView>(R.id.create_Btn_Dailog)

        alertDialog.setCancelable(false)

        cancelBtn.setOnClickListener {
            alertDialog.cancel()
        }
        createBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setView(layout)
        alertDialog.show()

        editTextQuer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                totalText = s.toString()
                val length = totalText.length
                textLength.text = "$length / 50"

                cancelBtnVisibility.setOnClickListener {
                    editTextQuer.text.clear()
                }



                if (totalText.isEmpty()) {
                    cancelBtnVisibility.visibility = View.GONE
                    createBtnVisibility.visibility = View.VISIBLE
                    createBtn.visibility = View.GONE
                } else {
                    cancelBtnVisibility.visibility = View.VISIBLE
                    createBtnVisibility.visibility = View.GONE
                    createBtn.visibility = View.VISIBLE

                    if (length > 50) {
                        textcolor200.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    } else {
                        textcolor200.setTextColor(ContextCompat.getColor(requireContext(), R.color.DarkGray))
                    }
                }
            }
        })
    }



}