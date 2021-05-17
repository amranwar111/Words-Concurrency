package com.cems.mvvmsturacture.ui.words

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.NetworkUtils
import com.cems.mvvmsturacture.R
import com.common.commondomain.interactor.words.WordsClassResult
import com.coredata.module.PreferenceModule
import com.coredomain.BaseVS
import com.coreui.ui.fragment.BaseFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.prefs.Preferences
import javax.inject.Inject

@AndroidEntryPoint
class WordsFragment : BaseFragment<WordsViewModel, WordsIntent>() {

    @Inject
    lateinit var preferenceModule: PreferenceModule

    private lateinit var gson: Gson

    var resultString = ArrayList<WordsModel>()
    private lateinit var wordsProgressBar: ProgressBar
    private lateinit var wordsRecycler: RecyclerView

    private lateinit var adapter: WordsAdapter

    private val wordsPublisher = BehaviorSubject.create<WordsIntent.GetWordsIntent>()

    override fun intents(): Observable<WordsIntent> = wordsPublisher as Observable<WordsIntent>

    override fun getFragmentVM(): Class<out WordsViewModel> = WordsViewModel::class.java

    @RequiresApi(Build.VERSION_CODES.N)
    override fun render(state: BaseVS) {

        when (state) {
            is BaseVS.Loading -> {
                wordsProgressBar.visibility = View.VISIBLE
            }
            is BaseVS.Success -> {
                wordsProgressBar.visibility = View.GONE
            }
            is BaseVS.Error -> {
                wordsProgressBar.visibility = View.GONE
            }
            is WordsClassResult -> {
                var thread = Thread.currentThread()
                Log.e("TAG", "render: " + Thread.currentThread().name.toString())
                wordsProgressBar.visibility = View.GONE

                Log.e("TAG", "render: " + Thread.currentThread().name)
                var stringWords =
                    Html.fromHtml(state.model.string(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV)
                        .toString()
                val re =
                    Regex("[^a-zA-Z0-9\\s\\u0600-\\u06FF\\u0750-\\u077F\\u08A0-\\u08FF\\uFB50-\\uFDCF\\uFDF0-\\uFDFF\\uFE70-\\uFEFF ]")
                stringWords = re.replace(stringWords, " ").split(" ").toString()

                val wordArrayList = re.replace(stringWords, " ").split(" ")
                val wordsSet = mutableSetOf<String>()
                val wordsList = mutableListOf<WordsModel>()

                for (text in wordArrayList) {
                    if (text.trim() != "") {
                        wordsSet.add(text.trim())
                    }
                }

                wordsSet.forEach {
                    val wordsModel = WordsModel(text = it)
                    wordsList.add(wordsModel)
                }

                wordArrayList.forEach { concurrency ->
                    wordsList.forEach { theWord ->
                        if (concurrency == theWord.text) {
                            theWord.concurrency++
                        }
                    }
                }
                resultString = ArrayList()
                wordsList.forEach {
                    resultString.add(WordsModel(it.text, it.concurrency))
                }

                val resultGson = gson.toJson(resultString)

                preferenceModule.setWordsList(resultGson)

                adapter.addList(resultString)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_words, container, false)

        wordsProgressBar = view.findViewById(R.id.wordsProgressBar)
        wordsRecycler = view.findViewById(R.id.wordsRecycler)

        wordsRecycler.setHasFixedSize(true)
        adapter = WordsAdapter()
        gson = Gson()

        getWords()

        wordsRecycler.adapter = adapter

        return view
    }

    fun getWords() {
        if (isNetworkAvailable(requireContext())) {
            wordsPublisher.onNext(WordsIntent.GetWordsIntent())
        } else {
            val result = gson.fromJson(preferenceModule.getWordsList(), Array<WordsModel>::class.java).asList()
            Log.e("TAG", "getWords: " + result)
            adapter.addList(result as MutableList<WordsModel>)
            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                //It will check for both wifi and cellular network
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        } else {
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }
}