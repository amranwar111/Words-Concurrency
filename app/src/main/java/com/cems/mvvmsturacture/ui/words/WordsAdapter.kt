package com.cems.mvvmsturacture.ui.words

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cems.mvvmsturacture.R
import java.util.ArrayList

class WordsAdapter() : RecyclerView.Adapter<WordsAdapter.ViewHolder>() {

    private val wordsList = ArrayList<WordsModel>()

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        val wordTxt: TextView = listItemView.findViewById(R.id.wordTxt)
        val concurrencyTxt: TextView = listItemView.findViewById(R.id.concurrencyTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsAdapter.ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.word_concurrency_item, parent, false)
        return ViewHolder(contactView)
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onBindViewHolder(holder: WordsAdapter.ViewHolder, position: Int) {

        val word: WordsModel = wordsList[position]

        holder.wordTxt.text = word.text
        holder.concurrencyTxt.text = word.concurrency.toString()
    }

    fun addList(list: ArrayList<WordsModel>) {
        wordsList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return wordsList.size
    }
}