package good.damn.filesharing.controllers

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.server.ServerListener
import good.damn.filesharing.manager.request.RequestManager
import java.io.ByteArrayOutputStream
import java.net.ServerSocket
import java.net.SocketException
import java.net.SocketTimeoutException

class Server(
    val mPort: Int
): Runnable {

    private val TAG = "Server"

    private var mServer: ServerSocket? = null
    private var mServerListener: ServerListener? = null
        set(value) {
            mRequestManager.delegate = value
            field = value
        }

    private val mRequestManager = RequestManager()

    override fun run() {
        mServer = ServerSocket(
            mPort
        )

        mServer?.reuseAddress = true

        mServerListener?.onCreateServer(
            mServer!!
        )

        while (listen(Application.BUFFER_MB)) {
            // Listen...
        }
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

            Thread.sleep(500)
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

            Log.d(TAG, "listen: DATA SIZE: ${data.size}")

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