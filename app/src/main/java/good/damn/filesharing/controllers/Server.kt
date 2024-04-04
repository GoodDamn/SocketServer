package good.damn.filesharing.controllers

import android.util.Log
import androidx.annotation.WorkerThread
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.NetworkInputListener
import good.damn.filesharing.listeners.network.server.ServerListener
import good.damn.filesharing.manager.RequestManager
import java.io.ByteArrayOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException
import java.nio.charset.Charset

class Server(
    port: Int
): Runnable {

    private val TAG = "Server"

    private var mHostPort = port

    private var mServer: ServerSocket? = null
    private var mServerListener: ServerListener? = null

    private var mResponseType = 2 // text
    private var mResponse = byteArrayOf(48)
    private var mResponseText = byteArrayOf(48)

    private val mRequestManager = RequestManager()

    init {
        mRequestManager.delegate = mServerListener
    }

    override fun run() {
        mServer = ServerSocket(mHostPort)
        mServer?.reuseAddress = true

        mServerListener?.onCreateServer(
            mServer!!
        )

        while (listen(Application.BUFFER_MB)) {
            // Listen...
        }
    }

    fun setResponse(
        data: ByteArray,
        fileName: String
    ) {
        mResponseType = 1
        mResponse = data
        mResponseText = fileName
            .toByteArray(Application.CHARSET)
    }

    fun setResponseText(
        text: String
    ) {
        mResponseType = 2
        mResponse = byteArrayOf()
        mResponseText = text
            .toByteArray(Application.CHARSET)
    }

    fun create() {
        Thread(this)
            .start()
    }

    fun drop() {
        mServer?.close()
        mServerListener?.onDropServer()
    }

    fun setOnServerListener(
        listener: ServerListener?
    ) {
        mServerListener = listener
    }

    private fun listen(
        buffer: ByteArray
    ): Boolean {

        mServerListener?.onStartListen()
        try {
            val clientSocket = mServer!!.accept()
            clientSocket.soTimeout = 13000

            val out = clientSocket.getOutputStream()
            val inp = clientSocket.getInputStream()

            val outArr = ByteArrayOutputStream()

            var n: Int

            val typeIn = inp.read()
            outArr.write(typeIn) // type

            Thread.sleep(750)
            while (true) {
                Log.d(TAG, "listen: READ ${inp.available()} ${outArr.size()}")
                if (inp.available() == 0) {
                    break
                }

                n = inp.read(buffer)
                Log.d(TAG, "listen: READ $n ${outArr.size()}")
                mServerListener?.onListenChunkData(
                    buffer,
                    n,
                    inp.available()
                )

                if (n == -1) {
                    break
                }

                outArr.write(
                    buffer,
                    0,
                    n
                )
            }

            val data = outArr.toByteArray()
            outArr.close()

            Log.d(TAG, "listen: DATA SIZE: ${data.size} RESPONSE TYPE: $typeIn")

            out.write(
                mRequestManager.manage(
                    data
                )
            )

            mServerListener?.onDropClient(
                clientSocket
            )

            out.close()
        } catch (e: SocketException) {
            Log.d(TAG, "listen: EXCEPTION:  ${e.message}")
            return false
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "listen: TIMED_OUT: ${e.message}")
        }

        return true
    }
}