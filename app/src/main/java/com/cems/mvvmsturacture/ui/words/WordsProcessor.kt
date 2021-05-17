package com.cems.mvvmsturacture.ui.words

import com.common.commondomain.interactor.words.WordsUseCase
import com.coredomain.BaseVS
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class WordsProcessor @Inject constructor(val wordsUseCase: WordsUseCase) {

    private val getWordsProcessor: ObservableTransformer<WordsIntent.GetWordsIntent, BaseVS> =
        ObservableTransformer { it ->
            it.switchMap {
                wordsUseCase.execute()
                    .toObservable()
                    .map { it as BaseVS }
                    .onErrorReturn { e -> BaseVS.Error(e) }
                    .startWith(BaseVS.Loading())
            }
        }

    var myActionProcessor: ObservableTransformer<WordsIntent, BaseVS>

    init {
        this.myActionProcessor = ObservableTransformer { it ->
            it.publish {
                it.ofType(WordsIntent.GetWordsIntent::class.java)
                    .compose(getWordsProcessor)
                    .mergeWith(it.filter { a -> a !is WordsIntent }
                        .flatMap {
                            Observable.error<BaseVS>(
                                IllegalArgumentException("Unknown Action type")
                            )
                        })
            }
        }
    }
}