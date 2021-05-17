package com.cems.mvvmsturacture.ui.words

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
import com.blankj.utilcode.util.NetworkUtils
import com.cems.mvvmsturacture.R
import com.common.commondomain.interactor.words.WordsClassResult
import com.coredata.module.PreferenceModule
import com.coredomain.BaseVS
import com.coreui.ui.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.lang.StringBuilder
import java.util.prefs.Preferences
import javax.inject.Inject

@AndroidEntryPoint
class WordsFragment : BaseFragment<WordsViewModel, WordsIntent>() {

    @Inject
    lateinit var preferenceModule: PreferenceModule

    private lateinit var wordsProgressBar: ProgressBar
    private lateinit var resultTxt: TextView

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
                wordsProgressBar.visibility = View.GONE

                var stringWords = Html.fromHtml(state.model.string(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV).toString()
                val re = Regex("[^a-zA-Z0-9\\s\\u0600-\\u06FF\\u0750-\\u077F\\u08A0-\\u08FF\\uFB50-\\uFDCF\\uFDF0-\\uFDFF\\uFE70-\\uFEFF ]")
                stringWords = re.replace(stringWords, " ").split(" ").toString()

                val wordArrayList = re.replace(stringWords, " ").split(" ")
                val wordsSet = mutableSetOf<String>()
                val wordsList = mutableListOf<WordsModel>()

                for (text in wordArrayList) {
                    if (text != "") {
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

                val resultString: StringBuilder = StringBuilder()

                wordsList.forEach {
                    resultString.appendln(it.text + "   " + it.concurrency)
                }

                preferenceModule.setWordsList(resultString.toString())

                resultTxt.text = resultString
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
        resultTxt = view.findViewById(R.id.resultTxt)

        getWords()

        return view
    }

    fun getWords() {
        if (NetworkUtils.isConnected()) {

            wordsPublisher.onNext(WordsIntent.GetWordsIntent())
        } else {
            var result: String? = " "
            result = preferenceModule.getWordsList().toString()
            resultTxt.text = result
            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
        }
    }
}