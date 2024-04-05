package good.damn.filesharing.listeners.activityResult

interface ActivityResultCopyListener {

    fun onErrorCopyFile(
        errorMsg: String
    )

    fun onSuccessCopyFile(
        fileName: String
    )

}