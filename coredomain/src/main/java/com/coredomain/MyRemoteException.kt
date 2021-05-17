package com.coredomain

import java.lang.Exception

class MyRemoteException(val code:Int, val errorMessage: String) : Exception()