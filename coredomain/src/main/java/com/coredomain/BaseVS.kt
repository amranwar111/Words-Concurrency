package com.coredomain




interface   BaseVS {

    class Loading : BaseVS{
        var type = 0
        companion object {
            fun getWithType(t:Int):Loading{
                var instance = Loading()
                instance.type = t
                return instance
            }
        }
    }
    class Success : BaseVS{
        var type = 0
        var code = 200
        var message:String? = ""
        companion object {
            fun getWithType(t:Int):Success{
                var instance = Success()
                instance.type = t
                return instance
            }
        }
    }

    class Error(val error:Throwable) : BaseVS{
        var type = 0
        var message:String? = if (error is MyRemoteException) error.errorMessage
        else if(error is java.net.ConnectException) "من فضلك تأكد من إتصالك بالإنترنت" else ""
        var code = if (error is MyRemoteException) error.code else 0
        companion object {
            fun getWithType(error:Throwable,t:Int):Error{
                var errorInstance = Error(error)
                errorInstance.type = t
                errorInstance.message = if (error is MyRemoteException) error.errorMessage else ""
                errorInstance.code = if (error is MyRemoteException) error.code else 0
                return errorInstance
            }
        }
    }

    class Empty : BaseVS{
        var type = 0
        companion object {
            fun getWithType(t:Int):Empty{
                var instance = Empty()
                instance.type = t
                return instance
            }
        }
    }

    class Normal : BaseVS
}