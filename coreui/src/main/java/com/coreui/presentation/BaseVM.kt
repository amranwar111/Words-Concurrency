package com.coreui.presentation

import androidx.lifecycle.ViewModel
import com.coredomain.BaseVS
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject


abstract class BaseVM<I> : ViewModel(){
  var intentsSubject: PublishSubject<I> = PublishSubject.create()
  val statesSubject: Observable<BaseVS> = compose()
//
  abstract fun compose(): Observable<BaseVS>
//   return intentsSubject
//    .compose(getProcessor())
//    .replay(1)
//    .autoConnect(0)
//  }
//
  abstract fun getProcessor (): ObservableTransformer<I, BaseVS>?
//
//   fun processIntents(intents: Observable<I>) {
//   intents.subscribe(intentsSubject)
//  }
//
//   fun states(): Observable<BaseVS> {
//   return statesSubject
//  }
}